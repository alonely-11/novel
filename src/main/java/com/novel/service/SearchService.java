package com.novel.service;


import com.novel.core.common.resp.PageRespDto;
import com.novel.core.common.resp.RestResp;
import com.novel.dto.req.BookSearchReqDto;
import com.novel.dto.resp.BookInfoRespDto;

public interface SearchService {
    RestResp<PageRespDto<BookInfoRespDto>> searchBooks(BookSearchReqDto condition);
}
