package com.zq.backend.object.results;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Data
public class UploadInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 843385331474202815L;

    private String uploadUrl;

    private Map<String, String> headers;

    private String targetResourceUrl;
}
