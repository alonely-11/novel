package com.novel.controller.front;

import com.novel.core.common.constant.ApiRouterConsts;
import com.novel.core.common.resp.RestResp;
import com.novel.dao.entity.BookCategory;
import com.novel.dto.resp.*;
import com.novel.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("visit")
    public RestResp<Void> addVisitCount(Long bookId){
        return bookService.addVisitCount(bookId);
    }

    @GetMapping("pre_chapter_id/{chapterId}")
    public RestResp<Long> getPreChapterId(@PathVariable Long chapterId){
        return bookService.getPreChapterId(chapterId);
    }

    @GetMapping("next_chapter_id/{chapterId}")
    public RestResp<Long> getNextChapterId(@PathVariable Long chapterId){
        return bookService.getNextChapterId(chapterId);
    }

    @GetMapping("chapter/list")
    public RestResp<List<BookChapterRespDto>> listChapters(Long bookId){
        return bookService.listChapters(bookId);
    }

    @GetMapping("category/list")
    public RestResp<List<BookCategoryRespDto>> listCategory(Integer workDirection){
        return bookService.listCategory(workDirection);
    }

    @GetMapping("content/{chapterId}")
    public RestResp<BookChapterContentRespDto> getChapterContentAbout(@PathVariable Long chapterId){
        return bookService.getChapterContentAbout(chapterId);
    }

}
