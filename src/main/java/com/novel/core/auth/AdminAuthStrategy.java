package com.novel.core.auth;

import com.novel.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAuthStrategy implements AuthStrategy{
    @Override
    public void auth(String token, String requestUri) throws BusinessException {
        // TODO 平台后台 token 校验
    }
}
