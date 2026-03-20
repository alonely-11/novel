package com.novel.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.novel.core.common.resp.RestResp;
import com.novel.dto.resp.ImgVerifyCodeRespDto;
import com.novel.manager.VerifyCodeManager;
import com.novel.manager.VerifyCodeManager;
import com.novel.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    private final VerifyCodeManager verifyCodeManager;

//    ImgVerifyCodeRespDto: sessionId img--String
    @Override
    public RestResp<ImgVerifyCodeRespDto> getImgVerifyCode() throws IOException {
//        String seesionId = UUID.randomUUID().toString();
        String seesionId = IdWorker.get32UUID();
        return RestResp.ok(ImgVerifyCodeRespDto.builder()
                .sessionId(seesionId)
                .img(verifyCodeManager.getImgVerifyCode(seesionId))
                .build());
    }
}
