package com.sonfind.chelsea.global.event;

import java.util.Objects;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChannelInBoundInterceptor implements ChannelInterceptor {

	// private final CrewService crewService;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		log.info("-------------------Handling start-----------------------");
		log.info("command: {}", accessor.getCommand());
		log.info("destination: {}", accessor.getDestination());
		log.info("roomId: {}", accessor.getFirstNativeHeader("roomId"));
		log.info("message: {}", accessor.getMessage());

		handleMessage(accessor.getCommand(), accessor);

		log.info("-------------------Handling confirm-----------------------");
		return message;
	}

	private void handleMessage(StompCommand command, StompHeaderAccessor accessor) {
		if (command.equals(StompCommand.SUBSCRIBE)) {
			handleSubscribe(accessor);
		}

		if (command.equals(StompCommand.UNSUBSCRIBE)) {
			handleUnsubscribe(accessor);
		}
	}

	private void handleSubscribe(StompHeaderAccessor accessor) {
		log.info("---------------Subscribing start-----------------------");
		log.info("memberId: {}", accessor.getFirstNativeHeader("memberId"));
		log.info("roomId: {}", accessor.getFirstNativeHeader("roomId"));

		Long memberId = Long.parseLong(Objects.requireNonNull(accessor.getFirstNativeHeader("memberId")));
		Long chatRoomId = Long.parseLong(Objects.requireNonNull(accessor.getFirstNativeHeader("roomId")));

		//입장
		//해당 멤버 아이디를 받아서 접속상태를 변경
		//안 읽은 메시지 읽음 처리
		//퇴장 시간 이후부터 접속시간 이전까지의 메시지 읽음 처리
		// crewService.connect(new CrewId(memberId, chatRoomId));

		log.info("---------------Subscribing end-----------------------");
	}

	private void handleUnsubscribe(StompHeaderAccessor accessor) {
		log.info("---------------Unsubscribing start-----------------------");
		log.info("studenId: {}", accessor.getFirstNativeHeader("studenId"));
		log.info("roomId: {}", accessor.getFirstNativeHeader("roomId"));

		//퇴장
		//해당 멤버 아이디를 받아서 접속상태와 퇴장 시각을 현 시점으로 변경
		Long studentId = Long.parseLong(Objects.requireNonNull(accessor.getFirstNativeHeader("studenId")));
		Long chatRoomId = Long.parseLong(Objects.requireNonNull(accessor.getFirstNativeHeader("roomId")));

		// crewService.disconnect(new CrewId(memberId, chatRoomId));

		log.info("---------------Unsubscribing end-----------------------");
	}
}
