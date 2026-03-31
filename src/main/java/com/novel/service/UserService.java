package com.novel.service;

import com.novel.core.common.resp.RestResp;
import com.novel.dto.req.UserLoginReqDto;
import com.novel.dto.req.UserRegisterReqDto;
import com.novel.dto.resp.UserInfoRespDto;
import com.novel.dto.req.UserInfoUptReqDto;
import com.novel.dto.resp.UserLoginRespDto;
import com.novel.dto.resp.UserRegisterRespDto;


public interface UserService {

    /**
     * 用户注册
     * @param dto 注册参数
     * @return JWT
     */
    RestResp<UserRegisterRespDto> register( UserRegisterReqDto dto);

    /**
     * 用户登录
     *
     * @param dto 登录参数
     * @return JWT + 昵称
     */
    RestResp<UserLoginRespDto> login( UserLoginReqDto dto);

    RestResp<UserInfoRespDto> getUserInfo(Long userId);

    RestResp<Void> updateUserInfo(UserInfoUptReqDto dto);
}
