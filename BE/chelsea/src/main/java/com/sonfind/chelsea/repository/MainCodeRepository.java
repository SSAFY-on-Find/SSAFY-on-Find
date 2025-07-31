package com.sonfind.chelsea.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sonfind.chelsea.global.domain.MainCode;

@Repository
public interface MainCodeRepository extends JpaRepository<MainCode, String> {
}
