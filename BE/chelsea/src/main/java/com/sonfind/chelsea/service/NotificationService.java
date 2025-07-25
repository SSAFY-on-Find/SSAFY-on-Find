package com.sonfind.chelsea.domain.service;

import org.springframework.stereotype.Service;

import com.sonfind.chelsea.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;

	
}
