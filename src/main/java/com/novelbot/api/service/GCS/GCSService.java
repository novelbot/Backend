package com.novelbot.api.service.GCS;

import com.google.cloud.storage.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class GCSService {

    @Value("${gcp.storage.bucket}")
    private String bucket;

    @Value("${gcp.storage.folder}")
    private String folderName;

    private final Storage storage;

    public GCSService(@Autowired(required = false) Storage storage) {
        this.storage = storage;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String objectName = String.format("%s/%s", folderName, file.getOriginalFilename());

        // 로컬 환경이거나 Storage가 null인 경우 Mock URL 반환
        if ("local-test-bucket".equals(bucket) || storage == null) {
            // 로컬 테스트용 - 실제 파일 업로드 없이 Mock URL 반환
            return String.format("https://mock-storage.local/%s/%s", bucket, objectName);
        }

        BlobId blobId = BlobId.of(bucket, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());

        // Public URL 생성
        return String.format("https://storage.googleapis.com/%s/%s", bucket, objectName);
    }
}
