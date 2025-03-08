package com.zq.backend.object.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2451146357322929281L;

    public BaseException(ErrorEnum errorEnum) {
        this.errorCode =  errorEnum.getErrorCode();
        this.errorMessage = errorEnum.getErrorMessage();
    }

    private String errorCode;
    private String errorMessage;
}
