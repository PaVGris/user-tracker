package com.diplom.analytics.tool.controller;

import com.diplom.analytics.tool.model.ClickEvent;
import com.diplom.analytics.tool.model.PageSessionEvent;
import com.diplom.analytics.tool.model.PageViewEvent;
import com.diplom.analytics.tool.model.ScrollEvent;
import com.diplom.analytics.tool.service.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tracker")
public class TrackerController {

    @Autowired
    private EventProducer metricEventProducer;

    @PostMapping("/click")
    public ResponseEntity<Void> receiveClick(@RequestBody ClickEvent event) {
        metricEventProducer.sendEvent(event);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/scroll")
    public ResponseEntity<Void> receiveScroll(@RequestBody ScrollEvent event) {
        metricEventProducer.sendEvent(event);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/siteSession")
    public ResponseEntity<Void> receiveSession(@RequestBody PageSessionEvent event) {
        metricEventProducer.sendEvent(event);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pageView")
    public ResponseEntity<Void> receivePageView(@RequestBody PageViewEvent event) {
        metricEventProducer.sendEvent(event);
        return ResponseEntity.ok().build();
    }

}