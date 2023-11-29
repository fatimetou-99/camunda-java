package com.example.democamunda.web;


import com.example.democamunda.dto.Task;
import com.example.democamunda.entity.Demand;
import com.example.democamunda.service.CamundaService;
import com.example.democamunda.service.DemandService;
import com.example.democamunda.web.api.DemandControllerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class DemandController implements DemandControllerApi {

    @Autowired
    private  DemandService demandService;

    @Autowired
    private CamundaService camundaService;

    @Override
    public ResponseEntity<Object> saveDemand(Demand demand) {

        // get task id

        List<Task> tasks = camundaService.retrieveTaskId("order");

        Task task = tasks.get(0);
        System.out.println(task.getId());


        camundaService.treatOrder(task.getId(),false);
        return demandService.saveDemand(demand);
    }

    @Override
    public ResponseEntity<?> submitDemand(Demand demand) {
        return demandService.submitDemand(demand);
    }

    @Override
    public ResponseEntity<?> cancelDemand(Demand demand) {
        List<Task> tasks = camundaService.retrieveTaskId("order");

        Task task = tasks.get(4);

        camundaService.completeTaskWithoutVariables("42da961f-8959-11ee-9773-02d230d617a1");
        return demandService.cancelDemand(demand);
    }

    @Override
    public ResponseEntity<?> inProgressDemand(Demand demand) {
        return demandService.inprogressDemand(demand);
    }

    @Override
    public ResponseEntity<?> confirmDemand(Demand demand) {
        return demandService.confirmDemand(demand);
    }


    @Override
    public ResponseEntity<?> rejectDemand(Demand demand) {
        return demandService.rejectDemand(demand);
    }


}
