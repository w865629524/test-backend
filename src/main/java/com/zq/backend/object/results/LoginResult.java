package com.zq.backend.object.results;

import com.zq.backend.object.vo.UserVO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class LoginResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 7719385794298617266L;

    private String token;
    private Date tokenExpire;
    private UserVO userInfo;
}
