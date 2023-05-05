package com.list.multipartfile.service;

import com.list.multipartfile.entity.FileData;
import com.list.multipartfile.repository.FileDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class FileService {

    private FileDataRepository fileDataRepository;

    private final String FOLDER_PATH;

    public FileService (@Value("${img.dir}") String imgDir, FileDataRepository fileDataRepository){
        this.FOLDER_PATH = imgDir;
        this.fileDataRepository = fileDataRepository;
    }

    public String uploadImageToFileSystem(MultipartFile file, HttpServletRequest req) throws IOException {

        String filePath = FOLDER_PATH + file.getOriginalFilename();
        System.out.println("filePath = " + filePath);
        FileData fileData = fileDataRepository.save(
                FileData.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .filePath(filePath)
                        .build()
        );

        // 파일 결로
        file.transferTo(new File(filePath));

        if (fileData != null) {
            return "업로드 경로 : " + filePath;
        }
        return null;
    }

    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        FileData fileData = fileDataRepository.findByName(fileName)
                .orElseThrow(RuntimeException::new);

        String filePath = fileData.getFilePath();

        return Files.readAllBytes(new File(filePath).toPath());
    }
}
