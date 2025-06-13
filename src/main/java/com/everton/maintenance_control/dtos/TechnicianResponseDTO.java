package com.everton.maintenance_control.dtos;

import com.everton.maintenance_control.model.ServiceOrder;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Response retornando os dados do técnico")
public class TechnicianResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "João Silva")
    private String name;

    @Schema(example = "TEC001")
    private String registration;

    @ArraySchema(
            schema = @Schema(implementation = ServiceOrder.class),
            arraySchema = @Schema(description = "Lista de ordens de serviço atribuídas ao técnico")
    )
    private List<ServiceOrder> serviceOrderList;
}
