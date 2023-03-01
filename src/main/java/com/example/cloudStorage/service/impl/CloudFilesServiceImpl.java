package com.example.cloudStorage.service.impl;

import com.example.cloudStorage.entities.CloudFile;
import com.example.cloudStorage.entities.User;
import com.example.cloudStorage.repositories.CloudFilesRepository;
import com.example.cloudStorage.repositories.UserRepository;
import com.example.cloudStorage.security.jwt.JwtUtils;
import com.example.cloudStorage.service.CloudFilesService;
import com.example.cloudStorage.util.exceptions.CloudFileException;
import com.example.cloudStorage.util.exceptions.UsernameNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j

 public class CloudFilesServiceImpl implements CloudFilesService {

    CloudFilesRepository cloudFilesRepository;
    UserRepository userRepository;
    JwtUtils jwtUtils;



    @Autowired
    public CloudFilesServiceImpl(CloudFilesRepository cloudFilesRepository,  UserRepository userRepository,
                                 JwtUtils jwtUtils) {
        this.cloudFilesRepository = cloudFilesRepository;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CloudFile upload(String authToken, String filename, MultipartFile resource) throws IOException {
        User user = getUserByToken(authToken);
        if(resource.isEmpty()) {
            log.info("the file not found");
            throw new CloudFileException("Error input data");
        }
        CloudFile file = CloudFile.builder()
                .name(filename)
                .fileType(resource.getContentType())
                .size(resource.getSize())
                .bytes(resource.getBytes())
                .owner(user)
                .build();
        Optional checkFilename = cloudFilesRepository.findCloudFileByName(file.getName());
        if(checkFilename.isPresent()) {
            log.error("the file with a such name already exist");
            throw new CloudFileException("the file with a such name already exist");

        }
        cloudFilesRepository.save(file);
        log.info("user successfully uploaded a file {}", resource);
        return file;
    }

    @Override
    public CloudFile download(String filename, String authToken) {
        User user = getUserByToken(authToken);
        Optional<CloudFile> file = cloudFilesRepository.findCloudFileByNameAndOwner(filename, user);
        return file.orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(String filename, String authToken) {

        CloudFile cloudFile = getCloudFileByName(filename, authToken);
        cloudFilesRepository.deleteById(cloudFile.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void renameFile(String authToken, String currentFileName,
                           String newFileName) {
        CloudFile file = getCloudFileByName(currentFileName, authToken);
        file.setName(newFileName);
        log.info("File's name was successfully changed");
        cloudFilesRepository.saveAndFlush(file);
    }


    private User getUserByToken(String authToken) {
        String username = jwtUtils.validateTokenRetrieveClaim(authToken.substring(7));
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username
                + " not found"));
    }


    @Override
    public List<CloudFile> getAllCloudFileForUser(String authToken){
        User user = getUserByToken(authToken);
        return cloudFilesRepository.findAllByOwnerOrderByCreatedDesc(user);
    }


    private CloudFile getCloudFileByName(String filename, String authToken) {
        User user = getUserByToken(authToken);
        return cloudFilesRepository.findCloudFileByNameAndOwner(filename, user)
                .orElseThrow(() -> new CloudFileException("File can't be found for user:  "
                        + user.getLogin()
                        ));
    }

  }
