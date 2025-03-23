package com.zq.backend.service;

import com.zq.backend.object.params.GetUploadInfoParam;
import com.zq.backend.object.results.UploadInfo;

public interface OSSService {
    UploadInfo getUploadInfo(GetUploadInfoParam param);
}
