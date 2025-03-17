package com.zq.backend.converter;

import com.zq.backend.object.RoleTypeEnum;
import com.zq.backend.object.data.UserDO;
import com.zq.backend.object.dto.UserDTO;
import com.zq.backend.object.dto.UserDTOWithPassword;
import com.zq.backend.object.dto.UserExtension;
import com.zq.backend.object.vo.UserVO;
import org.mapstruct.AfterMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

@Mapper
public interface DTOConverter {
    DTOConverter INSTANCE = Mappers.getMapper(DTOConverter.class);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "extension", ignore = true)
    @Named("toUserDTO")
    UserDTO toUserDTO(UserDO userDO);

    @IterableMapping(qualifiedByName = "toUserDTO")
    List<UserDTO> toUserDTOList(List<UserDO> userDOList);

    @AfterMapping
    default void afterMapping(@MappingTarget UserDTO userDTO, UserDO userDO) {
        if(Objects.isNull(userDO)) {
            return;
        }
        userDTO.setRole(RoleTypeEnum.parse(userDO.getRole()));
        userDTO.setExtension(UserExtension.parse(userDO.getExtension()));
    }

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "extension", ignore = true)
    @Named("toUserDTOWithPassword")
    UserDTOWithPassword toUserDTOWithPassword(UserDO userDO);

    @AfterMapping
    default void afterMapping(@MappingTarget UserDTOWithPassword userDTO, UserDO userDO) {
        if(Objects.isNull(userDO)) {
            return;
        }
        userDTO.setRole(RoleTypeEnum.parse(userDO.getRole()));
        userDTO.setExtension(UserExtension.parse(userDO.getExtension()));
    }
}
