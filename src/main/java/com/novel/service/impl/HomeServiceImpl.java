package com.novel.service.impl;

import com.novel.core.common.resp.RestResp;
import com.novel.dto.resp.HomeBookRespDto;
import com.novel.manager.HomeBookCacheManager;
import com.novel.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final HomeBookCacheManager  homeBookCacheManager;

    @Override
    public RestResp<List<HomeBookRespDto>> listHomeBooks() {
        return RestResp.ok(homeBookCacheManager.listHomeBookResp());
    }
}
