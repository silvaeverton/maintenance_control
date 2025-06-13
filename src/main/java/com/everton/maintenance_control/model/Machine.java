package com.everton.maintenance_control.model;

import com.everton.maintenance_control.enums.StatusMachine;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
public class Machine {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String sector;
    private String manufacturer;
    private LocalDate dateAcquisition;

    @Enumerated(EnumType.STRING)
    private StatusMachine status; // Active/Inactive

    @OneToMany(mappedBy = "machine")
    @JsonIgnore
    private List<ServiceOrder> serviceOrderList;
}
