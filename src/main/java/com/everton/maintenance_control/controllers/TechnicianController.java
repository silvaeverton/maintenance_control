package com.everton.maintenance_control.controllers;

import com.everton.maintenance_control.dtos.ResponseServiceOrderDTO;
import com.everton.maintenance_control.dtos.TechnicianRequestDTO;
import com.everton.maintenance_control.dtos.TechnicianResponseDTO;
import com.everton.maintenance_control.model.Technician;
import com.everton.maintenance_control.services.TechnicianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/technician")
@Tag(name = "Technician Controllers", description = "Endpoints para gerenciamento de técnicos")
public class TechnicianController {

    private final TechnicianService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar Técnico",description = "Cadastra técnico no sistema")
    public Technician createTech(@Valid @RequestBody TechnicianRequestDTO dto) {
        return service.createTech(dto);
    }

    @GetMapping("/{idTech}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar técnico por Id", description = "busca detatlhes do técnico por Id")
    public TechnicianResponseDTO findTechById(@PathVariable("idTech") Long idTech) {
        return service.findTechById(idTech);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar todos os técnicos", description = "Lista todos os técnicos do sistema")
    public List<TechnicianResponseDTO> allTech() {
        return service.allTech();
    }

    @PutMapping("/{idTech}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar técnico",description = "Busca técnico por Id e atualiza os dados")
    public Technician updateTech(@PathVariable("idTech") Long idTech,@Valid @RequestBody TechnicianRequestDTO requestDTO) {
        return service.updateTech(idTech, requestDTO);
    }

    @DeleteMapping("/{idTech}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar técnico", description = "Busca técnico por Id e realiza o delete")
    public void deleteTech(@PathVariable("idTech") Long idTech) {
        service.deleteTech(idTech);
    }

    @GetMapping("/order/{idTech}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar O.S por Técnico",description = "Busca o técnico por Id e lista as O.S vinculadas")
    public List<ResponseServiceOrderDTO> findOrderByTech(@PathVariable("idTech") Long idTech) {
        return service.findOrderByTech(idTech);
    }
}
