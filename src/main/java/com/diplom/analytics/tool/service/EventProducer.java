package com.diplom.analytics.tool.service;

import com.diplom.analytics.auth.User;
import com.diplom.analytics.auth.UserService;
import com.diplom.analytics.tool.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EventProducer {
    private final KafkaTemplate<String, TrackerEvent> kafkaTemplate;

    @Autowired
    private UserService userService;

    public void sendEvent(TrackerEvent event) {
        String topic;
        User user = userService.getUserById(Long.valueOf(event.getClientId()));
        event.
                setPageUrl(event.getPageUrl().
                replaceFirst(Pattern.quote(user.getUrl()), ""));

        if (event instanceof ClickEvent) {
            topic = "tracker-clicks";
        } else if (event instanceof ScrollEvent) {
            topic = "tracker-scrolls";
        } else if (event instanceof PageSessionEvent) {
            topic = "tracker-sessions";
        } else if (event instanceof PageViewEvent) {
            topic = "tracker-pageviews";
        } else {
            throw new IllegalArgumentException("Unknown event type");
        }

        kafkaTemplate.send(topic, event);
    }
}