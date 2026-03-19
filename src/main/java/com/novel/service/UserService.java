package com.novel.service;

import com.novel.core.common.resp.RestResp;
import com.novel.dto.req.UserRegisterReqDto;
import jakarta.validation.Valid;

public interface UserService {

    /**
     * 用户注册
     * @param userRegisterReqDto 注册参数
     * @return JWT
     */
    RestResp<UserRegisterReqDto> register(@Valid UserRegisterReqDto userRegisterReqDto);
}
