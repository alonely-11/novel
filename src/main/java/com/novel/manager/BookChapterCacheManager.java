package com.novel.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novel.core.common.constant.DatabaseConsts;
import com.novel.core.constant.CacheConsts;
import com.novel.dao.entity.BookChapter;
import com.novel.dao.mapper.BookChapterMapper;
import com.novel.dto.resp.BookChapterRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookChapterCacheManager {

    private final BookChapterMapper bookChapterMapper;

    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER
            ,value = CacheConsts.BOOK_CHAPTER_CACHE_NAME)
    public BookChapterRespDto getChapter(Long chapterId) {

        BookChapter bookChapter = bookChapterMapper.selectById(chapterId);

        return BookChapterRespDto.builder()
                .id(bookChapter.getId())
                .bookId(bookChapter.getBookId())
                .chapterNum(bookChapter.getChapterNum())
                .chapterName(bookChapter.getChapterName())
                .chapterWordCount(bookChapter.getWordCount())
                .chapterUpdateTime(bookChapter.getUpdateTime())
                .isVip(bookChapter.getIsVip())
                .build();

    }

    @CacheEvict(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
    value = CacheConsts.BOOK_CHAPTER_CACHE_NAME)
    public void evictBookChapterCache(Long chapterId) {
        //调用自动清除缓存
    }

}
