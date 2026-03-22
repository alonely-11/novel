package com.novel.controller.front;

import com.novel.core.common.constant.ApiRouterConsts;
import com.novel.core.common.resp.RestResp;
import com.novel.dto.resp.BookCommentRespDto;
import com.novel.dto.resp.BookRankRespDto;
import com.novel.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_BOOK_URL_PREFIX)
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("comment/newest_list")
    public RestResp<BookCommentRespDto> listNewestComments(Long bookId){
        return bookService.listNewestComments(bookId);
    }

    @GetMapping("visit_rank")
    public RestResp<List<BookRankRespDto>> listVisitRankBooks(){
        return bookService.listVisitRankBooks();
    }

}
