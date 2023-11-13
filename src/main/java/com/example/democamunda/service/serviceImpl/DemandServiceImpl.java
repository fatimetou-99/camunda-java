package com.example.democamunda.service.serviceImpl;

import com.example.democamunda.dao.DemandRepository;
import com.example.democamunda.entity.Demand;
import com.example.democamunda.enumeration.Status;
import com.example.democamunda.service.DemandService;
import com.example.democamunda.utils.ReturnMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DemandServiceImpl implements DemandService {


    @Autowired
    private  DemandRepository demandRepository;
    @Override
    public ResponseEntity<Object> saveDemand(Demand demand) {
        demand.setStatus(Status.DRAFT);
        return ResponseEntity.status(HttpStatus.OK).body(demandRepository.save(demand));

    }

    @Override
    public ResponseEntity<?> submitDemand(Demand demand) {
        demand.setStatus(Status.SUBMITTED);
        return ResponseEntity.status(HttpStatus.OK).body(demandRepository.save(demand));
    }

    @Override
    public ResponseEntity<?> inprogressDemand(Demand demand) {
        demand.setStatus(Status.IN_PROGRESS);
        return ResponseEntity.status(HttpStatus.OK).body(demandRepository.save(demand));
    }

    @Override
    public ResponseEntity<?> confirmDemand(Demand demand) {
        demand.setStatus(Status.CONFIRMED);
        return ResponseEntity.status(HttpStatus.OK).body(demandRepository.save(demand));
    }

    @Override
    public ResponseEntity<?> cancelDemand(Demand demand) {
        demand.setStatus(Status.CANCELLED);
        return ResponseEntity.status(HttpStatus.OK).body(demandRepository.save(demand));
    }

    @Override
    public ResponseEntity<?> rejectDemand(Demand demand) {
        demand.setStatus(Status.REJECTED);
        return ResponseEntity.status(HttpStatus.OK).body(demandRepository.save(demand));
    }
}
