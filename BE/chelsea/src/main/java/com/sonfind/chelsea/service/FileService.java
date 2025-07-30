package com.sonfind.chelsea.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileService {

	@Value("${upload.dir:/uploads}")
	private String uploadDirPath;

	public String uploadFile(MultipartFile file, String type) throws IOException {
		Path uploadPath = Paths.get(uploadDirPath + "/" + type);

		//해당하는 폴더가 없다면 새로 생성
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		String savedFileName = getSaveFileName(file.getOriginalFilename());
		Path filePath = uploadPath.resolve(savedFileName);
		file.transferTo(filePath.toFile());

		return savedFileName;
	}

	private String getSaveFileName(String originalFilename) {

		String extension = "";
		if (originalFilename.contains(".")) {
			extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		}

		String uuid = UUID.randomUUID().toString();
		String saveFilename = uuid + extension;

		return saveFilename;
	}

}
