package com.sonfind.chelsea.dto.dashboard;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class TrackAggregationResult {
	public int totalCount = 0;
	public Map<String, Integer> majorRecord = new HashMap<>();
	public Map<String, Integer> positionRecord = new HashMap<>();
}
