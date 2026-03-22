package com.novel.controller.front;

import com.novel.core.auth.UserHolder;
import com.novel.core.common.constant.ApiRouterConsts;
import com.novel.core.common.resp.RestResp;
import com.novel.dto.req.UserCommentReqDto;
import com.novel.dto.req.UserLoginReqDto;
import com.novel.dto.req.UserRegisterReqDto;
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

}
