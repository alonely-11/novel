package com.novel.core.auth;

import com.novel.core.common.exception.BusinessException;
import com.novel.core.util.JwtUtils;
import com.novel.manager.UserInfoCacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FrontAuthStrategy implements AuthStrategy{

    private final JwtUtils jwtUtils;

    private final UserInfoCacheManager  userInfoCacheManager;


    @Override
    public void auth(String token, String requestUri) throws BusinessException {
        authSSO(jwtUtils,userInfoCacheManager,token);
    }
}
