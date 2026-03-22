package com.novel.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novel.core.common.constant.DatabaseConsts;
import com.novel.core.common.resp.RestResp;
import com.novel.dao.entity.NewsContent;
import com.novel.dao.entity.NewsInfo;
import com.novel.dao.mapper.NewsInfoMapper;
import com.novel.dto.resp.NewsInfoRespDto;
import com.novel.manager.dao.NewsContentDaoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NewsCacheManager {

    private final RedisTemplate redisTemplate;

    private final NewsInfoMapper newsInfoMapper;

    private final NewsContentDaoMapper newsContentDaoMapper;

    public RestResp<List<NewsInfoRespDto>>  listLatestNews(){

        QueryWrapper<NewsInfo> newsInfoQueryWrapper = new QueryWrapper<>();
        newsInfoQueryWrapper.orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName())
                .last(DatabaseConsts.SqlEnum.LIMIT_2.getSql());
        List<NewsInfo> newsInfoList = newsInfoMapper.selectList(newsInfoQueryWrapper);

        Map<Long,NewsContent> map = newsInfoList.stream().collect(
                Collectors.toMap(NewsInfo::getId,newsInfo -> newsContentDaoMapper.getNewsContentByNewsId(newsInfo.getId()))
        );

         return RestResp.ok(newsInfoList.stream().map(v->
                 NewsInfoRespDto.builder()
                 .id(v.getId())
                 .categoryId(v.getCategoryId())
                 .categoryName(v.getCategoryName())
                 .sourceName(v.getSourceName())
                 .title(v.getTitle())
                 .updateTime(v.getUpdateTime())
                 .content(map.get(v.getId()).getContent())
                 .build()).toList());
    }

}
