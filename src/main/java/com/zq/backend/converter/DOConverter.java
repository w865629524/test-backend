package com.zq.backend.converter;

import com.alibaba.fastjson2.JSON;
import com.zq.backend.object.data.UserDO;
import com.zq.backend.object.dto.UserDTOWithPassword;
import com.zq.backend.object.params.UpdateUserParam;
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
    @Mapping(target = "extension", ignore = true)
    UserDO toUserDO(UserDTOWithPassword userDTO);

    @AfterMapping
    default void afterMapping(@MappingTarget UserDO userDO, UserDTOWithPassword userDTO) {
        if(Objects.isNull(userDTO)) {
            return;
        }
        if(Objects.nonNull(userDTO.getRole())) {
            userDO.setRole(userDTO.getRole().getName());
        }
        if(Objects.nonNull(userDTO.getExtension())) {
            userDO.setExtension(JSON.toJSONString(userDTO.getExtension()));
        }
    }

    @Mapping(target = "avator", source = "param.newAvator")
    @Mapping(target = "nick", source = "param.newNick")
    @Mapping(target = "email", source = "param.newEmail")
    @Mapping(target = "phone", source = "param.newPhone")
    @Mapping(target = "address", source = "param.newAddress")
    @Mapping(target = "username", source = "username")
    UserDO toUserDO(UpdateUserParam param, String username);
}
