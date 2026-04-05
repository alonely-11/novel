package com.novel.controller.author;

import com.novel.core.auth.UserHolder;
import com.novel.core.common.constant.ApiRouterConsts;
import com.novel.core.common.resp.RestResp;
import com.novel.dto.req.AuthorRegisterReqDto;
import com.novel.dto.req.BookAddReqDto;
import com.novel.dto.req.ChapterUpdateReqDto;
import com.novel.dto.resp.ChapterContentRespDto;
import com.novel.service.AuthorService;
import com.novel.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRouterConsts.API_AUTHOR_URL_PREFIX)
public class AuthorController {

    private final BookService bookService;

    private final AuthorService authorService;

    /**
     * 章节查询
     * @param chapterId
     * @return
     */
    @GetMapping("book/chapter/{chapterId}")
    public RestResp<ChapterContentRespDto> getBookChapter(@PathVariable Long chapterId){
        return bookService.getBookChapter(chapterId);
    }

    /**
     * 章节更新
     * @param chapterId
     * @param dto
     * @return
     */
    @PutMapping("book/chapter/{chapterId}")
    public RestResp<Void> updateBookChapter(
            @PathVariable Long chapterId,
            @Valid @RequestBody ChapterUpdateReqDto dto){
        return bookService.updateBookChapter(chapterId,dto);
    }

    /**
     * 作家注册
     * @param dto
     * @return
     */
    @PostMapping("register")
    public RestResp<Void> register(@Valid @RequestBody AuthorRegisterReqDto dto){
        dto.setUserId(UserHolder.getUserId());
        return authorService.register(dto);
    }

    @GetMapping("status")
    public RestResp<Integer> getStatus(){
        return authorService.getStatus(UserHolder.getUserId());
    }

    @DeleteMapping("book/chapter/{chapterId}")
    public RestResp<Void> deleteBookChapter(@PathVariable Long chapterId){
        return bookService.deleteBookChapter(chapterId);
    }

    @PostMapping("book")
    public RestResp<Void> publishBook(@Valid @RequestBody BookAddReqDto dto){
        return bookService.saveBook(dto);
    }

}
