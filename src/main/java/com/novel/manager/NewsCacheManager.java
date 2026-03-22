package com.novel.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novel.core.common.constant.DatabaseConsts;
import com.novel.core.common.resp.RestResp;
import com.novel.core.constant.CacheConsts;
import com.novel.dao.entity.NewsContent;
import com.novel.dao.entity.NewsInfo;
import com.novel.dao.mapper.NewsInfoMapper;
import com.novel.dto.resp.NewsInfoRespDto;
import com.novel.manager.dao.NewsContentDaoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NewsCacheManager {

    private final NewsInfoMapper newsInfoMapper;

    private final NewsContentDaoMapper newsContentDaoMapper;

    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
    value = CacheConsts.LATEST_NEWS_CACHE_NAME)
    public List<NewsInfoRespDto>  listLatestNews(){

        QueryWrapper<NewsInfo> newsInfoQueryWrapper = new QueryWrapper<>();
        newsInfoQueryWrapper.orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName())
                .last(DatabaseConsts.SqlEnum.LIMIT_2.getSql());
        List<NewsInfo> newsInfoList = newsInfoMapper.selectList(newsInfoQueryWrapper);

        Map<Long,NewsContent> map = newsInfoList.stream().collect(
                Collectors.toMap(NewsInfo::getId,newsInfo -> newsContentDaoMapper.getNewsContentByNewsId(newsInfo.getId()))
        );

         return newsInfoList.stream().map(v->
                 NewsInfoRespDto.builder()
                 .id(v.getId())
                 .categoryId(v.getCategoryId())
                 .categoryName(v.getCategoryName())
                 .sourceName(v.getSourceName())
                 .title(v.getTitle())
                 .updateTime(v.getUpdateTime())
                 .content(map.get(v.getId()).getContent())
                 .build()).toList();
    }

}
