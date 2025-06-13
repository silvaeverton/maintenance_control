package com.everton.maintenance_control.dtos;

import com.everton.maintenance_control.enums.StatusServiceOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Response retornando os dados da ordem de serviço")
public class ResponseServiceOrderDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Manutenção preventiva")
    private String maintenanceType;

    @Schema(example = "João Silva")
    private String name;

    private LocalDateTime openingDate;// data de abertura

    private LocalDateTime closingDate;// data de fechamento


    @Schema(example = "Troca de óleo da máquina CNC")
    private String problemDescription;

    @Schema(example = "1")
    private Long technicianId;

    @Schema(example = "2")
    private Long machineId;

    @Schema(example = "2024-06-10")
    private LocalDate createDate;

    @Schema(example = "OPEN")
    private StatusServiceOrder status;
}
