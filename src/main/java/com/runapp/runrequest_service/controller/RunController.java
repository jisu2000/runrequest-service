package com.runapp.runrequest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.runapp.runrequest_service.service.RunRequestService;

@RestController
@RequestMapping("/api/run")
public class RunController {

    @Autowired
    private RunRequestService runRequestService;

    @PostMapping(path = "/create-run", consumes = { "multipart/form-data" })

    public ResponseEntity<?> createRun(
            @RequestPart("MAP_PICTURE") MultipartFile mapPicture,
            @RequestPart("RUN_DATA") String runData,
            @RequestHeader("Authorization") String authHeader) {

        return new ResponseEntity<>(runRequestService.createRunRequest(authHeader, runData, mapPicture), HttpStatus.OK);
    }

    @GetMapping("/get-all-run")
    public ResponseEntity<?> getAllRunOfUser(
            @RequestHeader("Authorization") String authHeader) {
        return new ResponseEntity<>(runRequestService.getAllRunDtoOfUser(authHeader), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRun(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader) {
        return new ResponseEntity<>(runRequestService.deleteRun(authHeader, id), HttpStatus.OK);
    }

}
