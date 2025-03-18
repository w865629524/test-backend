package com.zq.backend.converter;

import com.zq.backend.object.data.UserDO;
import com.zq.backend.object.dto.UserDTO;
import com.zq.backend.object.dto.UserDTOWithPassword;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = ConverterWorker.class)
public interface DTOConverter {
    DTOConverter INSTANCE = Mappers.getMapper(DTOConverter.class);

    @Mapping(target = "role", source = "role", qualifiedByName = "parseRoleTypeEnum")
    @Mapping(target = "status", source = "status", qualifiedByName = "parseUserStatusEnum")
    @Mapping(target = "extension", ignore = true)
    @Named("toUserDTO")
    UserDTO toUserDTO(UserDO userDO);

    @IterableMapping(qualifiedByName = "toUserDTO")
    List<UserDTO> toUserDTOList(List<UserDO> userDOList);

    @Mapping(target = "role", source = "role", qualifiedByName = "parseRoleTypeEnum")
    @Mapping(target = "status", source = "status", qualifiedByName = "parseUserStatusEnum")
    @Mapping(target = "extension", ignore = true)
    @Named("toUserDTOWithPassword")
    UserDTOWithPassword toUserDTOWithPassword(UserDO userDO);
}
