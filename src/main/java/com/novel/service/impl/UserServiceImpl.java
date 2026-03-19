package com.novel.service.impl;

import com.novel.core.common.resp.RestResp;
import com.novel.core.constant.CacheConsts;
import com.novel.dto.req.UserRegisterReqDto;
import com.novel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public RestResp<UserRegisterReqDto> register(UserRegisterReqDto userRegisterReqDto) {
        if (userRegisterReqDto.getVelCode()
                .equals(stringRedisTemplate.opsForValue()
                        .get(CacheConsts.IMG_VERIFY_CODE_CACHE_KEY + userRegisterReqDto.getSessionId()))){

        }
        return null;
    }
}
