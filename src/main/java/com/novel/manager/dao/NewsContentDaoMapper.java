package com.novel.manager.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novel.core.common.constant.DatabaseConsts;
import com.novel.dao.entity.NewsContent;
import com.novel.dao.mapper.NewsContentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsContentDaoMapper {

    private final NewsContentMapper newsContentMapper;

    public NewsContent getNewsContentByNewsId(long newsId){

        QueryWrapper<NewsContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.NewsContentTable.COLUMN_NEWS_ID,newsId);
        return newsContentMapper.selectOne(queryWrapper);

    }

}
