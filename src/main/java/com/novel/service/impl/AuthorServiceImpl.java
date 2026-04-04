package com.novel.service.impl;

import com.novel.core.common.resp.RestResp;
import com.novel.dao.entity.AuthorInfo;
import com.novel.dao.mapper.AuthorInfoMapper;
import com.novel.dto.AuthorInfoDto;
import com.novel.dto.req.AuthorRegisterReqDto;
import com.novel.manager.AuthorInfoCacheManager;
import com.novel.service.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorInfoCacheManager authorInfoCacheManager;

    private final AuthorInfoMapper authorInfoMapper;

    @Override
    public RestResp<Void> register(AuthorRegisterReqDto dto) {

        AuthorInfoDto authorInfoDto = authorInfoCacheManager.getAuthor(dto.getUserId());
        if (Objects.nonNull(authorInfoDto)) {
            return RestResp.ok();
        }

        AuthorInfo authorInfo = new AuthorInfo();
        authorInfo.setUserId(dto.getUserId());
        authorInfo.setInviteCode("0");
        authorInfo.setPenName(dto.getPenName());
        authorInfo.setTelPhone(dto.getTelPhone());
        authorInfo.setChatAccount(dto.getChatAccount());
        authorInfo.setEmail(dto.getEmail());
        authorInfo.setWorkDirection(dto.getWorkDirection());
        authorInfo.setCreateTime(LocalDateTime.now());
        authorInfo.setUpdateTime(LocalDateTime.now());
        authorInfoMapper.insert(authorInfo);

        authorInfoCacheManager.exictAuthorCache();

        return RestResp.ok();
    }

    @Override
    public RestResp<Integer> getStatus(Long userId) {
        AuthorInfoDto authorInfoDto = authorInfoCacheManager.getAuthor(userId);
        return Objects.isNull(authorInfoDto) ?RestResp.ok(null) : RestResp.ok(authorInfoDto.getStatus());
    }
}
