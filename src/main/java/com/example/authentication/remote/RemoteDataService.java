package com.example.authentication.remote;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Setter
@ConfigurationProperties(prefix = "office.plan")
public class RemoteDataService {

    private String host;
    private static final String ENDPOINT="/api/users/";

    private final RestTemplate restTemplate;


    public List<RemoteUserDto> getRemoteUser(){
        ResponseEntity<RemoteUserDto[]> response = restTemplate.getForEntity(host + ENDPOINT, RemoteUserDto[].class);
        return Arrays.asList(response.getBody());
    }
}
