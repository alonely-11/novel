package com.novel.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novel.core.constant.CacheConsts;
import com.novel.dao.entity.BookInfo;
import com.novel.dao.entity.HomeBook;
import com.novel.dao.mapper.BookInfoMapper;
import com.novel.dao.mapper.HomeBookMapper;
import com.novel.dto.resp.HomeBookRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 首页推荐小说，缓存管理类
 */
@Component
@RequiredArgsConstructor
public class HomeBookCacheManager {

    //操作“小说推荐”表
    private final HomeBookMapper homeBookMapper;

    //操作"小说信息"表
    private final BookInfoMapper bookInfoMapper;

    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,value = CacheConsts.HOME_BOOK_CACHE_NAME)
    public List<HomeBookRespDto> listHomeBookResp(){

        //1.从小说推荐表中找小说id得到列表
        List<HomeBook> homeBooks = homeBookMapper.selectList(null);
        List<Long> bookIds;
        if (!(homeBooks == null || homeBooks.isEmpty())) {
            bookIds = homeBooks.stream().map(HomeBook::getBookId).toList();

            //2.从小说信息表中找信息
            QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", bookIds);
            List<BookInfo> bookInfos = bookInfoMapper.selectList(queryWrapper);

            if (!CollectionUtils.isEmpty(bookInfos)) {
                Map<Long,BookInfo> bookInfoMap = bookInfos.stream().collect(Collectors.toMap(BookInfo::getId, Function.identity()));
                return homeBooks.stream().map(v->{
                    BookInfo bookInfo = bookInfoMap.get(v.getBookId());
                    HomeBookRespDto homeBookRespDto = new HomeBookRespDto();
                    homeBookRespDto.setType(v.getType());
                    homeBookRespDto.setBookId(bookInfo.getId());
                    homeBookRespDto.setBookName(bookInfo.getBookName());
                    homeBookRespDto.setPicUrl(bookInfo.getPicUrl());
                    homeBookRespDto.setAuthorName(bookInfo.getAuthorName());
                    homeBookRespDto.setBookDesc(bookInfo.getBookDesc());
                    return homeBookRespDto;
                }).toList();

                }
            }
        return Collections.emptyList();
    }
}
