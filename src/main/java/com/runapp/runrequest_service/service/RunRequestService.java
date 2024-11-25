package com.runapp.runrequest_service.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.runapp.runrequest_service.dto.RunDto;

public interface RunRequestService {
    RunDto createRunRequest(String authHeader, String rundata, MultipartFile multipartFile);
    List<RunDto> getAllRunDtoOfUser(String authHeader);
    String deleteRun(String authHeader, String runId);
}
