package com.zq.backend.converter;

import com.zq.backend.object.RoleTypeEnum;
import com.zq.backend.object.data.UserDO;
import com.zq.backend.object.dto.UserDTO;
import com.zq.backend.object.vo.UserVO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public interface DTOConverter {
    DTOConverter INSTANCE = Mappers.getMapper(DTOConverter.class);

    @Mapping(target = "role", ignore = true)
    UserDTO fromUserDO(UserDO userDO);

    @AfterMapping
    default void afterMapping(@MappingTarget UserDTO userDTO, UserDO userDO) {
        if(Objects.isNull(userDO)) {
            return;
        }
        userDTO.setRole(RoleTypeEnum.parse(userDO.getRole()));
    }

    UserDTO fromUserVO(UserVO userVO);
}
