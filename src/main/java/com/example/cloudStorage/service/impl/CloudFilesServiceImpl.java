package com.example.cloudStorage.service.impl;

import com.example.cloudStorage.entities.CloudFile;
import com.example.cloudStorage.entities.User;
import com.example.cloudStorage.repositories.CloudFilesRepository;
import com.example.cloudStorage.repositories.UserRepository;
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
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j

 public class CloudFilesServiceImpl implements CloudFilesService {

    CloudFilesRepository cloudFilesRepository;
    UserRepository userRepository;



    @Autowired
    public CloudFilesServiceImpl(CloudFilesRepository cloudFilesRepository,  UserRepository userRepository) {
        this.cloudFilesRepository = cloudFilesRepository;
        this.userRepository = userRepository;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CloudFile upload(MultipartFile resource, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);
        CloudFile file = CloudFile.builder()
                .name(resource.getOriginalFilename())
                .fileType(resource.getContentType())
                .size(resource.getSize())
                .bytes(resource.getBytes())
                .owner(user)
                .build();
        cloudFilesRepository.save(file);
        log.info("user {} successfully uploaded a file {}", user.getUsername(), resource);
        return file;
        }


    public CloudFile download(String filename, Principal principal) {
        User user = getUserByPrincipal(principal);
        Optional<CloudFile> file = cloudFilesRepository.findCloudFileByNameAndOwner(filename, user);
        return file.orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(String filename, Principal principal) {
        CloudFile cloudFile = getCloudFileByName(filename, principal);
        cloudFilesRepository.deleteById(cloudFile.getId());
    }


    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username
                + " not found"));
    }


    @Override
    public List<CloudFile> getAllCloudFileForUser(Principal principal){
        User user = getUserByPrincipal(principal);
        return cloudFilesRepository.findAllByOwnerOrderByCreatedDesc(user);
    }


    private CloudFile getCloudFileByName(String filename, Principal principal) {
        User user = getUserByPrincipal(principal);
        return cloudFilesRepository.findCloudFileByNameAndOwner(filename, user)
                .orElseThrow(() -> new CloudFileException("File can't be found for user:  "
                        + user.getUsername()
                        ));
    }

    @Override
    public Optional<CloudFile> getCloudFileByName(String filename) {
        return cloudFilesRepository.findCloudFileByName(filename);
    }

  }
