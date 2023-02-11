package com.example.cloudStorage.util.mapper;

import com.example.cloudStorage.dto.entity.CloudFileDto;
import com.example.cloudStorage.entities.CloudFile;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudFileMapper {

    ModelMapper modelMapper;

    @Autowired
    public CloudFileMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CloudFile toFileFromMultipart(MultipartFile file) {
        try {
            CloudFile cloudFile = new CloudFile();
            cloudFile.setName(file.getOriginalFilename());
            cloudFile.setFileType(file.getContentType());
            cloudFile.setSize(file.getSize());
            cloudFile.setBytes(file.getBytes());
            return cloudFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public CloudFileDto fromCloudFileToDto(CloudFile file) {
        return this.modelMapper.map(file, CloudFileDto.class);
    }

    public CloudFile fromCloudFileDtoToFile(CloudFileDto fileDto) {
        return this.modelMapper.map(fileDto, CloudFile.class);
    }
}

