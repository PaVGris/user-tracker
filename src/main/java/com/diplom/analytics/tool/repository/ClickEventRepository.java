package com.diplom.analytics.tool.repository;

import com.diplom.analytics.tool.model.ClickHeatmapDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClickEventRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ClickEventRepository(@Qualifier("clickHouseJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ClickHeatmapDto> getClickHeatmapDataByClientAndPage(String clientId, String pageUrl) {
        String sql = "SELECT x, y, COUNT(*) AS value " +
                "FROM default.click_events " +
                "WHERE client_id = :clientId AND page_url = :pageUrl " +
                "GROUP BY x, y";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("clientId", clientId)
                .addValue("pageUrl", pageUrl);

        RowMapper<ClickHeatmapDto> rowMapper = (rs, rowNum) -> new ClickHeatmapDto(
                rs.getInt("x"),
                rs.getInt("y"),
                rs.getLong("value")
        );

        return jdbcTemplate.query(sql, params, rowMapper);
    }
}
