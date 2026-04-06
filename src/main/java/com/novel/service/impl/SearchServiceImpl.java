package com.novel.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novel.core.common.resp.PageRespDto;
import com.novel.core.common.resp.RestResp;
import com.novel.dao.entity.BookInfo;
import com.novel.dao.mapper.BookInfoMapper;
import com.novel.dto.req.BookSearchReqDto;
import com.novel.dto.resp.BookInfoRespDto;
import com.novel.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final BookInfoMapper bookInfoMapper;

    @Override
    public RestResp<PageRespDto<BookInfoRespDto>> searchBooks(BookSearchReqDto condition) {

        IPage<BookInfo> page = new Page<>();
        page.setCurrent(condition.getPageNum());
        page.setSize(condition.getPageSize());

        IPage<BookInfo> bookInfoIPage = bookInfoMapper.searchBooks(page,condition);

        return RestResp.ok(PageRespDto.of(
                page.getCurrent(),
                page.getSize(),
                page.getTotal(),
                bookInfoIPage.getRecords().stream().map(v->
                                BookInfoRespDto.builder()
                                        .id(v.getId())
                                        .bookName(v.getBookName())
                                        .categoryId(v.getCategoryId())
                                        .categoryName(v.getCategoryName())
                                        .authorId(v.getAuthorId())
                                        .authorName(v.getAuthorName())
                                        .wordCount(v.getWordCount())
                                        .lastChapterName(v.getLastChapterName())
                                        .build()
                        ).toList()
        ));
    }
}
