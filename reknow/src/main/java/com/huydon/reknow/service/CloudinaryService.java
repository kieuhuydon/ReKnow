package com.huydon.reknow.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile multipartFile) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    multipartFile.getBytes(),
                    ObjectUtils.asMap("folder", "reknow_users")
            );
            // Lấy ra đường dẫn URL bảo mật (https) của ảnh
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Phát sinh lỗi khi upload ảnh lên Cloudinary", e);
        }
    }
}
