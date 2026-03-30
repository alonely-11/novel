package com.novel.controller.front;

import com.novel.core.common.constant.ApiRouterConsts;
import com.novel.core.common.resp.PageRespDto;
import com.novel.core.common.resp.RestResp;
import com.novel.dto.req.BookSearchReqDto;
import com.novel.dto.resp.BookInfoRespDto;
import com.novel.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(ApiRouterConsts.API_FRONT_SEARCH_URL_PREFIX)
@RestController
public class SearchController {

    private final SearchService searchService;

    @GetMapping("books")
    public RestResp<PageRespDto<BookInfoRespDto>> searchBooks(BookSearchReqDto condition){

        return searchService.searchBooks(condition);

    }

}
