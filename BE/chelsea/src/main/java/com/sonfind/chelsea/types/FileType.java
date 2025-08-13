package com.sonfind.chelsea.types;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {
	PROFILE("profiles", List.of("png", "jpg", "jpeg", "gif"), 1 * 1024 * 1024),
	PORTFOLIO("portfolios", List.of("png", "jpg", "pdf", "pptx", "docx", "jpeg", "ppt"), 50 * 1024 * 1024);

	private final String directoryName;
	private final List<String> allowedExtensions;
	private final long maxSize;
}
