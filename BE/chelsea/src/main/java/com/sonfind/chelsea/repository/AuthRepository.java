package com.sonfind.chelsea.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.auth.Auth;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
	Optional<Auth> findByMateId(Long mateId); // 원시형 vs wrapper 형?
}
