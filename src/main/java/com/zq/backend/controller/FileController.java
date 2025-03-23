package com.zq.backend.controller;

import com.zq.backend.aop.Auth;
import com.zq.backend.object.common.BaseResult;
import com.zq.backend.object.enums.RoleTypeEnum;
import com.zq.backend.object.params.GetUploadInfoParam;
import com.zq.backend.object.results.UploadInfo;
import com.zq.backend.service.OSSService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file")
public class FileController extends BaseController {

    @Resource
    private OSSService ossService;

    @Auth(needLogin = false, requireRole = RoleTypeEnum.STRANGER)
    @PostMapping("/upload/info")
    public BaseResult<UploadInfo> getUploadInfo(@RequestBody GetUploadInfoParam param) {
        return doHandle(() -> ossService.getUploadInfo(param), "/api/file/upload/info", param);
    }
}
