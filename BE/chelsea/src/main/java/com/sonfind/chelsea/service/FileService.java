package com.sonfind.chelsea.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sonfind.chelsea.domain.studentInfo.UploadedFile;
import com.sonfind.chelsea.types.FileType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileService {

	@Value("${upload.dir:/uploads}")
	private String uploadDirPath;

	@Value("${custom.service.url}")
	private String serviceUrl;

	//프로필 이미지 저장 처리
	public String saveProfileImage(MultipartFile profile) throws IOException {

		if (profile == null || profile.isEmpty()) {
			return "";
		}
		return uploadFile(profile, FileType.PROFILE);
	}

	//포트폴리오 저장
	public UploadedFile savePortfolio(MultipartFile portfolio) throws IOException {

		if (portfolio == null || portfolio.isEmpty()) {
			return null;
		}

		String savedFilename = uploadFile(portfolio, FileType.PORTFOLIO);
		return UploadedFile.builder()
			.originalFileName(portfolio.getOriginalFilename())
			.savedFileName(savedFilename)
			.build();
	}

	// 파일 저장
	private String uploadFile(MultipartFile file, FileType fileType) throws IOException {

		validateFile(file, fileType);

		Path uploadPath = Paths.get(uploadDirPath + "/" + fileType.getDirectoryName());
		//해당하는 폴더가 없다면 새로 생성
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		String savedFileName = getSaveFileName(file.getOriginalFilename());
		Path filePath = uploadPath.resolve(savedFileName);
		file.transferTo(filePath.toFile());

		return serviceUrl + "/uploads/" + fileType.getDirectoryName() + "/" + savedFileName;
	}

	//파일 유효성 검사(크기 + 형식)
	private void validateFile(MultipartFile file, FileType fileType) throws FileUploadException {
		if (file.getSize() > fileType.getMaxSize()) {
			throw new FileUploadException(
				"파일 크기가 너무 큽니다. 최대 " + (fileType.getMaxSize() / 1024 / 1024) + "MB까지 업로드 할 수 있습니다."
			);
		}

		String extension = getExtension(file.getOriginalFilename());
		if (!fileType.getAllowedExtensions().contains(extension)) {
			throw new FileUploadException("지원하지 않는 파일 형식입니다. ");
		}
	}

	//파일 저장 이름 생성_난수값으로 생성
	private String getSaveFileName(String originalFileName) {

		String extension = getExtension(originalFileName);
		String uuid = UUID.randomUUID().toString();

		return uuid + "." + extension;
	}

	// 파일 확장자 얻는 함수
	private String getExtension(String fileName) {

		if (fileName == null || fileName.isEmpty()) {
			return "";
		}

		int dotIndex = fileName.lastIndexOf(".");
		if (dotIndex == -1) {
			return "";
		}

		return fileName.substring(dotIndex + 1).toLowerCase();
	}

	//프로필 삭제
	public void deleteProfileImage(String profileImageUrl) {
		if (profileImageUrl != null || !profileImageUrl.equals("")) {
			Path filePath = Paths.get(
				uploadDirPath + "/" + FileType.PROFILE.getDirectoryName() + "/" + profileImageUrl);
			deleteFile(filePath);
		}

	}

	//포트폴리오 삭제
	public void deletePortfolioFile(UploadedFile portfolio) {
		if (portfolio != null) {
			Path filePath = Paths.get(
				uploadDirPath + "/" + FileType.PORTFOLIO.getDirectoryName() + "/" + portfolio.getSavedFileName());
			deleteFile(filePath);
		}

	}

	//파일 삭제
	private boolean deleteFile(Path filePath) {
		try {
			return Files.deleteIfExists(filePath);
		} catch (IOException e) {
			log.error("파일 삭제 실패: {}", filePath, e);
			return false;
		}
	}
	
}
