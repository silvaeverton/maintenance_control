package com.everton.maintenance_control.services.impl;

import com.everton.maintenance_control.dtos.MachineRequestDTO;
import com.everton.maintenance_control.dtos.MachineResponseDTO;
import com.everton.maintenance_control.dtos.ResponseServiceOrderDTO;
import com.everton.maintenance_control.enums.StatusMachine;
import com.everton.maintenance_control.enums.StatusServiceOrder;
import com.everton.maintenance_control.exceptions.custom.NotFoundException;
import com.everton.maintenance_control.model.Machine;
import com.everton.maintenance_control.model.ServiceOrder;
import com.everton.maintenance_control.repositories.MachineRepository;
import com.everton.maintenance_control.repositories.ServiceOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MachineServiceImplTest {

    @InjectMocks
    MachineServiceImpl machineService;

    @Mock
    MachineRepository repository;

    @Mock
    ServiceOrderRepository serviceOrderRepository;

    @Test
    void mustToCreateMachine() {

        MachineRequestDTO machineRequestDTO = new MachineRequestDTO();
        machineRequestDTO.setName("Torno CNC 5000");

        Machine machineSave = new Machine();
        machineSave.setId(1L);
        machineSave.setName("Torno CNC 5000");

        when(repository.saveAndFlush(any(Machine.class))).thenReturn(machineSave);

        Machine result = machineService.createMachine(machineRequestDTO);

        assertNotNull(result);
        assertEquals(1L,result.getId());
        assertEquals("Torno CNC 5000", result.getName());

        verify(repository, times(1)).saveAndFlush(any(Machine.class));


    }

    @Test
    void shouldReturnMachineIfIdExist() {

        Machine machine = new Machine();
        machine.setName("Torno CNC 5000");
        machine.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(machine));

        MachineResponseDTO responseDTO = machineService.findMachineById(1L);

        assertNotNull(responseDTO);
        assertEquals(1L,responseDTO.getId());
        assertEquals("Torno CNC 5000", responseDTO.getName());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenNotExist() {

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()->{
            machineService.findMachineById(99L);
        });


    }

    @Test
    void mustReturnListMachineIsExist() {

        when(repository.findAll()).thenReturn(List.of(new Machine()));

        List<MachineResponseDTO> machineList = machineService.allMachine();

        assertNotNull(machineList);

        verify(repository, times(1)).findAll();


    }

    @Test
    void shouldThrowExceptionIfListEmpty() {

        when(repository.findAll()).thenReturn(List.of());

        assertThrows(NotFoundException.class, ()->{
            machineService.allMachine();
        });
    }

    @Test
    void mustToUpdateMachine() {

        Long idMachine = 1L;
        Machine machineExistent = new Machine();
        machineExistent.setId(idMachine);
        machineExistent.setName("Torno CNC 5000");
        machineExistent.setSector("Sector A");
        machineExistent.setManufacturer("manufacture A");
        machineExistent.setStatus(StatusMachine.ACTIVE);

        MachineRequestDTO updateDTO = new MachineRequestDTO();
        updateDTO.setName("Torno CNC 5000");
        updateDTO.setSector("Sector A");
        updateDTO.setManufacturer("manufacture A");
        updateDTO.setStatusMachine(StatusMachine.INACTIVE);

        when(repository.findById(idMachine)).thenReturn(Optional.of(machineExistent));
        when(repository.saveAndFlush(any(Machine.class))).thenReturn(machineExistent);

        Machine result = machineService.updateMachine(idMachine,updateDTO);

        assertNotNull(result);
        assertEquals("Torno CNC 5000",result.getName());
        assertEquals("Sector A",result.getSector());
        assertEquals("manufacture A", result.getManufacturer());
        assertEquals(StatusMachine.INACTIVE, result.getStatus());

        verify(repository, times(1)).findById(idMachine);
        verify(repository, times(1)).saveAndFlush(machineExistent);


    }
    @Test
    void shouldUpdateName() {

        Long idMachine = 1L;
        Machine machineExistent = new Machine();
        machineExistent.setId(idMachine);
        machineExistent.setName("Old name");
        machineExistent.setSector("Sector A");
        machineExistent.setManufacturer("manufacture A");
        machineExistent.setStatus(StatusMachine.ACTIVE);

        MachineRequestDTO updateDTO = new MachineRequestDTO();
        updateDTO.setName("New Name");


        when(repository.findById(idMachine)).thenReturn(Optional.of(machineExistent));
        when(repository.saveAndFlush(any(Machine.class))).thenAnswer(invocation -> invocation.getArgument(0));


        Machine result = machineService.updateMachine(idMachine,updateDTO);

        assertNotNull(result);
        assertEquals("New Name",result.getName());
        assertEquals("Sector A",result.getSector());
        assertEquals("manufacture A", result.getManufacturer());
        assertEquals(StatusMachine.ACTIVE, result.getStatus());

        verify(repository, times(1)).findById(idMachine);
        verify(repository, times(1)).saveAndFlush(machineExistent);


    }

    @Test
    void shouldUpdateSector() {

        Long idMachine = 1L;
        Machine machineExistent = new Machine();
        machineExistent.setId(idMachine);
        machineExistent.setName("Torno CNC 5000");
        machineExistent.setSector("Old Sector");
        machineExistent.setManufacturer("manufacture A");
        machineExistent.setStatus(StatusMachine.ACTIVE);

        MachineRequestDTO updateDTO = new MachineRequestDTO();
        updateDTO.setSector("New Sector");


        when(repository.findById(idMachine)).thenReturn(Optional.of(machineExistent));
        when(repository.saveAndFlush(any(Machine.class))).thenAnswer(invocation -> invocation.getArgument(0));


        Machine result = machineService.updateMachine(idMachine,updateDTO);

        assertNotNull(result);
        assertEquals("Torno CNC 5000",result.getName());
        assertEquals("New Sector",result.getSector());
        assertEquals("manufacture A", result.getManufacturer());
        assertEquals(StatusMachine.ACTIVE, result.getStatus());

        verify(repository, times(1)).findById(idMachine);
        verify(repository, times(1)).saveAndFlush(machineExistent);


    }

    @Test
    void shouldUpdateManufacturer() {

        Long idMachine = 1L;
        Machine machineExistent = new Machine();
        machineExistent.setId(idMachine);
        machineExistent.setName("Torno CNC 5000");
        machineExistent.setSector("Sector A");
        machineExistent.setManufacturer(" old manufacturer ");
        machineExistent.setStatus(StatusMachine.ACTIVE);

        MachineRequestDTO updateDTO = new MachineRequestDTO();
        updateDTO.setManufacturer("New manufacturer");


        when(repository.findById(idMachine)).thenReturn(Optional.of(machineExistent));
        when(repository.saveAndFlush(any(Machine.class))).thenAnswer(invocation -> invocation.getArgument(0));


        Machine result = machineService.updateMachine(idMachine, updateDTO);

        assertNotNull(result);
        assertEquals("Torno CNC 5000", result.getName());
        assertEquals("Sector A", result.getSector());
        assertEquals("New manufacturer", result.getManufacturer());
        assertEquals(StatusMachine.ACTIVE, result.getStatus());

        verify(repository, times(1)).findById(idMachine);
        verify(repository, times(1)).saveAndFlush(machineExistent);

    }
    @Test
    void mustToThrowExceptionWhenMachineNotFound() {

        Long idMachine = 99L;

        MachineRequestDTO dto = new MachineRequestDTO();
        dto.setName("Machine not exist");

        when(repository.findById(idMachine)).thenReturn(Optional.empty());

        assertThrows( NotFoundException.class, ()->{
            machineService.updateMachine(idMachine,dto);
        });


        verify(repository, times(1)).findById(idMachine);
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void mustDeleteMachine() {

        Long idMachine = 1L;

        machineService.deleteMachine(idMachine);

        verify(repository, times(1)).deleteById(idMachine);

    }

    @Test
    void mustReturnOrderListByMachine() {

        Long idMachine = 1L;
        ServiceOrder order = new ServiceOrder();
        order.setId(100L);
        order.setName("Daniel Santos");
        order.setMaintenanceType("Manutenção ");
        order.setStatus(StatusServiceOrder.IN_PROGRESS);
        order.setOpeningDate(LocalDateTime.now());

        List<ServiceOrder> list = List.of(order);

        when(serviceOrderRepository.findByMachineId(idMachine)).thenReturn(list);

        List<ResponseServiceOrderDTO> result = machineService.findOrderByMachine(idMachine);

        assertNotNull(result);
        assertEquals(1,result.size());
        ResponseServiceOrderDTO dto = result.get(0);
        assertEquals(order.getId(),dto.getId());
        assertEquals(order.getName(),dto.getName());
        assertEquals(order.getMaintenanceType(),dto.getMaintenanceType());
        assertEquals(order.getStatus(),dto.getStatus());
        assertEquals(order.getOpeningDate(),dto.getOpeningDate());

        verify(serviceOrderRepository,times(1)).findByMachineId(idMachine);


    }


}