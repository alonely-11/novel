package com.novel.manager;

import com.novel.core.constant.CacheConsts;
import com.novel.core.util.ImgVerifyCodeUtils;
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
public class VerfyCodeManager {

    private final StringRedisTemplate stringRedisTemplate;

    public String genImgVerifyCode(String seesionId) throws IOException {
        String verifyCode = ImgVerifyCodeUtils.getRandomVerifyCode(4);
        String img = ImgVerifyCodeUtils.generateVerifyCodeImg(verifyCode);
        stringRedisTemplate.opsForValue().set(CacheConsts.IMG_VERIFY_CODE_CACHE_KEY + seesionId
                ,verifyCode, Duration.ofMinutes(5));
        return img;
    }

    public boolean imgVerifyCodeOK(String seesionId,String verifyCode) throws IOException {
        return Objects.equals(
                verifyCode.equals(stringRedisTemplate.opsForValue().get(CacheConsts.IMG_VERIFY_CODE_CACHE_KEY+seesionId)), verifyCode);
    }

    public void removeImgVerifyCode(String seesionId,String verifyCode) throws IOException {
        stringRedisTemplate.delete(CacheConsts.IMG_VERIFY_CODE_CACHE_KEY + seesionId);
    }
}
