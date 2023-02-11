package com.example.cloudStorage.service;

import com.example.cloudStorage.entities.JwtBlackListEntity;

public interface JwtBlackListService {

    boolean isBlacklisted(String jwt);

    JwtBlackListEntity saveInBlackList(String jwt);
}
