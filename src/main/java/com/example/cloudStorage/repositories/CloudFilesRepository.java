package com.example.cloudStorage.repositories;


import com.example.cloudStorage.entities.CloudFile;
import com.example.cloudStorage.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CloudFilesRepository extends JpaRepository<CloudFile, Long> {


    List<CloudFile> findAllByOwnerOrderByCreatedDesc(User user);


    Optional<CloudFile> findCloudFileByName(String name);



    //поиск файлов по имени файла и юзеру
    Optional<CloudFile> findCloudFileByNameAndOwner(String fileName, User user);


    //удаление файла по id

    void deleteById(Long id);

}
