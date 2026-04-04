package com.novel.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.novel.core.common.constant.ErrorCodeEnum;
import com.novel.core.common.exception.BusinessException;
import com.novel.core.common.resp.RestResp;
import com.novel.core.common.util.QiniuOssUtil;
import com.novel.dto.resp.ImgVerifyCodeRespDto;
import com.novel.manager.VerifyCodeManager;
import com.novel.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    private final VerifyCodeManager verifyCodeManager;

    private final QiniuOssUtil qiniuOssUtil;

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

    @Override
    public RestResp<String> uploadImage(MultipartFile file){

        try {

        log.info("文件上传:{}",file);

        if (Objects.isNull(ImageIO.read(file.getInputStream()))){
            throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_TYPE_NOT_MATCH);
        }

        String fileName = file.getOriginalFilename();
        String extensionName = fileName.substring(fileName.lastIndexOf("."));
        String objectName = UUID.randomUUID().toString() + extensionName;

        String filePath = qiniuOssUtil.upload(file.getBytes(), objectName);

        return RestResp.ok(filePath);

        } catch (IOException e) {
            throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
        }
    }
}
