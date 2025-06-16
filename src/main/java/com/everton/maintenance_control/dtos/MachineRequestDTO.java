package com.everton.maintenance_control.dtos;

import com.everton.maintenance_control.enums.StatusMachine;
import com.everton.maintenance_control.model.ServiceOrder;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Schema(description = "Request para criação de máquina")
public class MachineRequestDTO {

    private Long id;

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    @NotBlank(message = "O nome não pode ser nulo")
    @Schema(example = "Torno CNC 5000")
    private String name;

    @NotBlank(message = "O nome não pode ser nulo")
    @Size(min = 5, message = "O Setor deve ter no mínimo 5 caracteres")
    @Schema(example = "Usinagem")
    private String sector;

    @NotBlank(message = "O nome não pode ser nulo")
    @Size(min = 3, message = "O Fabricante deve ter no mínimo 3 caracteres")
    @Schema(example = "Siemens")
    private String manufacturer;

    @PastOrPresent(message = "A data deve ser hoje ou no passado")
    @NotNull(message = "A data de aquisição é obrigatória")
    @Schema(example = "2024-06-04")
    private LocalDate dateAcquisition;

    @Pattern(regexp = "ACTIVE,INACTIVE",
            message = "Status inválido. Valores aceitos: ACTIVE, INACTIVE")
    @Schema(example = "ACTIVE")
    private StatusMachine statusMachine;

    @ArraySchema(
            schema = @Schema(implementation = ServiceOrder.class),
            arraySchema = @Schema(description = "Lista de ordens de serviço atribuídas ao técnico")
    )
    private List<ServiceOrder> serviceOrderList;

}
