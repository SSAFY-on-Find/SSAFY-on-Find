package com.sonfind.chelsea.global.event;

import com.sonfind.chelsea.dto.notification.HasPublisher;
import com.sonfind.chelsea.dto.notification.HasResponse;
import com.sonfind.chelsea.dto.notification.HasSubscriber;
import com.sonfind.chelsea.dto.notification.NotificationDto;
import com.sonfind.chelsea.factory.RequestPayloadFactory;
import com.sonfind.chelsea.factory.ResponsePayloadFactory;
import com.sonfind.chelsea.global.error.AppException;
import com.sonfind.chelsea.global.error.ErrorCode;
import com.sonfind.chelsea.service.SseService;
import com.sonfind.chelsea.types.NotificationDomainType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

/**
 * 알림 이벤트 리스너
 * NotificationEvent를 수신하여 알림을 발송하는 역할을 합니다.
 * 이벤트 타입에 따라 발신자와 수신자에게 알림을 전송합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

	private final SseService sseService;
	private final List<RequestPayloadFactory> requestPayloadFactories;
	private final List<ResponsePayloadFactory> responsePayloadFactories;

	/**
	 * 알림 이벤트 리스너
	 * 이벤트 타입에 따라 발신자와 수신자에게 알림을 전송합니다.
	 */
	@EventListener
	@TransactionalEventListener(phase = AFTER_COMMIT)
	public void onRequest(InvitationRequestEvent e) {

		RequestPayloadFactory factory = requestPayloadFactories.stream()
				.filter(f -> f.supportType() == e.getType())
				.findFirst()
				.orElseThrow(() -> new AppException(ErrorCode.NOT_SUPPORTED_TYPE));

		NotificationDto<?> pubPayload = factory.createPublisherPayload(e);
		NotificationDto<?> subPayload = factory.createSubscriberPayload(e);

		HasPublisher pubData = (HasPublisher) pubPayload.data();
		HasSubscriber subData = (HasSubscriber) subPayload.data();

		switch (e.getType()) {
			case APPLICATION, INVITATION, MERGE -> {
				log.info("알림 발송함~");
				sendBoth(pubData, pubPayload, subData, subPayload);
			}
			default -> {
				// 지원, 초대, 병합 외의 타입은 처리하지 않음
				throw new AppException(ErrorCode.NOT_SUPPORTED_TYPE);
			}
		}
	}

	@EventListener
	@TransactionalEventListener(phase = AFTER_COMMIT)
	public void onResponse(InvitationResponseEvent e) {

		ResponsePayloadFactory factory = responsePayloadFactories.stream()
				.filter(f -> f.supportStatus(e) == e.getStatus())
				.findFirst()
				.orElseThrow(() -> new AppException(ErrorCode.NOT_SUPPORTED_TYPE));

		NotificationDto<?> payload = factory.createPayload(e);

		HasResponse data = (HasResponse) payload.data();

		switch (e.getStatus()) {
			case ACCEPTED, REJECTED, CANCELED -> {

				sendBoth(
						data.pubId(),
						data.pubType(),
						data.subId(),
						data.subType(),
						payload
				);
			}
			default -> {
				// 수락/거절/취소 외의 상태는 처리하지 않음
				throw new AppException(ErrorCode.NOT_SUPPORTED_TYPE);
			}
		}
	}

	private void sendBoth(HasPublisher pubData, NotificationDto<?> pubPayload, HasSubscriber subData,
	                      NotificationDto<?> subPayload) {
		sseService.dispatch(
				pubData.publisher().id(),
				pubData.publisher().type(),
				pubPayload
		);
		sseService.dispatch(
				subData.subscriber().id(),
				subData.subscriber().type(),
				subPayload
		);
	}

	private <T> void sendBoth(Long pubId, NotificationDomainType pubType,
	                          Long subId, NotificationDomainType subType,
	                          NotificationDto<T> payload) {
		sseService.dispatch(pubId, pubType, payload);
		sseService.dispatch(subId, subType, payload);
	}
}
