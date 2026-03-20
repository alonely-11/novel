package com.novel.service;

import com.novel.core.common.resp.RestResp;
import com.novel.dto.req.UserRegisterReqDto;
import com.novel.dto.resp.UserRegisterRespDto;
import jakarta.validation.Valid;


public interface UserService {

    /**
     * 用户注册
     * @param dto 注册参数
     * @return JWT
     */
    RestResp<UserRegisterRespDto> register(@Valid UserRegisterReqDto dto);
}
