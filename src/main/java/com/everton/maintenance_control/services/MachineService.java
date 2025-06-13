package com.everton.maintenance_control.services;

import com.everton.maintenance_control.dtos.MachineRequestDTO;
import com.everton.maintenance_control.dtos.MachineResponseDTO;
import com.everton.maintenance_control.dtos.ResponseServiceOrderDTO;
import com.everton.maintenance_control.model.Machine;

import java.util.List;

public interface MachineService {

    public Machine createMachine(MachineRequestDTO dto);
    public MachineResponseDTO findMachineById(Long idMachine);
    public List<MachineResponseDTO> allMachine();
    public Machine updateMachine(Long idMachine, MachineRequestDTO updateMachine);
    public void deleteMachine(Long idMachine);
    public List<ResponseServiceOrderDTO> findOrderByMachine(Long idMachine);
}
