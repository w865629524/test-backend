package com.zq.backend.converter;

import com.zq.backend.object.RoleTypeEnum;
import com.zq.backend.object.dto.UserDTO;
import com.zq.backend.object.vo.UserVO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public interface VOConverter {
    VOConverter INSTANCE = Mappers.getMapper(VOConverter.class);

    UserVO toUserVO(UserDTO userDTO);

    @AfterMapping
    default void afterMapping(@MappingTarget UserVO userVO, UserDTO userDTO) {
        if(Objects.isNull(userDTO)) {
            return;
        }
        userVO.setIsAdmin(RoleTypeEnum.ADMIN.equals(userDTO.getRole()));
    }
}
