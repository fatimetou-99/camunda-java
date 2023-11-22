package com.example.democamunda.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor  // Generates all-args constructor
@NoArgsConstructor
@Getter
@Setter

public class Task {
        private String id;
        private String created;
        private String due;
        private String followUp;
        private String lastUpdated;
        private String delegationState;
        private String description;
        private String executionId;
        private String owner;
        private String parentTaskId;
        private String priority;
        private String processDefinitionId;
        private String processInstanceId;
        private String taskDefinitionKey;
        private String caseExecutionId;
        private String caseInstanceId;
        private String caseDefinitionId;
        private String suspended;
        private String formKey;
        private Object camundaFormRef;
        private String tenantId;

}
