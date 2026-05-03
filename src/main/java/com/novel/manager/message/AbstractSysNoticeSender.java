package com.novel.manager.message;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 抽象的系统通知发送者
 */
@Slf4j
public abstract class AbstractSysNoticeSender extends AbstractMessageSender{

    @Override
    protected void sendMessage(Long toUserId, String messageTitle, String messageContent) {
        LocalDateTime messageDateTime = LocalDateTime.now();
        log.info("系统通知发送成功，{},{},{},{}",toUserId,messageDateTime.format(DateTimeFormatter.ISO_DATE_TIME),
                messageTitle,messageContent);
    }
}
