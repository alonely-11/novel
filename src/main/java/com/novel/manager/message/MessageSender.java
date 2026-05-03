package com.novel.manager.message;

import java.util.Objects;

/**
 * 消息发送器接口，用来发送各种消息
 */
public interface MessageSender {

    /**
     * 发送消息，支持动态消息标题和动态消息内容
     * @param toUserId
     * @param args
     */
    void sendMessage(Long toUserId, Object... args);

}
