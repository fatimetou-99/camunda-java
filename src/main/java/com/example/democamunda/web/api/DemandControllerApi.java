package com.example.democamunda.web.api;

import com.example.democamunda.entity.Demand;
import com.example.democamunda.utils.Constants;
import com.example.democamunda.utils.ReturnMessages;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(Constants.APP + Constants.DEMAND)
public interface DemandControllerApi {

    @PostMapping
    public ResponseEntity<Object> saveDemand(@RequestBody Demand demand);

    @PutMapping(Constants.SUBMIT)
    public ResponseEntity<?>  submitDemand(@RequestParam("task") String task, @RequestBody Demand demand);

    @PutMapping(Constants.IN_PROGRESS)
    public ResponseEntity<?>  inProgressDemand(@RequestBody Demand demand);

    @PutMapping(Constants.CONFIRM)
    public ResponseEntity<?>  confirmDemand(@RequestBody Demand demand);

    @PutMapping(Constants.CANCEL)
    public ResponseEntity<?>  cancelDemand(@RequestParam("task") String task,@RequestBody Demand demand);

    @PutMapping(Constants.REJECT)
    public ResponseEntity<?>  rejectDemand(@RequestBody Demand demand);
}
