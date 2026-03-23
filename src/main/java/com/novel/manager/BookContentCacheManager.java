package com.novel.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novel.core.common.constant.DatabaseConsts;
import com.novel.core.constant.CacheConsts;
import com.novel.dao.entity.BookContent;
import com.novel.dao.mapper.BookContentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookContentCacheManager {

    private final BookContentMapper bookContentMapper;

    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER
            ,value = CacheConsts.BOOK_CONTENT_CACHE_NAME)
    public String getBookContent(Long chapterId) {

        QueryWrapper<BookContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookContentTable.COLUMN_CHAPTER_ID, chapterId);
        BookContent bookContent = bookContentMapper.selectOne(queryWrapper);
        return bookContent.getContent();

    }
}
