package com.annafabrichnaya.cloudstorage.repositories;


import com.annafabrichnaya.cloudstorage.entities.CloudFile;
import com.annafabrichnaya.cloudstorage.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CloudFilesRepository extends JpaRepository<CloudFile, Long> {


    List<CloudFile> findAllByOwnerOrderByCreatedDesc(User user);


    Optional<CloudFile> findCloudFileByName(String name);



    //поиск файлов по имени файла и юзеру
    Optional<CloudFile> findCloudFileByNameAndOwner(String filename, User user);

}
