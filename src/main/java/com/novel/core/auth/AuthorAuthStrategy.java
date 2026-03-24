package com.novel.core.auth;

import com.novel.core.common.constant.ApiRouterConsts;
import com.novel.core.common.constant.ErrorCodeEnum;
import com.novel.core.common.exception.BusinessException;
import com.novel.core.util.JwtUtils;
import com.novel.dao.entity.AuthorInfo;
import com.novel.dto.AuthorInfoDto;
import com.novel.manager.AuthorInfoCacheManager;
import com.novel.manager.UserInfoCacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthorAuthStrategy implements AuthStrategy {

    private JwtUtils jwtUtils;
    private UserInfoCacheManager userInfoCacheManager;
    private AuthorInfoCacheManager authorInfoCacheManager;

    private static final List<String> EXCLUDE_URI = List.of(
            ApiRouterConsts.API_AUTHOR_URL_PREFIX + "/register",
            ApiRouterConsts.API_AUTHOR_URL_PREFIX + "/status"
    );

    @Override
    public void auth(String token, String requestUri) throws BusinessException {

        Long userId = authSSO(jwtUtils,userInfoCacheManager,token);

        if (EXCLUDE_URI.contains(requestUri)) {
            return;
        }

        AuthorInfoDto authorInfo = authorInfoCacheManager.getAuthor(userId);

        if (Objects.isNull(authorInfo)) {
            throw new BusinessException(ErrorCodeEnum.USER_UN_AUTH);
        }

        UserHolder.setUserId(authorInfo.getId());

    }
}
