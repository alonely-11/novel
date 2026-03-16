package com.novel.service;

import com.novel.core.common.resp.RestResp;
import com.novel.dto.resp.ImgVerifyCodeRespDto;

import java.io.IOException;

public interface ResourceService {
    RestResp<ImgVerifyCodeRespDto> getImgVerifyCode() throws IOException;
}
