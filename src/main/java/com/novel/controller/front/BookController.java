package com.novel.controller.front;

import com.novel.core.common.constant.ApiRouterConsts;
import com.novel.core.common.resp.RestResp;
import com.novel.dto.resp.BookChapterAboutRespDto;
import com.novel.dto.resp.BookCommentRespDto;
import com.novel.dto.resp.BookInfoRespDto;
import com.novel.dto.resp.BookRankRespDto;
import com.novel.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
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

    @GetMapping("newest_rank")
    public RestResp<List<BookRankRespDto>> listNewestRankBooks(){
        return bookService.listNewestRankBooks();
    }

    @GetMapping("update_rank")
    public RestResp<List<BookRankRespDto>> listUpdateRankBooks(){
        return bookService.listUpdateRankBooks();
    }

    @GetMapping("{id}")
    public RestResp<BookInfoRespDto> getBookById(@PathVariable("id") Long bookId){
        return bookService.getBookById(bookId);
    }

    @GetMapping("last_chapter/about")
    public RestResp<BookChapterAboutRespDto> getLastChapterAbout(Long bookId){
        return bookService.getLastChapterAbout(bookId);
    }

    @GetMapping("rec_list")
    public RestResp<List<BookInfoRespDto>> listRecBooks(Long bookId) throws NoSuchAlgorithmException {
        return bookService.listRecBooks(bookId);
    }

}
