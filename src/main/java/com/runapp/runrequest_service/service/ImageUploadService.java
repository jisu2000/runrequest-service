package com.runapp.runrequest_service.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
        Map<?,?> uploadImage(MultipartFile file);
}
