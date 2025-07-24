package com.sonfind.chelsea.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.auth.Auth;
import com.sonfind.chelsea.repository.AuthRepository;

@Service
public class AuthService {

	@Autowired
	private final AuthRepository authRepository;

	public AuthService(AuthRepository authRepository) {
		this.authRepository = authRepository;
	}

	public Auth findByMateId(long mateId) {

		Optional<Auth> authOptional = authRepository.findByMateId(mateId);
		Auth auth = null;

		if (authOptional.isPresent()) {
			auth = authOptional.get();
		}

		return auth;
	}

}
