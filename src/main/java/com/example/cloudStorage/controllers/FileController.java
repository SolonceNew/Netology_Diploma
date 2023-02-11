package com.example.cloudStorage.controllers;


import com.example.cloudStorage.dto.entity.CloudFileDto;
import com.example.cloudStorage.dto.response.MessageResponse;
import com.example.cloudStorage.entities.CloudFile;
import com.example.cloudStorage.service.CloudFilesService;
import com.example.cloudStorage.util.mapper.CloudFileMapper;
import com.example.cloudStorage.util.validator.ResponseErrorValidator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController

@RequestMapping("api/files")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {

    CloudFilesService cloudFilesService;
    ResponseErrorValidator responseErrorValidator;
    CloudFileMapper cloudFileMapper;


    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file,
                                         Principal principal) {
        try {
            CloudFile cloudFile = cloudFilesService.upload(file, principal);
            CloudFileDto cloudFileDto = cloudFileMapper.fromCloudFileToDto(cloudFile);
            return new ResponseEntity<>(cloudFileDto, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/user/{filename}/download")
    public ResponseEntity<?> download(@RequestParam("filename") String filename, Principal principal) {

        CloudFile cloudFile = cloudFilesService.download(filename, principal);
        if (cloudFile != null) {
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + cloudFile.getName())
                    .body(cloudFile);
        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/allfiles")
    public ResponseEntity<List<CloudFileDto>> getAllCloudFiles(Principal principal) {
        List<CloudFileDto> cloudFileDtos = cloudFilesService.getAllCloudFileForUser(principal)
                .stream()
                .map(cloudFileMapper::fromCloudFileToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(cloudFileDtos, HttpStatus.OK);


    }

    @DeleteMapping("/{filename}/delete")
    public ResponseEntity<MessageResponse> deleteCloudFile(@PathVariable("filename") String name,
                                                           Principal principal) {
        cloudFilesService.deleteFile(name, principal);
        return  new ResponseEntity<>(new MessageResponse("a file was successfully deleted"), HttpStatus.OK);

    }








}
