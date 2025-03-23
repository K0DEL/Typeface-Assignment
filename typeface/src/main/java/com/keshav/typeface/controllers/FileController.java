package com.keshav.typeface.controllers;

import com.keshav.typeface.domains.FileInfo;
import com.keshav.typeface.services.S3Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class FileController {

    S3Service s3Service;

    FileController(S3Service s3Service){
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = s3Service.uploadFile(file);
            return ResponseEntity.ok("File uploaded: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("File upload failed");
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List> listFiles() {

        List<FileInfo> listOfFiles = s3Service.getListOfFiles();
        return ResponseEntity.ok(listOfFiles);
    }

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fileName") String fileName){
        try{
            byte[] file = s3Service.downloadFile(fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(file);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(("Error downloading file: " + e.getMessage()).getBytes());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam String fileName) {
        try {
            s3Service.deleteFile(fileName);
            return ResponseEntity.ok("File deleted successfully: " + fileName);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting file: " + e.getMessage());
        }
    }



}
