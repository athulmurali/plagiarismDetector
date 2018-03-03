package com.blacksheep.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains the implementation for the file upload use case
 */
@RestController
public class UploadController {

    /**
     * Logger instance
     */
    private final Logger logger = LoggerFactory.getLogger(UploadController.class);

    /**
     * Receives the source files and sends them to the upload method
     *
     * @param files : source files to be uploaded
     */
    @RequestMapping(method = RequestMethod.POST, value = "/upload/source")
    public ResponseEntity uploadFileSource(@RequestParam("files") MultipartFile[] files) {
        String path = getUploadLocation();
        path = path + "/src/main/resources/python/";

        return uploadFiles(path, files);
    }

    /**
     * Receives the suspect files and sends them to the upload method
     *
     * @param files : suspect files to be uploaded
     */
    @RequestMapping(method = RequestMethod.POST, value = "/upload/suspect")
    public ResponseEntity uploadFileSuspect(@RequestParam("files") MultipartFile[] files) {
        String path = getUploadLocation();
        path = path + "/src/main/resources/python/";

        return uploadFiles(path, files);
    }

    /**
     * Uploads the files to the specified location
     *
     * @param savePath : file save location
     * @param files : files to the saved
     */
    private ResponseEntity<?> uploadFiles(String savePath, MultipartFile[] files) {

        try {
            String uploadedFileName = Arrays.stream(files).map(x -> x.getOriginalFilename())
                    .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

            if (StringUtils.isEmpty(uploadedFileName)) {
                return new ResponseEntity<>("Please select a file!", HttpStatus.BAD_REQUEST);
            }

            saveUploadedFiles(Arrays.asList(files), savePath);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Upload success");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Saves the uploaded files to the specified location
     *
     * @param savePath : file save location
     * @param files : files to the saved
     */
    private void saveUploadedFiles(List<MultipartFile> files, String savePath) throws IOException {
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get(savePath + file.getOriginalFilename());
            Files.write(path, bytes);
        }
    }

    /**
     * Returns the absolute path of the file save location
     *
     */
    private String getUploadLocation(){
        Path workingDirectory = Paths.get("").toAbsolutePath();
        String path = workingDirectory.toString();
        path = path.replaceAll("\\\\", "/");
        return path;
    }
}
