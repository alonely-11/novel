package com.novel.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novel.core.common.constant.DatabaseConsts;
import com.novel.core.constant.CacheConsts;
import com.novel.dao.entity.BookChapter;
import com.novel.dao.entity.BookInfo;
import com.novel.dao.mapper.BookChapterMapper;
import com.novel.dao.mapper.BookInfoMapper;
import com.novel.dto.resp.BookInfoRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookInfoCacheManager {

    private final BookInfoMapper bookInfoMapper;

    private final BookChapterMapper bookChapterMapper;

    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public BookInfoRespDto getBookInfo(Long bookId) {
        return cachePutBookInfo(bookId);
    }

    @CachePut(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
    value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public BookInfoRespDto cachePutBookInfo(Long bookId) {

        QueryWrapper<BookInfo> bookInfoQueryWrapper = new QueryWrapper<>();
        bookInfoQueryWrapper.eq(DatabaseConsts.CommonColumnEnum.ID.getName(), bookId);

        BookInfo bookInfo = bookInfoMapper.selectOne(bookInfoQueryWrapper);

        QueryWrapper<BookChapter> bookChapterQueryWrapper = new QueryWrapper<>();

        bookChapterQueryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
                .orderByAsc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());

        BookChapter bookChapter = bookChapterMapper.selectOne(bookChapterQueryWrapper);

        return BookInfoRespDto.builder()
                .id(bookId)
                .categoryId(bookInfo.getCategoryId())
                .categoryName(bookInfo.getCategoryName())
                .picUrl(bookInfo.getPicUrl())
                .bookName(bookInfo.getBookName())
                .authorId(bookInfo.getAuthorId())
                .authorName(bookInfo.getAuthorName())
                .bookDesc(bookInfo.getBookDesc())
                .bookStatus(bookInfo.getBookStatus())
                .visitCount(bookInfo.getVisitCount())
                .wordCount(bookInfo.getWordCount())
                .commentCount(bookInfo.getCommentCount())
                .firstChapterId(bookChapter.getId())
                .lastChapterId(bookInfo.getLastChapterId())
                .lastChapterName(bookInfo.getLastChapterName())
                .updateTime(bookInfo.getUpdateTime())
                .build();
    }


    @CacheEvict(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.BOOK_INFO_CACHE_NAME)
    public void evictBookInfoCache(Long bookId) {
        //调用此方法自动清除小说信息缓存
    }

    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
    value = CacheConsts.LAST_UPDATE_BOOK_ID_LIST_CACHE_NAME)
    public List<Long> getLastUpdateIdList(Long categoryId) {

        QueryWrapper<BookInfo> bookInfoQueryWrapper = new QueryWrapper<>();
        bookInfoQueryWrapper.eq(DatabaseConsts.BookTable.COLUMN_CATEGORY_ID, categoryId)
                .gt(DatabaseConsts.BookTable.COLUMN_WORD_COUNT,0)
                .orderByDesc(DatabaseConsts.CommonColumnEnum.UPDATE_TIME.getName())
                .last(DatabaseConsts.SqlEnum.LIMIT_500.getSql());

        List<BookInfo> bookInfoList = bookInfoMapper.selectList(bookInfoQueryWrapper);

        return bookInfoList.stream().map(BookInfo::getId).toList();

    }
}
