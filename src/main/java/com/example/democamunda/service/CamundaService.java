package com.example.democamunda.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
@Service
public class CamundaService {

    @Autowired
    private RestTemplate restTemplate;
//    @Value("${camunda.base-url}")

    private final String camundaBaseUrl = "http://localhost:8080/engine-rest";

    public String startCamundaProcess() {

        String idValue = "";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

//        String requestBody = "{\"businessKey\":\"" + articleId + "\"}";
        String requestBody = "{}";

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        String url = camundaBaseUrl + "/process-definition/key/order/start";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        String responseBody = response.getBody();
        try {
            // Use Jackson ObjectMapper to parse the JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // Extract the "id" variable
            JsonNode id = jsonNode.path("id");
            idValue = id.asText();

            // Print the result
            System.out.println("Id value: " + idValue);

        } catch (Exception e) {
            // Handle JSON parsing exception
            e.printStackTrace();
        }

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Process started successfully.");
        } else {
            System.err.println("Failed to start the process. Response: " + response.getBody());
        }

        return idValue;
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


    public void treatOrder(String taskId, boolean isAccepted) {
        String completeTaskUrl = camundaBaseUrl + "/task/{taskId}/complete";

        if (isAccepted) {
            System.out.println("Order Accepted.");
            completeTask(completeTaskUrl, taskId, createVariablesForAcceptance());
        }
        else {
            System.out.println("Order Refused.");
            completeTask(completeTaskUrl, taskId, createVariablesForRejection());
        }
    }

    public String retrieveTaskIdForProcessInstance(String processDefinitionKey) {
        String taskQueryUrl = camundaBaseUrl + "/process-instance/task?processDefinitionKey="+processDefinitionKey;
//        Map<String, String> params = new HashMap<>();
//        params.put("processDefinitionKey", processDefinitionKey);
        RestTemplate restTemplate = new RestTemplate();

        try {
            System.out.println(restTemplate);
            String taskId = restTemplate.getForObject(taskQueryUrl, String.class);
            return taskId;
        } catch (HttpClientErrorException.NotFound e) {
            System.err.println("Process instance not found. Error message: " + e.getResponseBodyAsString());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void completeTask(String completeTaskUrl, String taskId, Map<String, Object> variables) {
        RestTemplate restTemplate = new RestTemplate();

        System.out.println("variables " + variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(variables);
        restTemplate.postForEntity(completeTaskUrl, requestEntity, Void.class, taskId);

    }

    public void completeTaskWithoutVariables(String taskId) {
        String completeTaskUrl = camundaBaseUrl + "/task/{taskId}/complete";

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(completeTaskUrl, null, Void.class, taskId);

    }

    private Map<String, Object> createVariablesForAcceptance() {
        Map<String, Object> variables = new HashMap<>();

        Map<String, Object> approvedMap = new HashMap<>();
        approvedMap.put("value", "true");

        variables.put("approved", approvedMap);
        return variables;
    }
    private Map<String, Object> createVariablesForRejection() {
        Map<String, Object> variables = new HashMap<>();

        Map<String, Object> approvedMap = new HashMap<>();
        approvedMap.put("value", "false");

        variables.put("approved", approvedMap);
        return variables;
    }
}
