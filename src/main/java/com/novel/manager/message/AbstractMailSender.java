package com.novel.manager.message;

import com.novel.core.config.MailProperties;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 抽象的邮件消息发送者
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractMailSender extends AbstractMessageSender{

    private final MailProperties mailProperties;

    private final JavaMailSender mailSender;

    @Override
    protected void sendMessage(Long toUserId, String messageTitle, String messageContent) {

        String toEmail = "L6060909@163.com";

        log.info("发送 HTML 邮件开始：{}，{}，{}",toEmail,messageTitle,messageContent);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(message,true);
            helper.setFrom(new InternetAddress(mailProperties.username(),mailProperties.nickname(),"UTF-8"));
            helper.setTo(toEmail);
            helper.setSubject(messageTitle);
            // 第二个参数true代表支持html
            helper.setText(messageContent,true);
            mailSender.send(message);
            log.info("发送 HTML 邮件 to {} 成功", toEmail);
        }catch (Exception e){
            log.info("发送 HTML 邮件 to {} 失败", toEmail,e);
        }
    }
}
