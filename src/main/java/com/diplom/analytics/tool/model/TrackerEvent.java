package com.diplom.analytics.tool.model;

import lombok.Data;

@Data
public class TrackerEvent {
    protected String clientId;
    protected String sessionId;
    protected String pageUrl;
    protected long timestamp;
}