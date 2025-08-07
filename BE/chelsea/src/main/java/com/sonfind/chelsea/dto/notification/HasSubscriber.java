package com.sonfind.chelsea.dto.notification;

public interface HasSubscriber<T extends CommonField> {
	CommonField subscriber();
}
