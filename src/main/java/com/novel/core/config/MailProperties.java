package com.novel.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "novel.mail")
public record MailProperties(String nickname, String username) {
}
