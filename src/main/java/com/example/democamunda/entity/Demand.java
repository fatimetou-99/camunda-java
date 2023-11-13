package com.example.democamunda.entity;

import com.example.democamunda.enumeration.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data  // Generates getters, setters, toString, equals, and hashCode
@AllArgsConstructor  // Generates all-args constructor
@NoArgsConstructor  // Generates no-args constructor
public class Demand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Status status;
}
