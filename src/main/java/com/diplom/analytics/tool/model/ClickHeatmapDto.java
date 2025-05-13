package com.diplom.analytics.tool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClickHeatmapDto {
    private int x;
    private int y;
    private long value;
}