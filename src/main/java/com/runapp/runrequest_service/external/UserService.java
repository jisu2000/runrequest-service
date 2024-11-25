package com.runapp.runrequest_service.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runapp.runrequest_service.dto.UserDTO;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserService {

    @Value("${auth.service.url}")
    private String authUrl;

    private final RestClient.Builder restClientBuilder;
    private RestClient restClient;

    public UserDTO getUserFromToken(String authHeader) {
        UserDTO userDto = null;
        try {
            restClient = restClientBuilder.baseUrl(authUrl).build();

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restClient.get()
                    .uri("/users/get-user")
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .retrieve()
                    .body(Map.class);

            ObjectMapper objectMapper = new ObjectMapper();
            userDto = objectMapper.convertValue(response.get("data"), UserDTO.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userDto;
    }
}
