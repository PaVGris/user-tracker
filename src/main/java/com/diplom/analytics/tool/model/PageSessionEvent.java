package com.diplom.analytics.tool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageSessionEvent extends TrackerEvent {
    private long timeSpent; // в миллисекундах
}