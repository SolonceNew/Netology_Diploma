package com.example.cloudStorage.repositories;


import com.example.cloudStorage.entities.JwtBlackListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtBlackListRepository extends JpaRepository<JwtBlackListEntity, Long> {
    Optional<JwtBlackListEntity> findByJwtEquals(String jwt);
}
