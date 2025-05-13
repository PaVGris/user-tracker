package com.diplom.analytics.tool.repository;

import com.diplom.analytics.tool.model.ClickEvent;
import com.diplom.analytics.tool.model.PageSessionEvent;
import com.diplom.analytics.tool.model.PageViewEvent;
import com.diplom.analytics.tool.model.ScrollEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrackerEventRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TrackerEventRepository(@Qualifier("clickHouseJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveClicksBatch(List<ClickEvent> batch) {
        String sql = """
            INSERT INTO default.click_events (client_id, page_url, x, y, timestamp)
            VALUES (:clientId, :pageUrl, :x, :y, fromUnixTimestamp64Milli(:timestamp))
            """;
        jdbcTemplate.batchUpdate(sql, batch.stream()
                .map(event -> new MapSqlParameterSource()
                    .addValue("clientId", event.getClientId())
                    .addValue("pageUrl", event.getPageUrl())
                    .addValue("x", event.getX())
                    .addValue("y", event.getY())
                    .addValue("timestamp", event.getTimestamp()))
                .toArray(MapSqlParameterSource[]::new));
    }

    public void saveScrollsBatch(List<ScrollEvent> batch) {
        String sql = """
            INSERT INTO default.scroll_events (client_id, page_url, scroll_depth, timestamp)
            VALUES (:clientId, :pageUrl, :scrollDepth, fromUnixTimestamp64Milli(:timestamp))
            """;

        jdbcTemplate.batchUpdate(sql, batch.stream()
                .map(event -> new MapSqlParameterSource()
                    .addValue("clientId", event.getClientId())
                    .addValue("pageUrl", event.getPageUrl())
                    .addValue("scrollDepth", event.getScrollDepth())
                    .addValue("timestamp", event.getTimestamp()))
                .toArray(MapSqlParameterSource[]::new));
    }

    public void saveSessionsBatch(List<PageSessionEvent> batch) {
        String sql = """
            INSERT INTO default.page_sessions (client_id, session_id, duration, timestamp)
            VALUES (:clientId, :sessionId, :duration, fromUnixTimestamp64Milli(:timestamp))
            """;

        jdbcTemplate.batchUpdate(sql, batch.stream()
                .map(event -> new MapSqlParameterSource()
                        .addValue("clientId", event.getClientId())
                        .addValue("sessionId", event.getSessionId())
                        .addValue("duration", event.getTimeSpent())
                        .addValue("timestamp", event.getTimestamp()))
                .toArray(MapSqlParameterSource[]::new));
    }

    public void savePageViewsBatch(List<PageViewEvent> batch) {
        String sql = """
            INSERT INTO default.page_views (client_id, page_url, timestamp, duration)
            VALUES (:clientId, :pageUrl, fromUnixTimestamp64Milli(:timestamp), :duration)
            """;

        jdbcTemplate.batchUpdate(sql, batch.stream()
                .map(event -> new MapSqlParameterSource()
                        .addValue("clientId", event.getClientId())
                        .addValue("pageUrl", event.getPageUrl())
                        .addValue("timestamp", event.getTimestamp())
                        .addValue("duration", event.getTimeSpent()))
                .toArray(MapSqlParameterSource[]::new));
    }

    public static class TrackerPersistenceException extends RuntimeException {
        public TrackerPersistenceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
