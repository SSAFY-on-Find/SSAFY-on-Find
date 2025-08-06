package com.sonfind.chelsea.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.global.domain.SubCode;

@Repository
public interface SubCodeRepository extends JpaRepository<SubCode, String> {

	SubCode findBySubCode(String subCode);

	List<SubCode> findAllBySubCodeIn(List<String> subCodes);

	SubCode findBySubCodeName(String subCodeName);

	@Query("SELECT s FROM SubCode s WHERE s.mainCode.mainCode = :mainCodeValue AND s.useYn = true")
	List<SubCode> findByMainCodeAndUseYnTrue(@Param("mainCodeValue") String mainCodeValue);

}
