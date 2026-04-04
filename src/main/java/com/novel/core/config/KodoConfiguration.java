package com.novel.core.config;

import com.novel.core.common.util.QiniuOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class KodoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public QiniuOssUtil qiniuOssUtil(QiniuKodoProperties qiniuKodoProperties) {
        log.info("开始上传七牛云文件上传：{}", qiniuKodoProperties);
        return new QiniuOssUtil(
                qiniuKodoProperties.getAccessKey(),
                qiniuKodoProperties.getSecretKey(),
                qiniuKodoProperties.getBucketName(),
                qiniuKodoProperties.getReferringDomain());
    }

}
