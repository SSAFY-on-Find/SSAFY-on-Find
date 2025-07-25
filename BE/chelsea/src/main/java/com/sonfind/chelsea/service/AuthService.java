package com.sonfind.chelsea.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.auth.Auth;
import com.sonfind.chelsea.repository.AuthRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthRepository authRepository;

	public Auth findByMateId(long mateId) {

		Optional<Auth> authOptional = authRepository.findByMateId(mateId);
		Auth auth = null;

		if (authOptional.isPresent()) {
			auth = authOptional.get();
		}

		return auth;
	}

}
