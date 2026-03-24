package com.novel.core.auth;

import lombok.Data;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户信息 持有类
 *
 * @author xiongxiaoyang
 * @date 2022/5/18
 */
@Slf4j
@UtilityClass
@Data
public class UserHolder {

    /**
     * 当前线程用户ID
     */
    private static final ThreadLocal<Long> userIdTL = new ThreadLocal<>();

    /**
     * 当前线程作家ID
     */
    private static final ThreadLocal<Long> authorIdTL = new ThreadLocal<>();

    public void setUserId(Long userId) {
        log.info("==============用户id:{}======================", userId);
        userIdTL.set(userId);
    }

    public Long getUserId() {
        return userIdTL.get();
    }

    public void setAuthorId(Long authorId) {
        authorIdTL.set(authorId);
    }

    public Long getAuthorId() {
        return authorIdTL.get();
    }

    public void clear() {
        userIdTL.remove();
        authorIdTL.remove();
    }

}
