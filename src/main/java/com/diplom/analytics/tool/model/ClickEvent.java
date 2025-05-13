package com.diplom.analytics.tool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClickEvent extends TrackerEvent {
    private int x;
    private int y;
}