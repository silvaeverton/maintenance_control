package com.everton.maintenance_control.dtos;

import com.everton.maintenance_control.enums.StatusMachine;
import com.everton.maintenance_control.model.ServiceOrder;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Schema(description = "Retorno da máquina")
public class MachineResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Torno CNC 5000")
    private String name;

    @Schema(example = "Usinagem")
    private String sector;

    @Schema(example = "Siemens")
    private String manufacturer;

    @Schema(example = "2024-06-04")
    private LocalDate dateAcquisition;

    @Schema(example = "ACTIVE")
    private StatusMachine statusMachine;

    @ArraySchema(
            schema = @Schema(implementation = ServiceOrder.class),
            arraySchema = @Schema(description = "Lista de ordens de serviço atribuídas ao técnico")
    )
    private List<ServiceOrder> serviceOrderList;

}


