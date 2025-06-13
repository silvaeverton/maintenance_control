package com.everton.maintenance_control.dtos;

import com.everton.maintenance_control.enums.StatusServiceOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Status da O.S")
public class OrderServiceStatusDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Troca de óleo da máquina CNC")
    private String problemDescription;

    @Schema(example = "1")
    private Long technicianId;

    @Schema(example = "1")
    private Long machineId;

    @Schema(example = "OPEN")
    private StatusServiceOrder statusServiceOrder;
}
