package com.novel.core.auth;

import com.novel.core.common.constant.ErrorCodeEnum;
import com.novel.core.common.constant.SystemConfigConsts;
import com.novel.core.common.exception.BusinessException;
import com.novel.core.util.JwtUtils;
import com.novel.dto.UserInfoDto;
import com.novel.manager.UserInfoCacheManager;
import org.springframework.util.StringUtils;

import java.util.Objects;

public interface AuthStrategy {

    void auth(String token, String requestUri) throws BusinessException;

    default Long authSSO(JwtUtils jwtUtils, UserInfoCacheManager userInfoCacheManager, String token){

        if (!StringUtils.hasText(token)){
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }

        Long userId = jwtUtils.parseToken(token, SystemConfigConsts.NOVEL_FRONT_KEY);
        if (Objects.isNull(userId)){
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }

        UserInfoDto userInfo = userInfoCacheManager.getUser(userId);
        if (Objects.isNull(userInfo)){
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }

        UserHolder.setUserId(userId);

        return userId;

    }

}
