package com.sonfind.chelsea.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sonfind.chelsea.domain.teams.Recruitment;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
}
