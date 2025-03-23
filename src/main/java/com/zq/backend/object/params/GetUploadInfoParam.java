package com.zq.backend.object.params;

import com.zq.backend.object.common.ParamChecker;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetUploadInfoParam extends BaseParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 8203475915037913041L;

    private String fileName;
    private String fileType;
    private Long fileSize;
    private String fileMd5;

    @Override
    public void checkAndRevise() {
        ParamChecker.checkNotBlank(fileName, "fileName");
        ParamChecker.checkNotBlank(fileType, "fileType");
        ParamChecker.checkNotNull(fileSize, "fileSize");
        ParamChecker.checkNotBlank(fileMd5, "fileMd5");
    }
}
