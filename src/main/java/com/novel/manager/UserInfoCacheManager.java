package com.novel.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novel.core.common.constant.DatabaseConsts;
import com.novel.core.constant.CacheConsts;
import com.novel.dao.entity.UserInfo;
import com.novel.dao.mapper.UserInfoMapper;
import com.novel.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserInfoCacheManager {

    private final UserInfoMapper userInfoMapper;

    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
    value = CacheConsts.USER_INFO_CACHE_NAME)
    public UserInfoDto getUser(Long userId){

        UserInfo userInfo = userInfoMapper.selectById(userId);

        if (Objects.isNull(userInfo)){
            return null;
        }

        return UserInfoDto.builder()
                .id(userInfo.getId())
                .status(userInfo.getStatus())
                .build();

    }

}
