package com.novel.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "novel.qiniu")
@Data
public class QiniuKodoProperties {

    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String referringDomain;

}
