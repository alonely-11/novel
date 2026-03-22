package com.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novel.core.common.constant.DatabaseConsts;
import com.novel.core.common.resp.RestResp;
import com.novel.dao.entity.NewsContent;
import com.novel.dao.entity.NewsInfo;
import com.novel.dao.mapper.NewsContentMapper;
import com.novel.dao.mapper.NewsInfoMapper;
import com.novel.dto.resp.NewsInfoRespDto;
import com.novel.manager.NewsCacheManager;
import com.novel.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewServiceImpl implements NewsService {

    private final NewsInfoMapper newsInfoMapper;

    private final NewsContentMapper newsContentMapper;

    private final NewsCacheManager newsCacheManager;

    @Override
    public RestResp<NewsInfoRespDto> getNews(Long id) {

        QueryWrapper<NewsInfo> newsInfoQueryWrapper = new QueryWrapper<>();
        newsInfoQueryWrapper.eq(DatabaseConsts.CommonColumnEnum.ID.getName(), id);
        NewsInfo newsInfo = newsInfoMapper.selectOne(newsInfoQueryWrapper);

        QueryWrapper<NewsContent> newsContentQueryWrapper = new QueryWrapper<>();
        newsContentQueryWrapper.eq(DatabaseConsts.NewsContentTable.COLUMN_NEWS_ID,id);
        NewsContent newsContent = newsContentMapper.selectOne(newsContentQueryWrapper);

        return RestResp.ok(NewsInfoRespDto.builder()
                .id(newsInfo.getId())
                .categoryId(newsInfo.getCategoryId())
                .categoryName(newsInfo.getCategoryName())
                .sourceName(newsInfo.getSourceName())
                .title(newsInfo.getTitle())
                .updateTime(newsInfo.getUpdateTime())
                .content(newsContent.getContent())
                .build());
    }

    @Override
    public RestResp<List<NewsInfoRespDto>> listLatestNews() {
        return newsCacheManager.listLatestNews();
    }

}
