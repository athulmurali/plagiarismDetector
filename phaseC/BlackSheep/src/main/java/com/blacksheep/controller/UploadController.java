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

@RestController
public class UploadController {
    private final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private static String SOURCE_PATH = "D:/Manpreet/sem2/MSD/Project/workspace/temp/source/";
    private static String SUSPECT_PATH = "D:/Manpreet/sem2/MSD/Project/workspace/temp/suspect/";

    @RequestMapping(method = RequestMethod.POST, value = "/upload/source")
    public ResponseEntity<?> uploadFileSource(@RequestParam("files") MultipartFile[] files) {

        return uploadFiles(SOURCE_PATH, files);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/upload/suspect")
    public ResponseEntity<?> uploadFileSuspect(@RequestParam("files") MultipartFile[] files) {

        return uploadFiles(SUSPECT_PATH, files);
    }

    public ResponseEntity<?> uploadFiles(String savePath, MultipartFile[] files) {

        String uploadedFileName = Arrays.stream(files).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity("please select a file!", HttpStatus.BAD_REQUEST);
        }

        try {
            saveUploadedFiles(Arrays.asList(files), savePath);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded - " + uploadedFileName, HttpStatus.OK);
    }

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
}
