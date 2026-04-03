package com.novel.controller.author;

import com.novel.core.common.constant.ApiRouterConsts;
import com.novel.core.common.resp.RestResp;
import com.novel.dto.req.ChapterUpdateReqDto;
import com.novel.dto.resp.ChapterContentRespDto;
import com.novel.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRouterConsts.API_AUTHOR_URL_PREFIX)
public class AuthorController {

    public final BookService bookService;

    @GetMapping("book/chapter/{chapterId}")
    public RestResp<ChapterContentRespDto> getBookChapter(@PathVariable Long chapterId){
        return bookService.getBookChapter(chapterId);
    }

    @PutMapping("book/chapter/{chapterId}")
    public RestResp<Void> updateBookChapter(
            @PathVariable Long chapterId,
            @Valid @RequestBody ChapterUpdateReqDto dto){
        return bookService.updateBookChapter(chapterId,dto);
    }

}
