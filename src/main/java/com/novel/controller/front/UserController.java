package com.novel.controller.front;

import com.novel.core.auth.UserHolder;
import com.novel.core.common.constant.ApiRouterConsts;
import com.novel.core.common.req.PageReqDto;
import com.novel.core.common.resp.PageRespDto;
import com.novel.core.common.resp.RestResp;
import com.novel.dto.req.UserCommentReqDto;
import com.novel.dto.req.UserInfoUptReqDto;
import com.novel.dto.req.UserLoginReqDto;
import com.novel.dto.req.UserRegisterReqDto;
import com.novel.dto.resp.UserCommentRespDto;
import com.novel.dto.resp.UserInfoRespDto;
import com.novel.dto.resp.UserLoginRespDto;
import com.novel.dto.resp.UserRegisterRespDto;
import com.novel.service.BookService;
import com.novel.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_USER_URL_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    private final BookService bookService;

    /**
     * 注册
     * @param dto
     * @return
     */
    @PostMapping("register")
    public RestResp<UserRegisterRespDto> register(@Valid @RequestBody UserRegisterReqDto dto) {
        return userService.register(dto);
    }

    /**
     * 登录
     * @param dto
     * @return
     */
    @PostMapping("login")
    public RestResp<UserLoginRespDto> login(@Valid @RequestBody UserLoginReqDto dto) {
        return userService.login(dto);
    }

    /**
     * 评论
     * @param dto
     * @return
     */
    @PostMapping("comment")
    public RestResp<Void> comment(@Valid @RequestBody UserCommentReqDto dto){
        dto.setUserId(UserHolder.getUserId());
        return bookService.saveComment(dto);
    }

    /**
     * 修改评论
     * @param id 评论id
     * @param content
     * @return
     */
    @PutMapping("comment/{id}")
    public RestResp<Void> updateComment(@PathVariable Long id, String content){
        return bookService.updateComment(UserHolder.getUserId(),id,content);
    }

    /**
     * 删除评论
     * @param id 评论id
     * @return
     */
    @DeleteMapping("comment/{id}")
    public RestResp<Void> deleteComment(@PathVariable Long id){
        return bookService.deleteComment(UserHolder.getUserId(),id);
    }

    @GetMapping
    public RestResp<UserInfoRespDto> getUserInfo(){
        return userService.getUserInfo(UserHolder.getUserId());
    }

    @PutMapping
    public RestResp<Void> updateUserInfo(@Valid @RequestBody UserInfoUptReqDto dto) {
        dto.setUserId(UserHolder.getUserId());
        return userService.updateUserInfo(dto);
    }

    @GetMapping("bookshelf_status")
    public RestResp<Integer> getBookshelfStatus(String bookId){
        return bookService.getBookshelfStatus(bookId,UserHolder.getUserId());
    }

    @GetMapping("comments")
    public RestResp<PageRespDto<UserCommentRespDto>> listComments(PageReqDto pageReqDto){
        return bookService.listComments(UserHolder.getUserId(),pageReqDto);
    }

    @PostMapping("feedback")
    public RestResp<Void> submitFeedback(@RequestBody String content){
        return userService.saveFeedback(UserHolder.getUserId(),content);
    }

    @DeleteMapping("feedback/{id}")
    public RestResp<Void> deleteFeedback(@PathVariable Long id){
        return userService.deleteFeedback(UserHolder.getUserId(),id);
    }

}
