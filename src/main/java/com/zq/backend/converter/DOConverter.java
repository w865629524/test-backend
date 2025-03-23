package com.zq.backend.converter;

import com.zq.backend.object.data.UserDO;
import com.zq.backend.object.dto.UserDTOWithPassword;
import com.zq.backend.object.params.UpdateUserParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ConverterWorker.class)
public interface DOConverter {
    DOConverter INSTANCE = Mappers.getMapper(DOConverter.class);

    @Mapping(target = "role", source = "userDTO.role.roleName")
    @Mapping(target = "status", source = "userDTO.status.value")
    @Mapping(target = "extension", source = "extension", qualifiedByName = "toNotEmptyJsonString")
    UserDO toUserDO(UserDTOWithPassword userDTO);

    @Mapping(target = "avatar", source = "param.newAvatar")
    @Mapping(target = "nick", source = "param.newNick")
    @Mapping(target = "email", source = "param.newEmail")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "extension", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserDO toUserDO(UpdateUserParam param, String username);
}
