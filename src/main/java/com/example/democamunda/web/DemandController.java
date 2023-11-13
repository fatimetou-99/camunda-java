package com.example.democamunda.web;


import com.example.democamunda.entity.Demand;
import com.example.democamunda.service.CamundaService;
import com.example.democamunda.service.DemandService;
import com.example.democamunda.web.api.DemandControllerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class DemandController implements DemandControllerApi {

    @Autowired
    private  DemandService demandService;

    @Autowired
    private CamundaService camundaService;

    @Override
    public ResponseEntity<Object> saveDemand(Demand demand) {
        camundaService.startCamundaProcess(demand.getId());
        return demandService.saveDemand(demand);
    }

    @Override
    public ResponseEntity<?> submitDemand(String task, Demand demand) {
        camundaService.completeCamundaTask(task);
        return demandService.submitDemand(demand);
    }

    @Override
    public ResponseEntity<?> cancelDemand(String task, Demand demand) {
        camundaService.completeCamundaTask(task);
        return demandService.cancelDemand(demand);
    }

    @Override
    public ResponseEntity<?> inprogressDemand(Demand demand) {
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
