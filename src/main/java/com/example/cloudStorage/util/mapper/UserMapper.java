package com.example.cloudStorage.util.mapper;

import com.example.cloudStorage.dto.entity.UserDto;
import com.example.cloudStorage.dto.request.SignupRequest;
import com.example.cloudStorage.entities.User;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMapper {
    ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }



    public User convertToUser(UserDto userDto) {
        return  this.modelMapper.map(userDto, User.class);
    }

    public User convertToSignupUser(SignupRequest request) { return this.modelMapper.map(request, User.class);}
    public SignupRequest convertToUseSignupReq(User user) {
        return this.modelMapper.map(user,
                SignupRequest.class);
    }
    public UserDto convertToUserDto(User user) {
        return this.modelMapper.map(user, UserDto.class);
    }
}
