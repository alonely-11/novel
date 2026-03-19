package com.novel.controller.front;

import com.novel.core.common.constant.ApiRouterConsts;
import com.novel.core.common.resp.RestResp;
import com.novel.dto.req.UserRegisterReqDto;
import com.novel.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_USER_URL_PREFIX)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("register")
    public RestResp<UserRegisterReqDto> register(@Valid @RequestBody UserRegisterReqDto userRegisterReqDto) {
        return userService.register(userRegisterReqDto);
    }
}
