package com.sonfind.chelsea.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.global.domain.SubCode;

@Repository
public interface SubCodeRepository extends JpaRepository<SubCode, String> {

	//subcode 테이블에서 입력한 mainCode 문자열과 일치하는 모든 SubCode 반환
	//e.g. findByMainCode_MainCode("TRK") -> TRK001, TRK002,...
	List<SubCode> findByMainCode_MainCode(String mainCode);

	SubCode findBySubCode(String subCode);
}
