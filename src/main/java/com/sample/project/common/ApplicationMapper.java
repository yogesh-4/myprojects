package com.sample.project.common;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.sample.project.domain.AppUserDetails;
import com.sample.project.dto.UserDto;



@Mapper(componentModel = "spring")
public interface ApplicationMapper {
	
	ApplicationMapper INSTANCE = Mappers.getMapper(ApplicationMapper.class);
	
	List<UserDto> mapUserDomainToDto(List<AppUserDetails> userList);

}
