package com.novel.service;

import com.novel.core.common.resp.RestResp;
import com.novel.dto.resp.NewsInfoRespDto;

import java.util.List;

public interface NewsService {

    RestResp<NewsInfoRespDto> getNews(Long id);

    RestResp<List<NewsInfoRespDto>> listLatestNews();
}
