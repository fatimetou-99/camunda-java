package com.example.democamunda.service;

import com.example.democamunda.dto.Approved;
import com.example.democamunda.dto.Task;
import com.example.democamunda.dto.Variables;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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

    public List<Task> retrieveTaskId(String processDefinitionKey) {
        String taskQueryUrl = camundaBaseUrl + "/task?processDefinitionKey=" + processDefinitionKey;
        ResponseEntity<List<Task>> responseEntity = restTemplate.exchange(taskQueryUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>() {});

        try {
            return responseEntity.getBody();
        }
        catch (HttpClientErrorException.NotFound e) {
            System.err.println("Process instance not found. Error message: " + e.getResponseBodyAsString());
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void completeTask(String completeTaskUrl, String taskId, Map<String, Object> variables) {

        Map<String, Object>  final_variables = new HashMap<>();
        final_variables.put("variables" , variables);

        RestTemplate restTemplate = new RestTemplate();
        System.out.println(final_variables);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(final_variables, headers);
        System.out.println(requestEntity.getBody());
        try{
            ResponseEntity<Object> responseEntity = restTemplate.postForEntity(completeTaskUrl, requestEntity, Object.class, taskId);
        }

        catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    public void completeTaskWithoutVariables(String taskId) {
        String completeTaskUrl = camundaBaseUrl + "/task/"+ taskId +"/complete";
        System.out.println(completeTaskUrl);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(completeTaskUrl, requestEntity, Void.class);

    }

    private Map<String, Object> createVariablesForAcceptance() {
        Map<String, Object> variables = new HashMap<>();

        Map<String, Object> approvedMap = new HashMap<>();
        approvedMap.put("value", true);

        variables.put("approved", approvedMap);
        return variables;
    }
    private Map<String, Object> createVariablesForRejection() {
        Map<String, Object> variables = new HashMap<>();

        Map<String, Object> approvedMap = new HashMap<>();
        approvedMap.put("value", false);

        variables.put("approved", approvedMap);
        return variables;
    }
}
