package com.example.democamunda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
//    @Value("${camunda.base-url}")

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


    public void treatOrder(String processInstanceId, boolean isAccepted) {
        String completeTaskUrl = camundaBaseUrl + "/task/{taskId}/complete";
        String taskId = retrieveTaskIdForProcessInstance(processInstanceId);
        if (isAccepted) {
            // Logique pour traiter une demande acceptée
            System.out.println("Demande acceptée.");
            completeTask(completeTaskUrl, taskId, createVariablesForAcceptance());
        } else {
            // Logique pour traiter une demande refusée
            System.out.println("Demande refusée.");
            completeTask(completeTaskUrl, taskId, createVariablesForRejection());
        }
    }

    private String retrieveTaskIdForProcessInstance(String processInstanceId) {
        String taskQueryUrl = camundaBaseUrl + "/process-instance/{processInstanceId}/task";
        Map<String, String> params = new HashMap<>();
        params.put("processInstanceId", processInstanceId);
        RestTemplate restTemplate = new RestTemplate();
        String taskId = restTemplate.getForObject(taskQueryUrl, String.class, params);
        // Assuming the response contains the task ID, adjust this based on the actual Camunda response
        return taskId;
    }

    private void completeTask(String completeTaskUrl, String taskId, Map<String, Object> variables) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(completeTaskUrl, null, Void.class, taskId, variables);
    }

    private Map<String, Object> createVariablesForAcceptance() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", true);
        return variables;
    }
    private Map<String, Object> createVariablesForRejection() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", false);
        return variables;
    }
}
