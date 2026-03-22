package com.novel.service;

import com.novel.core.common.resp.RestResp;
import com.novel.dto.resp.HomeBookRespDto;
import com.novel.dto.resp.HomeFriendLinkRespDto;

import java.util.List;

public interface HomeService {
    RestResp<List<HomeBookRespDto>> listHomeBooks();

    RestResp<List<HomeFriendLinkRespDto>> listHomeFriendLinks();
}
