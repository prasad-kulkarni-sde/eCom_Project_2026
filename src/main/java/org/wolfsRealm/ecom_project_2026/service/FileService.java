package org.wolfsRealm.ecom_project_2026.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    public String updateImage(String path, MultipartFile file) throws IOException;
}
