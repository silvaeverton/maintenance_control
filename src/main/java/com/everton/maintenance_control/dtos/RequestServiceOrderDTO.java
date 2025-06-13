package com.everton.maintenance_control.dtos;

import com.everton.maintenance_control.enums.StatusServiceOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Requisição de dados da ordem de serviço")
public class RequestServiceOrderDTO {

    private Long id;

    @Schema(example = "Manutenção preventiva")
    private String maintenanceType;

    @Schema(example = "João Silva")
    private String name;

    @Schema(example = "Troca de óleo da máquina CNC")
    private String problemDescription;

    @Schema(example = "OPEN")
    private StatusServiceOrder status;

    @Schema(example = "1")
    private Long technicianId;

    @Schema(example = "2")
    private Long machineId;



}
