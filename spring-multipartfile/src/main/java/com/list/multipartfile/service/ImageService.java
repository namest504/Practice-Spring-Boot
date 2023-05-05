package com.list.multipartfile.service;

import com.list.multipartfile.entity.ImageData;
import com.list.multipartfile.repository.ImageRepository;
import com.list.multipartfile.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public String uploadImage(MultipartFile file) throws IOException {
        log.info("upload file: {}", file);
        ImageData imageData = imageRepository.save(
                ImageData.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .imageData(ImageUtil.compressImage(file.getBytes()))
                        .build());
        if (imageData != null) {
            log.info("imageData: {}", imageData);
            return "file uploaded successfully : " + file.getOriginalFilename();
        }

        return null;
    }

    // 이미지 파일로 압축하기
    public byte[] downloadImage(String fileName) {
        ImageData imageData = imageRepository.findByName(fileName)
                .orElseThrow(RuntimeException::new);

        log.info("download imageData: {}", imageData);

        return ImageUtil.decompressImage(imageData.getImageData());
    }
}
