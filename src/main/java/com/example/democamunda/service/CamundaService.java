package com.example.democamunda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
@Service
public class CamundaService {

    @Autowired
    private RestTemplate restTemplate;

    private final String camundaBaseUrl = "http://localhost:8080/engine-rest";

    public void startCamundaProcess(Long articleId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestBody = "{\"businessKey\":\"" + articleId + "\"}";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        String url = camundaBaseUrl + "/process-definition/key/test/start";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        System.err.println("res"+response);
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Process started successfully.");
        } else {
            System.err.println("Failed to start the process. Response: " + response.getBody());
        }
    }


    public void completeCamundaTask(String taskId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        //taskId = "b0447c76-7e22-11ee-bb9b-0242ac110002";
        // Construct the request body with task variables (if any)
        Map<String, Object> requestBodyMap = new HashMap<>();
        // requestBodyMap.put("variables", variables);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBodyMap, headers);

        String url = camundaBaseUrl + "/task/" + taskId + "/complete";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Task completed successfully.");
        } else {
            System.err.println("Failed to complete the task. Response: " + response.getBody());
        }
    }
}
