package com.novel.manager;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novel.core.common.constant.DatabaseConsts;
import com.novel.core.constant.CacheConsts;
import com.novel.dao.entity.AuthorInfo;
import com.novel.dao.mapper.AuthorInfoMapper;
import com.novel.dto.AuthorInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthorInfoCacheManager {

    private final AuthorInfoMapper authorInfoMapper;

    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.AUTHOR_INFO_CACHE_NAME)
    public AuthorInfoDto getAuthor(Long userId){

        QueryWrapper<AuthorInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.AuthorInfoTable.COLUMN_USER_ID, userId);
        AuthorInfo authorInfo = authorInfoMapper.selectOne(queryWrapper);

        if (Objects.isNull(authorInfo)){
            return null;
        }

        return AuthorInfoDto.builder()
                .id(authorInfo.getId())
                .penName(authorInfo.getPenName())
                .status(authorInfo.getStatus())
                .build();

    }

    @CacheEvict(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.AUTHOR_INFO_CACHE_NAME)
    public void exictAuthorCache(){
    }

}
