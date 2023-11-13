package com.example.democamunda.service;

import com.example.democamunda.entity.Demand;
import com.example.democamunda.utils.ReturnMessages;
import org.springframework.http.ResponseEntity;

public interface DemandService {
     public ResponseEntity<Object> saveDemand(Demand demand);
     public ResponseEntity<?>  submitDemand(Demand demand);
     public ResponseEntity<?>  inprogressDemand(Demand demand);
     public ResponseEntity<?>  confirmDemand(Demand demand);
     public ResponseEntity<?>  cancelDemand(Demand demand);
     public ResponseEntity<?>  rejectDemand(Demand demand);

}
