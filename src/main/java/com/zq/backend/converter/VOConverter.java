package com.zq.backend.converter;

import com.zq.backend.object.dto.UserDTO;
import com.zq.backend.object.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = ConverterWorker.class)
public interface VOConverter {
    VOConverter INSTANCE = Mappers.getMapper(VOConverter.class);

    @Mapping(target = "isAdmin", source = "role", qualifiedByName = "checkIsAdmin")
    UserVO toUserVO(UserDTO userDTO);

    List<UserVO> toUserVOList(List<UserDTO> userDTOList);
}
