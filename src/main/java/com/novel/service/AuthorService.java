package com.novel.service;

import com.novel.core.common.resp.RestResp;
import com.novel.dto.req.AuthorRegisterReqDto;
import jakarta.validation.Valid;

public interface AuthorService {
    RestResp<Void> register(@Valid AuthorRegisterReqDto dto);

    RestResp<Integer> getStatus(Long userId);
}
