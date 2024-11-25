package com.runapp.runrequest_service.service.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.runapp.runrequest_service.service.ImageUploadService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageUploaderServiceImpl implements ImageUploadService{


    private final Cloudinary cloudinary;

    @Override
    public Map<?, ?> uploadImage(MultipartFile file) {



        Map<?, ?> uploadedImageMap = null;
        try {
            uploadedImageMap = cloudinary.uploader().upload(file.getBytes(), Map.of("folder","run"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return uploadedImageMap;
    }
    
}
