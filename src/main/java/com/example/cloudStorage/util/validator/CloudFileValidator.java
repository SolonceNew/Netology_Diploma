package com.example.cloudStorage.util.validator;

import com.example.cloudStorage.entities.CloudFile;
import com.example.cloudStorage.service.impl.CloudFilesServiceImpl;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudFileValidator implements Validator {

    CloudFilesServiceImpl cloudFilesServiceImpl;

    @Autowired
    public CloudFileValidator(CloudFilesServiceImpl cloudFilesServiceImpl) {
        this.cloudFilesServiceImpl = cloudFilesServiceImpl;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return CloudFile.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CloudFile cloudFile = (CloudFile) target;
        if(cloudFile.getOwner() == null) {
            return;
        }
        if(cloudFilesServiceImpl.getCloudFileByName(cloudFile.getName()).isPresent()) {
            errors.rejectValue("name", "A file with a such name already exists");

        }

    }
}
