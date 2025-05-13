package com.diplom.analytics.tool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageViewEvent extends TrackerEvent{
    private long timeSpent;
}
