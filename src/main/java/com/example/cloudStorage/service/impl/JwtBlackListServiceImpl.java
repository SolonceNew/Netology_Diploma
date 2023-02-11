package com.example.cloudStorage.service.impl;


import com.example.cloudStorage.entities.JwtBlackListEntity;
import com.example.cloudStorage.repositories.JwtBlackListRepository;
import com.example.cloudStorage.security.jwt.JwtUtils;
import com.example.cloudStorage.service.JwtBlackListService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class JwtBlackListServiceImpl implements JwtBlackListService {
    JwtBlackListRepository jwtBlackListRepository;
    JwtUtils jwtUtils;

    @Autowired
    public JwtBlackListServiceImpl(JwtBlackListRepository jwtBlackListRepository, JwtUtils jwtUtils) {
        this.jwtBlackListRepository = jwtBlackListRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public JwtBlackListEntity saveInBlackList(String jwt) {
        Long exp = jwtUtils.extractExpiration(jwt).getTime();

        return jwtBlackListRepository.saveAndFlush(new JwtBlackListEntity(jwt, exp));
    }

    @Override
    public boolean isBlacklisted(String jwt) {
        return jwtBlackListRepository.findByJwtEquals(jwt).isPresent();
    }
}

