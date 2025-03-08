package com.zq.backend.converter;

import com.zq.backend.object.dto.UserDTO;
import com.zq.backend.object.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VOConverter {
    VOConverter INSTANCE = Mappers.getMapper(VOConverter.class);

    UserVO toUserVO(UserDTO userDTO);
}
