package com.zq.backend.converter;

import com.zq.backend.object.data.UserDO;
import com.zq.backend.object.dto.UserDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public interface DOConverter {
    DOConverter INSTANCE = Mappers.getMapper(DOConverter.class);

    @Mapping(target = "role", ignore = true)
    UserDO toUserDO(UserDTO userDTO);

    @AfterMapping
    default void afterMapping(@MappingTarget UserDO userDO, UserDTO userDTO) {
        if(Objects.isNull(userDTO)) {
            return;
        }
        if(Objects.nonNull(userDO.getRole())) {
            userDO.setRole(userDTO.getRole().getName());
        }
    }
}
