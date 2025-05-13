package com.diplom.analytics.tool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrollEvent extends TrackerEvent {
    private float scrollDepth; // 0.0 до 1.0
}
