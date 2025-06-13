package com.everton.maintenance_control.dtos;


import com.everton.maintenance_control.model.ServiceOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "Request para criação de técnico")
public class TechnicianRequestDTO {

    private Long id;

    @Schema(example = "João Silva")
    private String name;

    @Schema(example = "TEC001")
    private String registration;


    private List<ServiceOrder> serviceOrderList;
}
