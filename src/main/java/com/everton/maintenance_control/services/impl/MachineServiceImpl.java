package com.everton.maintenance_control.services.impl;

import com.everton.maintenance_control.dtos.MachineRequestDTO;
import com.everton.maintenance_control.dtos.MachineResponseDTO;
import com.everton.maintenance_control.dtos.ResponseServiceOrderDTO;
import com.everton.maintenance_control.exceptions.custom.NotFoundException;
import com.everton.maintenance_control.model.Machine;
import com.everton.maintenance_control.model.ServiceOrder;
import com.everton.maintenance_control.repositories.MachineRepository;
import com.everton.maintenance_control.repositories.ServiceOrderRepository;
import com.everton.maintenance_control.services.MachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MachineServiceImpl implements MachineService {

    private final MachineRepository repository;

    private final ServiceOrderRepository serviceOrderRepository;


    @Override
    public Machine createMachine(MachineRequestDTO dto) {

        Machine machine = new Machine();

        machine.setName(dto.getName());
        machine.setSector(dto.getSector());
        machine.setStatus(dto.getStatusMachine());
        machine.setManufacturer(dto.getManufacturer());
        machine.setDateAcquisition(LocalDate.now());

        return repository.saveAndFlush(machine);
    }

    @Override
    public MachineResponseDTO findMachineById(Long idMachine) {
        Machine machine = repository.findById(idMachine).orElseThrow(
                ()-> new NotFoundException("Machine not found",404)
        ) ;
        MachineResponseDTO machineResponseDTO = new MachineResponseDTO();
        machineResponseDTO.setId(machine.getId());
        machineResponseDTO.setName(machine.getName());
        machineResponseDTO.setSector(machine.getSector());
        machineResponseDTO.setStatusMachine(machine.getStatus());
        machineResponseDTO.setManufacturer(machine.getManufacturer());
        machineResponseDTO.setDateAcquisition(machine.getDateAcquisition());
        return machineResponseDTO;
    }

    @Override
    public List<MachineResponseDTO> allMachine() {
        List<Machine> machineList = repository.findAll();
        if(machineList.isEmpty()) {
            throw new NotFoundException("The List is Empty",404);
        }
        return machineList.stream()
                .map(machine -> {
                    MachineResponseDTO dto = new MachineResponseDTO();
                    dto.setId(machine.getId());
                    dto.setName(machine.getName());
                    dto.setSector(machine.getSector());
                    dto.setManufacturer(machine.getManufacturer());
                    dto.setStatusMachine(machine.getStatus());
                    dto.setDateAcquisition(machine.getDateAcquisition());
                    return dto;

                        }).toList();

    }

    @Override
    public Machine updateMachine(Long idMachine, MachineRequestDTO updateMachine) {
       Machine machineExist = getMachineById(idMachine);

       if(updateMachine.getName() != null) {
           machineExist.setName(updateMachine.getName());
       }if(updateMachine.getSector() != null) {
            machineExist.setSector(updateMachine.getSector());
        }if(updateMachine.getManufacturer() != null) {
            machineExist.setManufacturer(updateMachine.getManufacturer());
        }if(updateMachine.getStatusMachine() != null) {
            machineExist.setStatus(updateMachine.getStatusMachine());
        }
        repository.saveAndFlush(machineExist);

        return machineExist;
    }

    @Override
    public void deleteMachine(Long idMachine) {
        repository.deleteById(idMachine);

    }

    @Override
    public List<ResponseServiceOrderDTO> findOrderByMachine(Long idMachine) {

        List<ServiceOrder> orders = serviceOrderRepository.findByMachineId(idMachine);

        return orders.stream()
                .map(order ->{
                    ResponseServiceOrderDTO o = new ResponseServiceOrderDTO();
                    o.setMachineId(idMachine);
                    o.setName(order.getName());
                    o.setId(order.getId());
                    o.setMaintenanceType(order.getMaintenanceType());
                    o.setProblemDescription(order.getProblemDescription());
                    o.setTechnicianId(o.getTechnicianId());
                    o.setOpeningDate(order.getOpeningDate());
                    o.setStatus(order.getStatus());
                    return o;

                }).toList();
    }

    private  Machine getMachineById(Long id) {
        return repository.findById(id).orElseThrow(
                ()-> new NotFoundException("Technician not found with id: " + id, 404));

    }
}
