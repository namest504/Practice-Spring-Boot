package com.list.multipartfile.repository;

import com.list.multipartfile.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageData, Long> {
    Optional<ImageData> findByName(String fileName);
}
