package com.everton.maintenance_control.dtos;


import com.everton.maintenance_control.model.ServiceOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Request para criação de técnico")
public class TechnicianRequestDTO {

    private Long id;

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    @Schema(example = "João Silva")
    private String name;


    @NotNull(message = " O preenchimento de registro é obrigatória")
    @Schema(example = "TEC001")
    private String registration;


    private List<ServiceOrder> serviceOrderList;
}
