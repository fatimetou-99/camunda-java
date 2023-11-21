package com.example.democamunda.web;


import com.example.democamunda.entity.Demand;
import com.example.democamunda.service.CamundaService;
import com.example.democamunda.service.DemandService;
import com.example.democamunda.web.api.DemandControllerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
        // start process


        camundaService.treatOrder("fbbc8970-885c-11ee-9773-02d230d617a1",false);

        // set variable to true or false
        return demandService.saveDemand(demand);
    }

    @Override
    public ResponseEntity<?> submitDemand(String task, Demand demand) {
        return demandService.submitDemand(demand);
    }

    @Override
    public ResponseEntity<?> cancelDemand(String task, Demand demand) {
        camundaService.completeTaskWithoutVariables(task);
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
