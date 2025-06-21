package com.everton.maintenance_control.controllers;

import com.everton.maintenance_control.dtos.MachineRequestDTO;
import com.everton.maintenance_control.dtos.MachineResponseDTO;
import com.everton.maintenance_control.dtos.ResponseServiceOrderDTO;
import com.everton.maintenance_control.model.Machine;
import com.everton.maintenance_control.services.MachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/machine")
@Tag(name = "Machine Controllers", description = "Endpoint para gerenciamento de maquinas")
public class MachineController {

    private final MachineService machineService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar nova maquina", description = "Criar nova maquina no sistema")
    public Machine createMachine( @RequestBody MachineRequestDTO dto) {
        return machineService.createMachine(dto);
    }

    @GetMapping("/{idMachine}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar maquina por ID", description = "Buscar detalhes da maquina através do ID")
    public MachineResponseDTO findMachineById(@PathVariable("idMachine") Long idMachine) {
        return machineService.findMachineById(idMachine);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar todas as Maquinas", description = "Listas todas as maquinas no sistema")
    public List<MachineResponseDTO> allMachine() {
        return machineService.allMachine();
    }

    @PutMapping("/{idMachine}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualização Maquina", description = "Buscar maquina po id e realiza atualização de dados")
    public Machine updateMachine(@PathVariable("idMachine") Long idMachine,
                                  @RequestBody MachineRequestDTO updateMachine) {

        return machineService.updateMachine(idMachine, updateMachine);
    }

    @DeleteMapping("/{idMachine}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar maquina", description = "buscar maquina por Id e realizar o delete")
    public void deleteMachine(@PathVariable("idMachine")Long idMachine) {
        machineService.deleteMachine(idMachine);
    }

    @GetMapping("/order/{idMachine}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar O.S por maquina", description = "Busca o técnico por Id e lista as O.S vinculadas")
    public List<ResponseServiceOrderDTO> findOrderByMachine(@PathVariable("idMachine") Long idMachine) {
        return machineService.findOrderByMachine(idMachine);
    }
}
