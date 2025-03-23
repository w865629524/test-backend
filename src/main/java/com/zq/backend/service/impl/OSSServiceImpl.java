package com.zq.backend.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.zq.backend.object.common.ErrorEnum;
import com.zq.backend.object.common.ExceptionUtil;
import com.zq.backend.object.params.GetUploadInfoParam;
import com.zq.backend.object.results.UploadInfo;
import com.zq.backend.service.OSSService;
import com.zq.backend.utils.LogUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class OSSServiceImpl implements OSSService {

    @Value("${oss.bucket.name}")
    private String bucketName;

    @Value("${oss.region}")
    private String region;

    @Resource
    private OSS ossClient;

    private static final Long DEFAULT_VALID_MILLI_SECONDS = 15L * 60 * 1000;

    @Override
    public UploadInfo getUploadInfo(GetUploadInfoParam param) {
        Long validMilliSeconds = DEFAULT_VALID_MILLI_SECONDS;

        HashMap<String, String> headers = new HashMap<>();
        headers.put(OSSHeaders.CONTENT_TYPE, param.getFileType());
        headers.put("origin_filename", Base64.getEncoder().encodeToString(param.getFileName().getBytes(StandardCharsets.UTF_8)));

        HashMap<String, String> userMetadata = new HashMap<>();

        String distinctName = System.currentTimeMillis() + "-" + UUID.randomUUID().toString().replaceAll("-", "");

        URL signedUrl = getSignedUrl(distinctName, param.getFileMd5(), validMilliSeconds, headers, userMetadata);
        UploadInfo uploadInfo = new UploadInfo();
        uploadInfo.setUploadUrl(signedUrl.toString());
        uploadInfo.setHeaders(headers);
        uploadInfo.setTargetResourceUrl(String.format("https://%s.oss-%s.aliyuncs.com/%s", bucketName, region, distinctName));
        return uploadInfo;
    }

    private URL getSignedUrl(String filename, String md5, Long validateMilliSeconds, Map<String, String> headers, Map<String, String> userMetadata) {
        URL signedUrl = null;
        try {
            Date expireTime = new Date(System.currentTimeMillis() + validateMilliSeconds);
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, filename, HttpMethod.PUT);
            request.setExpiration(expireTime);
            request.setContentMD5(md5);
            request.setHeaders(headers);
            request.setUserMetadata(userMetadata);
            signedUrl = ossClient.generatePresignedUrl(request);
        } catch (OSSException oe) {
            LogUtil.error(log, "ossException", oe, oe::getRequestId, oe::getErrorCode, oe::getErrorMessage, oe::getHostId);
            ExceptionUtil.throwException(ErrorEnum.OSS_ERROR);
        } catch (ClientException ce) {
            LogUtil.error(log, "ossClientException", ce, ce::getMessage);
            ExceptionUtil.throwException(ErrorEnum.OSS_ERROR);
        }
        return signedUrl;
    }
}
