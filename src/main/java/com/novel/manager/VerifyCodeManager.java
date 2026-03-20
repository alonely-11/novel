package com.novel.manager;

import com.novel.core.constant.CacheConsts;
import com.novel.core.common.util.ImgVerifyCodeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class VerifyCodeManager {

    private final StringRedisTemplate stringRedisTemplate;

    public String getImgVerifyCode(String seesionId) throws IOException {
        String verifyCode = ImgVerifyCodeUtils.getRandomVerifyCode(4);
        log.info("生成验证码："+verifyCode);
        String img = ImgVerifyCodeUtils.generateVerifyCodeImg(verifyCode);
        stringRedisTemplate.opsForValue().set(CacheConsts.IMG_VERIFY_CODE_CACHE_KEY + seesionId
                ,verifyCode, Duration.ofMinutes(5));
        return img;
    }

    public boolean imgVerifyCodeOk(String sessionId,String verifyCode) {
        return Objects.equals(stringRedisTemplate.opsForValue()
                .get(CacheConsts.IMG_VERIFY_CODE_CACHE_KEY+sessionId), verifyCode);
    }

    public void removeImgVerifyCode(String seesionId){
        stringRedisTemplate.delete(CacheConsts.IMG_VERIFY_CODE_CACHE_KEY + seesionId);
    }
}
