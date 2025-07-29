package com.sonfind.chelsea.global.manager;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * SSE Emitter 관리 매니저
 *
 * - 클라이언트마다 하나의 SseEmitter를 생성하고 관리
 * - 연결(completion, timeout, error) 이벤트를 처리하여 map에서 제거
 * - 필요한 경우, 초기 커넥션 확인용 코멘트를 전송
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SseEmitterManager {

	/**
	 * <pubId, SseEmitter> 맵.
	 * 키: 구독자의 ID (subId)
	 * 값: 해당 사용자의 SSE 연결 객체
	 */
	private final Map<Long, SseEmitter> emittersMap = new ConcurrentHashMap<>();

	/**
	 * 새로운 SSE 연결을 생성하고 관리 맵에 등록
	 *
	 * @param pubId  연결을 요청한 사용자 ID
	 * @return SseEmitter (스트리밍 응답 채널)
	 * @throws RuntimeException 초기 이벤트 전송에 실패할 경우
	 */
	public SseEmitter connect(Long pubId) {
		// 타임아웃: 20분 (밀리초 단위)
		SseEmitter emitter = new SseEmitter(60 * 20 * 1000L);

		// map에 등록하여 이후 sendTo 호출 시 조회 가능하도록 함
		emittersMap.put(pubId, emitter);

		// 연결이 완료되거나 오류/타임아웃 발생 시 map에서 제거
		emitter.onCompletion(() -> emittersMap.remove(pubId));
		emitter.onTimeout(() -> emittersMap.remove(pubId));
		emitter.onError((e) -> emittersMap.remove(pubId));

		// 초기 커넥션 확인용으로 빈 이벤트(comment) 한 번 전송
		try {
			emitter.send(SseEmitter.event().comment("connected"));
		} catch (IOException e) {
			log.error("Failed to send initial SSE event to user {}: {}", pubId, e.getMessage());
			emittersMap.remove(pubId);
			throw new RuntimeException("Failed to send initial SSE event", e);
		}

		return emitter;
	}

	/**
	 * 특정 사용자의 SSE 연결로 데이터를 전송
	 *
	 * @param subId  수신 대상 사용자 ID
	 * @param data    전송할 페이로드 (JSON 직렬화 가능한 객체)
	 */
	public void sendTo(Long subId, Object data) {
		SseEmitter emitter = emittersMap.get(subId);
		if (emitter != null) {
			try {
				// 이벤트 이름 "notification" 으로 데이터 전송
				emitter.send(SseEmitter.event()
					.name("notification")
					.data(data));
			} catch (Exception e) {
				// 전송 실패 시 로그 기록 후 map에서 제거
				log.error("Failed to send SSE event to user {}: {}", subId, e.getMessage());
				emittersMap.remove(subId);
			}
		}
	}
}
