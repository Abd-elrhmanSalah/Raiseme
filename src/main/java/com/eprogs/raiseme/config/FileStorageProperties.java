package com.eprogs.raiseme.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.file")
@Data
public class FileStorageProperties {
    private String uploadDir = "uploads/item";  // default folder
}