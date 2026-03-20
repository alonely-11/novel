package com.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novel.core.common.constant.DatabaseConsts;
import com.novel.core.common.constant.ErrorCodeEnum;
import com.novel.core.common.constant.SystemConfigConsts;
import com.novel.core.common.exception.BusinessException;
import com.novel.core.common.resp.RestResp;
import com.novel.core.util.JwtUtils;
import com.novel.dao.entity.UserInfo;
import com.novel.dao.mapper.UserInfoMapper;
import com.novel.dto.req.UserLoginReqDto;
import com.novel.dto.req.UserRegisterReqDto;
import com.novel.dto.resp.UserLoginRespDto;
import com.novel.dto.resp.UserRegisterRespDto;
import com.novel.manager.VerifyCodeManager;
import com.novel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final VerifyCodeManager verifyCodeManager;

    private final UserInfoMapper userInfoMapper;

    private final JwtUtils jwtUtils;

    @Override
    public RestResp<UserRegisterRespDto> register(UserRegisterReqDto userRegisterReqDto) {
        // 校验图形验证码是否正确
        if (!verifyCodeManager.imgVerifyCodeOk(userRegisterReqDto.getSessionId(), userRegisterReqDto.getVelCode())) {
            // 图形验证码校验失败
            throw new BusinessException(ErrorCodeEnum.USER_VERIFY_CODE_ERROR);
        }

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_USERNAME, userRegisterReqDto.getUsername())
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        if(userInfoMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException(ErrorCodeEnum.USER_NAME_EXIST);
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userRegisterReqDto.getUsername());
        userInfo.setPassword(DigestUtils.md5DigestAsHex(
                userRegisterReqDto.getPassword().getBytes(StandardCharsets.UTF_8)
        ));
        userInfo.setNickName(userRegisterReqDto.getUsername());
        userInfo.setCreateTime(LocalDateTime.now());
        userInfo.setUpdateTime(LocalDateTime.now());
        userInfo.setSalt("0");
        userInfoMapper.insert(userInfo);

        verifyCodeManager.removeImgVerifyCode(userRegisterReqDto.getSessionId());

        return RestResp.ok(
                UserRegisterRespDto.builder()
                        .token(jwtUtils.generateToken(userInfo.getId(), SystemConfigConsts.NOVEL_FRONT_KEY))
                        .uid(userInfo.getId())
                        .build()
        );
    }

    @Override
    public RestResp<UserLoginRespDto> login(UserLoginReqDto dto) {

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_USERNAME, dto.getUsername())
//                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());

        queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_USERNAME, dto.getUsername());

        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);

        if (userInfo == null) {throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);}

        if (!(userInfo.getPassword().equals(DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8))))) {
            throw new BusinessException(ErrorCodeEnum.USER_PASSWORD_ERROR);
        }

//        if(userInfoMapper.selectCount(queryWrapper) < 1) {
//            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
//        }

//        queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_PASSWORD, DigestUtils.md5DigestAsHex(
//                dto.getPassword().getBytes(StandardCharsets.UTF_8)));

//        if(userInfoMapper.selectCount(queryWrapper) < 1) {
//            throw new BusinessException(ErrorCodeEnum.USER_PASSWORD_ERROR);
//        }

        return RestResp.ok(
                UserLoginRespDto.builder()
                        .uid(userInfo.getId())
                        .nickname(userInfo.getNickName())
                        .token(jwtUtils.generateToken(userInfo.getId(),SystemConfigConsts.NOVEL_FRONT_KEY))
                        .build()
        );
    }
}
