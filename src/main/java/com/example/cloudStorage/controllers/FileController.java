package com.example.cloudStorage.controllers;


import com.example.cloudStorage.dto.entity.CloudFileDto;
import com.example.cloudStorage.dto.response.MessageResponse;
import com.example.cloudStorage.entities.CloudFile;
import com.example.cloudStorage.service.CloudFilesService;
import com.example.cloudStorage.util.mapper.CloudFileMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController

@RequestMapping("/")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {

    CloudFilesService cloudFilesService;
    CloudFileMapper cloudFileMapper;


    @PostMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> upload(@RequestHeader("auth-token") String authToken,
                                         @RequestParam("filename") String filename, MultipartFile file)
                                         {
        try {
            CloudFile cloudFile = cloudFilesService.upload(authToken, filename, file);
            CloudFileDto cloudFileDto = cloudFileMapper.fromCloudFileToDto(cloudFile);
            return new ResponseEntity<>(cloudFileDto, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }

    @PutMapping("/file")
    public ResponseEntity<String> editFileName(@RequestHeader("auth-token") String authToken,
                                               @RequestParam("filename") String oldFileName,
                                          @RequestBody String newFileName) {
    cloudFilesService.renameFile(authToken, oldFileName, newFileName);
    return new ResponseEntity<>("Success upload", HttpStatus.OK);
    }


    @GetMapping("/file")
    public ResponseEntity<?> download(@RequestParam("filename") String filename,
                                      @RequestHeader("auth-token") String authToken) {

        CloudFile cloudFile = cloudFilesService.download(filename, authToken);
        if (cloudFile != null) {
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + cloudFile.getName())
                    .body(cloudFile);
        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllCloudFiles(@RequestHeader("auth-token") String authToken,
                                              @RequestParam("limit") int limit)  {

        List<CloudFileDto> cloudFileDtos = cloudFilesService.getAllCloudFileForUser(authToken)
                .stream()
                .limit(limit)
                .map(cloudFileMapper::fromCloudFileToDto)
                .collect(Collectors.toList());
        if(limit <=0) return ResponseEntity.status(400).body("Error input data");
        if (cloudFileDtos.size() > 3) return ResponseEntity.status(500).body("Error getting file list");
        return ResponseEntity.status(200).body(cloudFileDtos);


    }

    @DeleteMapping("/file")
    public ResponseEntity<MessageResponse> deleteCloudFile(@RequestParam("filename") String filename,
                                                           @RequestHeader("auth-token") String authToken) {
        cloudFilesService.deleteFile(filename, authToken);
        return  new ResponseEntity<>(new MessageResponse("Success delete"), HttpStatus.OK);

    }

}
