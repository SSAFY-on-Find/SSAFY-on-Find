package com.sonfind.chelsea.global.event;

import com.sonfind.chelsea.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DashboardEventListener {

	private final SseService sseService;

	@EventListener
	public void onDashboardEvent() {

	}
}
