package com.annafabrichnaya.cloudstorage.service;

import com.annafabrichnaya.cloudstorage.entities.JwtBlackListEntity;

public interface JwtBlackListService {

    boolean isBlacklisted(String jwt);

    JwtBlackListEntity saveInBlackList(String jwt);
}
