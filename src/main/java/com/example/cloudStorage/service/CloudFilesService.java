package com.example.cloudStorage.service;


import com.example.cloudStorage.entities.CloudFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface CloudFilesService {

    CloudFile upload (MultipartFile resource, Principal principal) throws IOException;

    List<CloudFile> getAllCloudFileForUser(Principal principal);

    CloudFile download(String filename, Principal principal);

    void deleteFile(String filename, Principal principal);

    Optional<CloudFile> getCloudFileByName(String filename);



}
