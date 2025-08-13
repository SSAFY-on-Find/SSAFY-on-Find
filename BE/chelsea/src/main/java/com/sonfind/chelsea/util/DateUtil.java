package com.sonfind.chelsea.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public final class DateUtil {

	private DateUtil() {
	} // 유틸 클래스: 인스턴스화 방지

	private static final ZoneId KST = ZoneId.of("Asia/Seoul");

	// 예: 7월 20일 오후 9:20 (동년), 2024년 7월 20일 오후 9:20 (이전/다음 해)
	private static final DateTimeFormatter KO_MD_AP_HM =
			DateTimeFormatter.ofPattern("M월 d일 a h:mm", Locale.KOREAN);
	private static final DateTimeFormatter KO_YMD_AP_HM =
			DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h:mm", Locale.KOREAN);

	/**
	 * 같은 해면 'yyyy년' 생략
	 */
	public static String formatKoShort(Date date) {
		if (date == null) return null;
		ZonedDateTime zdt = ZonedDateTime.ofInstant(date.toInstant(), KST);
		boolean sameYear = zdt.getYear() == LocalDate.now(KST).getYear();
		return zdt.format(sameYear ? KO_MD_AP_HM : KO_YMD_AP_HM);
	}

	/**
	 * 항상 'M월 d일 a h:mm' (연도 생략 고정)
	 */
	public static String formatKoMonthDay(Date date) {
		if (date == null) return null;
		return ZonedDateTime.ofInstant(date.toInstant(), KST).format(KO_MD_AP_HM);
	}

	/**
	 * Instant 오버로드
	 */
	public static String formatKoShort(Instant instant) {
		if (instant == null) return null;
		return formatKoShort(Date.from(instant));
	}
}
