package com.example.authentication.remote;

import com.example.authentication.model.Token;
import com.example.authentication.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@Setter
//@TestPropertySource(locations="classpath:application.properties")
@SpringBootTest
//@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
class RemoteDataServiceTest {
    @Value("${office.plan.host}")
    private  String host;

    private  String globalToken;



    @BeforeEach()
    public  void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        Token response = restTemplate.postForObject(host + "/api/auth/login", new User("frank@frank.com", "12#Madd!Ass"), Token.class);
        globalToken = response.getAccessToken();
    }
    @Test
    public void getData(){
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String response =  restTemplate.getForObject(host + "/api/hello", String.class);
        assertThat(response).isNotEmpty();
    }

    @Test
    public void getDataOauth(){

        RestTemplate restTemplate = new RestTemplateBuilder().build();
        Token response = restTemplate.postForObject(host + "/api/auth/login", new User("frank@frank.com", "12#Madd!Ass"), Token.class);
        assertThat(response.getAccessToken()).isNotEmpty();
        HttpEntity<String> request = new HttpEntity<>("parameters", createHttpHeaders(response.getAccessToken()));
        ResponseEntity<String> rr = restTemplate.exchange(host + "/api/users", HttpMethod.GET, request, String.class);
        assertThat(rr).isNotNull();
    }


    private HttpHeaders createHttpHeaders(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    @Test
    public void getUsingInterceptor() {
        RestTemplate restTemplate = new RestTemplateBuilder().additionalInterceptors((httpRequest, bytes, clientHttpRequestExecution) -> {
            httpRequest.getHeaders().setBearerAuth(globalToken);
            return clientHttpRequestExecution.execute(httpRequest, bytes);
        }).build();
        ResponseEntity<String> rr = restTemplate.getForEntity(host + "/api/users", String.class);
        assertThat(rr).isNotNull();
    }

}