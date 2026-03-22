package com.novel.service.impl;

import com.novel.core.common.resp.RestResp;
import com.novel.dao.mapper.HomeFriendLinkMapper;
import com.novel.dto.resp.HomeBookRespDto;
import com.novel.dto.resp.HomeFriendLinkRespDto;
import com.novel.manager.HomeBookCacheManager;
import com.novel.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final HomeBookCacheManager  homeBookCacheManager;

    private final HomeFriendLinkMapper homeFriendLinkMapper;

    @Override
    public RestResp<List<HomeBookRespDto>> listHomeBooks() {
        return RestResp.ok(homeBookCacheManager.listHomeBookResp());
    }

    @Override
    public RestResp<List<HomeFriendLinkRespDto>> listHomeFriendLinks() {

        return RestResp.ok(homeFriendLinkMapper.selectList(null).stream().map(v->
                HomeFriendLinkRespDto.builder()
                        .linkName(v.getLinkName())
                        .linkUrl(v.getLinkUrl())
                        .build()
        ).toList());
    }
}
