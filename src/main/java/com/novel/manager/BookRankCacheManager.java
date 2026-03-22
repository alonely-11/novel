package com.novel.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novel.core.common.constant.DatabaseConsts;
import com.novel.core.constant.CacheConsts;
import com.novel.dao.entity.BookInfo;
import com.novel.dao.mapper.BookInfoMapper;
import com.novel.dto.resp.BookRankRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookRankCacheManager {

    private final BookInfoMapper bookInfoMapper;

    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
    value = CacheConsts.BOOK_VISIT_RANK_CACHE_NAME)
    public List<BookRankRespDto> listVisitRankBooks() {

        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DatabaseConsts.BookTable.COLUMN_VISIT_COUNT);
        return listRankBooks(queryWrapper);
    }

    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
    value = CacheConsts.BOOK_NEWEST_RANK_CACHE_NAME)
    public List<BookRankRespDto> listNewestRankBookds(){

        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName())
                .gt(DatabaseConsts.BookTable.COLUMN_WORD_COUNT,0);;
        return listRankBooks(queryWrapper);

    }

    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
    value = CacheConsts.BOOK_UPDATE_RANK_CACHE_NAME )
    public List<BookRankRespDto> listUpdateRankBooks(){

        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DatabaseConsts.CommonColumnEnum.UPDATE_TIME.getName())
                .gt(DatabaseConsts.BookTable.COLUMN_WORD_COUNT,0);
        return listRankBooks(queryWrapper);
    }



    private List<BookRankRespDto> listRankBooks(QueryWrapper<BookInfo> queryWrapper){

        queryWrapper.last(DatabaseConsts.SqlEnum.LIMIT_30.getSql());

        List<BookInfo> bookInfos = bookInfoMapper.selectList(queryWrapper);

        return bookInfos.stream().map(v->
        BookRankRespDto.builder()
                        .id(v.getId())
        .categoryId(v.getCategoryId())
        .categoryName(v.getCategoryName())
        .picUrl(v.getPicUrl())
        .bookName(v.getBookName())
        .authorName(v.getAuthorName())
        .bookDesc(v.getBookDesc())
        .wordCount(v.getWordCount())
        .lastChapterName(v.getLastChapterName())
        .lastChapterUpdateTime(v.getLastChapterUpdateTime())
        .build()
                ).toList();
    }
}
