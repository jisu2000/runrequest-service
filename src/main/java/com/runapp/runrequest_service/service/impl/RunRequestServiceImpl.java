package com.runapp.runrequest_service.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runapp.runrequest_service.dto.RunDto;
import com.runapp.runrequest_service.dto.RunRequestDTO;
import com.runapp.runrequest_service.dto.UserDTO;
import com.runapp.runrequest_service.external.UserService;
import com.runapp.runrequest_service.model.RunEO;
import com.runapp.runrequest_service.repo.RunRepo;
import com.runapp.runrequest_service.service.ImageUploadService;
import com.runapp.runrequest_service.service.RunRequestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RunRequestServiceImpl implements RunRequestService {

    private final UserService userService;
    private final ImageUploadService imageUploadService;
    private final RunRepo runrepo;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    @Override
    public RunDto createRunRequest(String authHeader, String rundata, MultipartFile multipartFile) {

        UserDTO userDTO = userService.getUserFromToken(authHeader);
        if (userDTO == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        Map<String, Object> runDataMap = null;

        try {
            runDataMap = objectMapper.readValue(rundata, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Data format");
        }

        Map<?, ?> imageMap = imageUploadService.uploadImage(multipartFile);

        if (imageMap.get("url") == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image has not been uploaded");
        }

        System.out.println(runDataMap);

        RunRequestDTO runReq = new RunRequestDTO();

        // Ensure proper casting and defaults
        runReq.setDurationMillis(((Number) runDataMap.getOrDefault("durationMillis", 0L)).longValue()); // Cast to Long
        runReq.setDistanceMeters(((Number) runDataMap.getOrDefault("distanceMeters", 0)).intValue()); // Cast to Integer
        runReq.setEpochMilliis(((Number) runDataMap.getOrDefault("epochMilliis", 0L)).longValue()); // Cast to Long
        runReq.setLat(((Number) runDataMap.getOrDefault("lat", 0.0)).doubleValue()); // Cast to Double
        runReq.setLon(((Number) runDataMap.getOrDefault("lon", 0.0)).doubleValue()); // Cast to Double
        runReq.setAvgSpeedKmh(((Number) runDataMap.getOrDefault("avgSpeedKmh", 0.0)).doubleValue()); // Cast to Double
        runReq.setMaxSpeedKmh(((Number) runDataMap.getOrDefault("maxSpeedKmh", 0.0)).doubleValue()); // Cast to Double
        runReq.setTotalElevationMeters(((Number) runDataMap.getOrDefault("totalElevationMeters", 0)).intValue()); // Cast to Integer
        runReq.setId((String) runDataMap.getOrDefault("id", null)); // Cast to String
        
        RunEO goingToSaved = modelMapper.map(runReq, RunEO.class);


        goingToSaved.setDateTimeUtc(LocalDateTime.now());
    
        goingToSaved.setUserId(userDTO.getId());
        goingToSaved.setMapPictureUrl(imageMap.get("url").toString());
        RunEO saved = runrepo.save(goingToSaved);

        RunDto response = mapRunDtoFromRunEO(saved);

        return response;

    }

    @Override
    public List<RunDto> getAllRunDtoOfUser(String authHeader) {

        UserDTO userDTO = userService.getUserFromToken(authHeader);
        if (userDTO == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        List<RunEO> runs = runrepo.findByUserId(userDTO.getId());

        List<RunDto> response = runs.stream().map(this::mapRunDtoFromRunEO).toList();

        return response;

    }

    @Override
    public String deleteRun(String authHeader, String runId) {

        UserDTO userDTO = userService.getUserFromToken(authHeader);
        if (userDTO == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        RunEO run = runrepo.findById(runId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Run not found"));

        if(run.getUserId()!=userDTO.getId()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You can not delete this run");
        }

        try {
            runrepo.delete(run);
            return "Run deleted";
        } catch (Exception e) {
        }

        return "Request failed";

    }

    private RunDto mapRunDtoFromRunEO(RunEO runEO) {
        RunDto runDto = new RunDto();
        runDto.setDurationMillis(runEO.getDurationMillis());
        runDto.setDistanceMeters(runEO.getDistanceMeters());
        runDto.setEpochMilliis(runEO.getEpochMilliis());
        runDto.setLat(runEO.getLat());
        runDto.setLon(runEO.getLon());
        runDto.setAvgSpeedKmh(runEO.getAvgSpeedKmh());
        runDto.setMaxSpeedKmh(runEO.getMaxSpeedKmh());
        runDto.setTotalElevationMeters(runEO.getTotalElevationMeters());
        runDto.setId(runEO.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss dd-MM-yyyy");

        if(runEO.getDateTimeUtc()!=null){
        runDto.setDateTimeUtc(runEO.getDateTimeUtc().format(formatter));
        }
        runDto.setMapPictureUrl(runEO.getMapPictureUrl());

        return runDto;

    }

}
