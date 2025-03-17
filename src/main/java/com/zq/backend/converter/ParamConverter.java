package com.zq.backend.converter;

import com.zq.backend.object.RoleTypeEnum;
import com.zq.backend.object.dto.UserDTOWithPassword;
import com.zq.backend.object.params.RegisterPararm;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper
public interface ParamConverter {
    ParamConverter INSTANCE = Mappers.getMapper(ParamConverter.class);

    UserDTOWithPassword toUserDTOWithPassword(RegisterPararm param);

    @AfterMapping
    default void afterMapping(@MappingTarget UserDTOWithPassword userDTO, RegisterPararm param) {
        if(Objects.isNull(param)) {
            return;
        }
        userDTO.setRole(RoleTypeEnum.USER);
    }
}
