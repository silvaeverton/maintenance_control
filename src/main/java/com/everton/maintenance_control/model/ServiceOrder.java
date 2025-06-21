package com.everton.maintenance_control.model;

import com.everton.maintenance_control.enums.StatusServiceOrder;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class ServiceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime openingDate;// data de abertura
    private LocalDateTime closingDate;// data de fechamento
    private String maintenanceType; //Tip (Preventive/Corrective)
    private String name;
    private LocalDate scheduling;
    private String problemDescription;

    @Enumerated(EnumType.STRING)
    private StatusServiceOrder status;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private Technician technician;

}
