package com.everton.maintenance_control.dtos;

import com.everton.maintenance_control.enums.StatusServiceOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Requisição de dados da ordem de serviço")
public class RequestServiceOrderDTO {

    private Long id;

    @NotBlank(message = "O tipo de manutenção não pode estar em branco")
    @Size(min = 8, message = "O tipo de manutenção deve ter no mínimo 8 caracteres")
    @Schema(example = "Manutenção preventiva")
    private String maintenanceType;


    @NotBlank(message = "O nome não pode estar em branco")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    @Schema(example = "João Silva")
    private String name;


    @NotBlank(message = "A descrição do problema não pode estar em branco")
    @Schema(example = "Troca de óleo da máquina CNC")
    private String problemDescription;


    @Pattern(regexp = "OPEN|IN_PROGRESS|FINISHED|CANCELED",
            message = "Status inválido. Valores aceitos: OPEN, IN_PROGRESS, FINISHED, CANCELED")
    @Schema(example = "OPEN")
    private StatusServiceOrder status;

    @Schema(example = "1")
    private Long technicianId;

    @Schema(example = "2")
    private Long machineId;



}
