package com.novel.core.common.util;

import com.google.gson.Gson;
import com.novel.core.common.constant.ErrorCodeEnum;
import com.novel.core.common.exception.BusinessException;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

@Data
@RequiredArgsConstructor
@Slf4j
public class QiniuOssUtil {

    private final String accessKey;
    private final String secretKey;
    private final String bucketName;
    private final String referringDomain;

    public String upload(byte[] uploadBytes,String objectName) {

        //构造一个带指定 Region 对象的配置类
        Configuration cfg = Configuration.create(Region.huanan());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

////...生成上传凭证，然后准备上传
//        String accessKey = "your access key";
//        String secretKey = "your secret key";
//        String bucket = "your bucket name";

//默认不指定key的情况下，以文件内容的hash值作为文件名
        //此处指定文件名
        String key = objectName;
//            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            //需要字节数组
            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucketName);
            try {
                Response response = uploadManager.put(byteInputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                ex.printStackTrace();
                if (ex.response != null) {
                    System.err.println(ex.response);
                    try {
                        String body = ex.response.toString();
                        System.err.println(body);
                    } catch (Exception ignored) {
                        throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
                    }
                }
                throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
            }
        StringBuilder stringBuilder = new StringBuilder("/");
            stringBuilder.append(objectName);
        log.info("文件上传到：{}",stringBuilder.toString());
            return stringBuilder.toString();

    }

}
