package com.annafabrichnaya.cloudstorage.util.mapper;

import com.annafabrichnaya.cloudstorage.entities.User;
import com.annafabrichnaya.cloudstorage.dto.request.SignupRequest;
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


    public User convertToSignupUser(SignupRequest request) {
        return this.modelMapper.map(request, User.class);
    }
}