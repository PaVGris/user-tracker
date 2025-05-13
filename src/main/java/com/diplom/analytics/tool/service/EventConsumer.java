package com.diplom.analytics.tool.service;

import com.diplom.analytics.tool.model.ClickEvent;
import com.diplom.analytics.tool.model.PageSessionEvent;
import com.diplom.analytics.tool.model.PageViewEvent;
import com.diplom.analytics.tool.model.ScrollEvent;
import com.diplom.analytics.tool.repository.TrackerEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventConsumer {
    private final TrackerEventRepository repository;

    @KafkaListener(topics = "tracker-clicks")
    public void handleClicks(List<ClickEvent> events) {
        repository.saveClicksBatch(events);
    }

    @KafkaListener(topics = "tracker-scrolls")
    public void handleScrolls(List<ScrollEvent> events) {
        repository.saveScrollsBatch(events);
    }

    @KafkaListener(topics = "tracker-sessions")
    public void handleSessions(List<PageSessionEvent> events) {
        repository.saveSessionsBatch(events);
    }

    @KafkaListener(topics = "tracker-pageviews")
    public void handlePageViews(List<PageViewEvent> events) {
        repository.savePageViewsBatch(events);
    }
}