package com.zq.backend.converter;

import com.zq.backend.object.dto.UserExtension;
import com.zq.backend.object.enums.RoleTypeEnum;
import com.zq.backend.object.dto.UserDTOWithPassword;
import com.zq.backend.object.enums.UserStatusEnum;
import com.zq.backend.object.params.RegisterPararm;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Objects;

@Mapper(uses = ConverterWorker.class)
public interface ParamConverter {
    ParamConverter INSTANCE = Mappers.getMapper(ParamConverter.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "extension", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserDTOWithPassword toUserDTOWithPassword(RegisterPararm param);

    @AfterMapping
    default void afterMapping(@MappingTarget UserDTOWithPassword userDTO, RegisterPararm param) {
        if(Objects.isNull(param)) {
            return;
        }
        userDTO.setStatus(UserStatusEnum.NORMAL);
        userDTO.setRole(RoleTypeEnum.USER);
        userDTO.setExtension(new UserExtension());
    }
}
