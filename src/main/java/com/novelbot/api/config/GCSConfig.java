package com.novelbot.api.config;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GCSConfig {
    @Bean
    public Storage storage() {
        try {
            return StorageOptions.getDefaultInstance().getService();
        } catch (Exception e) {
            // 로컬에서 GCS 연결 실패 시 null 반환
            return null;
        }
    }
}
