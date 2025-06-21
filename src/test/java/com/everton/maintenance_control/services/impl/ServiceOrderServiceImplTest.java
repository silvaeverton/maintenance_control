package com.everton.maintenance_control.services.impl;

import com.everton.maintenance_control.dtos.OrderServiceStatusDTO;
import com.everton.maintenance_control.dtos.RequestServiceOrderDTO;
import com.everton.maintenance_control.dtos.ResponseServiceOrderDTO;
import com.everton.maintenance_control.enums.StatusServiceOrder;
import com.everton.maintenance_control.exceptions.custom.AlreadyModifiedException;
import com.everton.maintenance_control.exceptions.custom.NotFoundException;
import com.everton.maintenance_control.model.Machine;
import com.everton.maintenance_control.model.ServiceOrder;
import com.everton.maintenance_control.model.Technician;
import com.everton.maintenance_control.repositories.MachineRepository;
import com.everton.maintenance_control.repositories.ServiceOrderRepository;
import com.everton.maintenance_control.repositories.TechnicianRepository;
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
class ServiceOrderServiceImplTest {

    @InjectMocks
    ServiceOrderServiceImpl service;

    @Mock
    ServiceOrderRepository repository;

    @Mock
    TechnicianRepository technicianRepository;

    @Mock
    MachineRepository machineRepository;


    @Test
    void mustCreateOrder() {

        Long idTech = 1L;
        Long idMachine = 2L;
        Machine machine = new Machine();
        machine.setId(idMachine);
        Technician technician = new Technician();
        technician.setId(idTech);

        when(machineRepository.findById(idMachine)).thenReturn(Optional.of(machine));
        when(technicianRepository.findById(idTech)).thenReturn(Optional.of(technician));

        ServiceOrder order = new ServiceOrder();
        order.setId(99L);
        order.setName("Daniel Alves");
        order.setOpeningDate(LocalDateTime.now());
        order.setTechnician(technician);
        order.setMachine(machine);

        RequestServiceOrderDTO dto = new RequestServiceOrderDTO();
        dto.setName("Daniel Alves");
        dto.setMachineId(idMachine);
        dto.setTechnicianId(idTech);
        order.setOpeningDate(LocalDateTime.now());

        when(repository.saveAndFlush(any(ServiceOrder.class))).thenReturn(order);

        ServiceOrder result = service.createOrder(dto);

        assertNotNull(result);
        assertEquals(99L, result.getId());
        assertEquals(order.getTechnician(), result.getTechnician());
        assertEquals(order.getMachine(), result.getMachine());
        assertEquals("Daniel Alves", result.getName());
        assertEquals(order.getOpeningDate(), result.getOpeningDate());


        verify(machineRepository, times(1)).findById(idMachine);
        verify(technicianRepository, times(1)).findById(idTech);

    }

    @Test
    void shouldThrowExceptionIfIdMachineNotFound() {
        RequestServiceOrderDTO dto = new RequestServiceOrderDTO();
        dto.setTechnicianId(1L);
        dto.setMachineId(2L);


        when(technicianRepository.findById(1L)).thenReturn(Optional.of(new Technician()));
        when(machineRepository.findById(2L)).thenReturn(Optional.empty());


        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.createOrder(dto);
        });

        assertEquals("Machine not found", exception.getMessage());
        assertEquals(404, exception.getStatus());
    }

    @Test
    void shouldThrowExceptionIfIdTechnicianNotFound() {
        RequestServiceOrderDTO dto = new RequestServiceOrderDTO();
        dto.setTechnicianId(1L);
        dto.setMachineId(2L);


        when(technicianRepository.findById(1L)).thenReturn(Optional.empty());


        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.createOrder(dto);
        });

        assertEquals("Technician not found", exception.getMessage());
        assertEquals(404, exception.getStatus());
    }

    @Test
    void shouldCloseOrderSuccessfully() {

        ServiceOrder order = new ServiceOrder();
        order.setId(1L);
        order.setStatus(StatusServiceOrder.OPEN);
        order.setOpeningDate(LocalDateTime.now());

        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(repository.saveAndFlush(any(ServiceOrder.class))).thenReturn(order);

        ServiceOrder result = service.closeOrder(1L);

        assertNotNull(result.getClosingDate());
        assertEquals(StatusServiceOrder.FINISHED, result.getStatus());
        assertEquals(order.getOpeningDate(), result.getOpeningDate());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionIfIdServiceOrderNotFound() {

        Long idServiceOrder = 1L;
        ServiceOrder order = new ServiceOrder();
        order.setId(idServiceOrder);

        when(repository.findById(idServiceOrder)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.closeOrder(idServiceOrder);
        });

        assertEquals("Order not Found", exception.getMessage());
        assertEquals(404, exception.getStatus());
    }

    @Test
    void shouldThrowExceptionIfStatusOrderFinished() {

        ServiceOrder order = new ServiceOrder();
        order.setId(1L);
        order.setStatus(StatusServiceOrder.FINISHED);
        order.setOpeningDate(LocalDateTime.now());

        when(repository.findById(1L)).thenReturn(Optional.of(order));

        AlreadyModifiedException exception = assertThrows(AlreadyModifiedException.class, () -> {
            service.closeOrder(1L);
        });

        assertEquals("The order is already closed ", exception.getMessage());
        assertEquals(409, exception.getStatus());


    }

    @Test
    void mustSearchOrderById() {

        ServiceOrder order = new ServiceOrder();
        order.setName("Daniel Santos");
        order.setId(1L);

        Machine machine = new Machine();
        machine.setId(2L);
        order.setMachine(machine);

        Technician technician = new Technician();
        technician.setId(3L);
        order.setTechnician(technician);

        when(repository.findById(1L)).thenReturn(Optional.of(order));

        ResponseServiceOrderDTO result = service.findOrderById(1L);


        assertEquals(Long.valueOf(1L), result.getId());
        assertEquals("Daniel Santos", result.getName());
        assertEquals(Long.valueOf(2L), result.getMachineId());
        assertEquals(Long.valueOf(3L), result.getTechnicianId());


    }

    @Test
    void shouldThrowExceptionIfIdOrderNotFound() {
        Long idOrder = 1L;

        when(repository.findById(idOrder)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.findOrderById(idOrder);
        });

        assertEquals(" Service Order not found", exception.getMessage());
        assertEquals(404, exception.getStatus());
    }

    @Test
    void mustReturnAllOrders() {

        ServiceOrder order = new ServiceOrder();
        Machine machine = new Machine();
        machine.setId(2L);
        order.setMachine(machine);

        Technician technician = new Technician();
        technician.setId(3L);
        order.setTechnician(technician);

        when(repository.findAll()).thenReturn(List.of(order));

        List<ResponseServiceOrderDTO> list = service.allOrders();

        assertNotNull(list);
        assertEquals(1L, list.size());


    }

    @Test
    void shouldThrowExceptionIfEmpty() {

        ServiceOrder order = new ServiceOrder();
        Machine machine = new Machine();
        machine.setId(2L);
        order.setMachine(machine);

        Technician technician = new Technician();
        technician.setId(3L);
        order.setTechnician(technician);

        when(repository.findAll()).thenReturn(List.of());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.allOrders();
        });

        assertEquals("The Service List is Empty", exception.getMessage());
        assertEquals(404, exception.getStatus());

    }

    @Test
    void mustUpdateMaintenanceOrderIfIdExist() {

        ServiceOrder order = new ServiceOrder();
        order.setName("Daniel Santos");
        order.setMaintenanceType("Old maintenance");
        order.setProblemDescription("Motor queimado");
        order.setStatus(StatusServiceOrder.OPEN);
        order.setId(1L);

        Machine machine = new Machine();
        machine.setId(2L);
        order.setMachine(machine);

        Technician technician = new Technician();
        technician.setId(3L);
        order.setTechnician(technician);

        RequestServiceOrderDTO dto = new RequestServiceOrderDTO();
        dto.setTechnicianId(3L);
        dto.setMachineId(2L);
        dto.setMaintenanceType("new Maintenance");


        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(machineRepository.findById(2L)).thenReturn(Optional.of(machine));
        when(technicianRepository.findById(3L)).thenReturn(Optional.of(technician));
        when(repository.saveAndFlush(any(ServiceOrder.class))).thenReturn(order);

        ServiceOrder result = service.updateOrder(1L, dto);

        assertEquals("Daniel Santos", result.getName());
        assertEquals("new Maintenance", result.getMaintenanceType());
        assertEquals("Motor queimado", result.getProblemDescription());
        assertEquals(StatusServiceOrder.OPEN, result.getStatus());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).saveAndFlush(order);
        verify(machineRepository, times(1)).findById(2L);
        verify(technicianRepository, times(1)).findById(3L);

    }

    @Test
    void mustUpdateNameOrderIfIdExist() {

        ServiceOrder order = new ServiceOrder();
        order.setName("old Name");
        order.setMaintenanceType("Preventive");
        order.setProblemDescription("Motor queimado");
        order.setStatus(StatusServiceOrder.OPEN);
        order.setId(1L);

        Machine machine = new Machine();
        machine.setId(2L);
        order.setMachine(machine);

        Technician technician = new Technician();
        technician.setId(3L);
        order.setTechnician(technician);

        RequestServiceOrderDTO dto = new RequestServiceOrderDTO();
        dto.setTechnicianId(3L);
        dto.setMachineId(2L);
        dto.setName("new Name");


        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(machineRepository.findById(2L)).thenReturn(Optional.of(machine));
        when(technicianRepository.findById(3L)).thenReturn(Optional.of(technician));
        when(repository.saveAndFlush(any(ServiceOrder.class))).thenReturn(order);

        ServiceOrder result = service.updateOrder(1L, dto);

        assertEquals("new Name", result.getName());
        assertEquals("Preventive", result.getMaintenanceType());
        assertEquals("Motor queimado", result.getProblemDescription());
        assertEquals(StatusServiceOrder.OPEN, result.getStatus());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).saveAndFlush(order);
        verify(machineRepository, times(1)).findById(2L);
        verify(technicianRepository, times(1)).findById(3L);

    }

    @Test
    void mustUpdateProblemDescriptionOrderIfIdExist() {

        ServiceOrder order = new ServiceOrder();
        order.setName("Daniel Santos");
        order.setMaintenanceType("Preventive");
        order.setProblemDescription("Old Problem");
        order.setStatus(StatusServiceOrder.OPEN);
        order.setId(1L);

        Machine machine = new Machine();
        machine.setId(2L);
        order.setMachine(machine);

        Technician technician = new Technician();
        technician.setId(3L);
        order.setTechnician(technician);

        RequestServiceOrderDTO dto = new RequestServiceOrderDTO();
        dto.setTechnicianId(3L);
        dto.setMachineId(2L);
        dto.setProblemDescription("new Problem");


        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(machineRepository.findById(2L)).thenReturn(Optional.of(machine));
        when(technicianRepository.findById(3L)).thenReturn(Optional.of(technician));
        when(repository.saveAndFlush(any(ServiceOrder.class))).thenReturn(order);

        ServiceOrder result = service.updateOrder(1L, dto);

        assertEquals("Daniel Santos", result.getName());
        assertEquals("Preventive", result.getMaintenanceType());
        assertEquals("new Problem", result.getProblemDescription());
        assertEquals(StatusServiceOrder.OPEN, result.getStatus());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).saveAndFlush(order);
        verify(machineRepository, times(1)).findById(2L);
        verify(technicianRepository, times(1)).findById(3L);

    }

    @Test
    void mustUpdateStatusOrderIfIdExist() {

        ServiceOrder order = new ServiceOrder();
        order.setName("Daniel Santos");
        order.setMaintenanceType("Preventive");
        order.setProblemDescription("Motor queimado");
        order.setStatus(StatusServiceOrder.OPEN);
        order.setId(1L);

        Machine machine = new Machine();
        machine.setId(2L);
        order.setMachine(machine);

        Technician technician = new Technician();
        technician.setId(3L);
        order.setTechnician(technician);

        RequestServiceOrderDTO dto = new RequestServiceOrderDTO();
        dto.setTechnicianId(3L);
        dto.setMachineId(2L);
        dto.setStatus(StatusServiceOrder.IN_PROGRESS);


        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(machineRepository.findById(2L)).thenReturn(Optional.of(machine));
        when(technicianRepository.findById(3L)).thenReturn(Optional.of(technician));
        when(repository.saveAndFlush(any(ServiceOrder.class))).thenReturn(order);

        ServiceOrder result = service.updateOrder(1L, dto);

        assertEquals("Daniel Santos", result.getName());
        assertEquals("Preventive", result.getMaintenanceType());
        assertEquals("Motor queimado", result.getProblemDescription());
        assertEquals(StatusServiceOrder.IN_PROGRESS, result.getStatus());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).saveAndFlush(order);
        verify(machineRepository, times(1)).findById(2L);
        verify(technicianRepository, times(1)).findById(3L);

    }

    @Test
    void mustUpdateIdTechIfOrderIfIdExist() {

        ServiceOrder order = new ServiceOrder();
        order.setId(1L);


        Machine oldMachine = new Machine();
        oldMachine.setId(2L);
        order.setMachine(oldMachine);

        Technician oldTechnician = new Technician();
        oldTechnician.setId(3L);
        order.setTechnician(oldTechnician);


        Technician newTechnician = new Technician();
        newTechnician.setId(4L);


        RequestServiceOrderDTO dto = new RequestServiceOrderDTO();
        dto.setMachineId(null);
        dto.setTechnicianId(4L);

        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(technicianRepository.findById(4L)).thenReturn(Optional.of(newTechnician));
        when(repository.saveAndFlush(any(ServiceOrder.class))).thenReturn(order);

        ServiceOrder result = service.updateOrder(1L, dto);

        assertEquals(2L, result.getMachine().getId());
        assertEquals(4L, result.getTechnician().getId());

        verify(repository, times(1)).findById(1L);
        verify(machineRepository, never()).findById(any());
        verify(technicianRepository, times(1)).findById(4L);
        verify(repository, times(1)).saveAndFlush(order);

    }

    @Test
    void mustUpdateIdMachineIfOrderIfIdExist() {


        ServiceOrder order = new ServiceOrder();
        order.setId(1L);


        Machine oldMachine = new Machine();
        oldMachine.setId(2L);
        order.setMachine(oldMachine);

        Technician oldTechnician = new Technician();
        oldTechnician.setId(3L);
        order.setTechnician(oldTechnician);


        Machine newMachine = new Machine();
        newMachine.setId(4L);


        RequestServiceOrderDTO dto = new RequestServiceOrderDTO();
        dto.setMachineId(4L);
        dto.setTechnicianId(null);

        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(machineRepository.findById(4L)).thenReturn(Optional.of(newMachine));
        when(repository.saveAndFlush(any(ServiceOrder.class))).thenReturn(order);

        ServiceOrder result = service.updateOrder(1L, dto);

        assertEquals(4L, result.getMachine().getId());
        assertEquals(3L, result.getTechnician().getId());

        verify(repository, times(1)).findById(1L);
        verify(machineRepository, times(1)).findById(4L);
        verify(technicianRepository, never()).findById(any());
        verify(repository, times(1)).saveAndFlush(order);
    }

    @Test
    void shouldThrowExceptionIfTechnicianNotFound() {

        Long idTech = 1L;
        RequestServiceOrderDTO dto = new RequestServiceOrderDTO();

        dto.setTechnicianId(idTech);

        ServiceOrder order = new ServiceOrder();
        order.setId(2L);


        when(repository.findById(2L)).thenReturn(Optional.of(order));


        when(technicianRepository.findById(idTech)).thenReturn(Optional.empty());


        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.updateOrder(2L, dto);
        });

        assertEquals("Technician not found", exception.getMessage());
        assertEquals(404, exception.getStatus());


    }

    @Test
    void shouldThrowExceptionIfMachineNotFound() {

        Long idMachine = 1L;
        RequestServiceOrderDTO dto = new RequestServiceOrderDTO();

        dto.setMachineId(idMachine);

        ServiceOrder order = new ServiceOrder();
        order.setId(2L);


        when(repository.findById(2L)).thenReturn(Optional.of(order));


        when(machineRepository.findById(idMachine)).thenReturn(Optional.empty());


        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.updateOrder(2L, dto);
        });

        assertEquals("Machine not found", exception.getMessage());
        assertEquals(404, exception.getStatus());


    }

    @Test
    void mustDeleteOrderSuccessfully() {


        service.deleteOrder(anyLong());


        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldReturnOrderStatus() {

        Long idServiceOrder = 1L;

        ServiceOrder order = new ServiceOrder();
        order.setStatus(StatusServiceOrder.OPEN);
        order.setProblemDescription("Maquina quebrada");
        order.setId(idServiceOrder);

        Machine machine = new Machine();
        machine.setId(2L);
        order.setMachine(machine);

        Technician technician = new Technician();
        technician.setId(3L);
        order.setTechnician(technician);

        when(repository.findById(idServiceOrder)).thenReturn(Optional.of(order));

        OrderServiceStatusDTO dto = new OrderServiceStatusDTO();
        dto.setId(1L);
        dto.setStatusServiceOrder(StatusServiceOrder.OPEN);
        dto.setTechnicianId(3L);
        dto.setMachineId(2L);
        dto.setProblemDescription("Maquina quebrada");


        OrderServiceStatusDTO result = service.veryOrderStatus(idServiceOrder);

        assertEquals(StatusServiceOrder.OPEN, result.getStatusServiceOrder());
        assertEquals("Maquina quebrada", result.getProblemDescription());
        assertEquals(3L, result.getTechnicianId());
        assertEquals(2L, result.getMachineId());

        verify(repository, times(1)).findById(idServiceOrder);


    }

    @Test
    void mustExecutedService() {

        Long idServiceOrder = 1L;

        ServiceOrder order = new ServiceOrder();
        order.setStatus(StatusServiceOrder.OPEN);


        Machine machine = new Machine();
        machine.setId(2L);
        order.setMachine(machine);

        Technician technician = new Technician();
        technician.setId(3L);
        order.setTechnician(technician);


        when(repository.findById(idServiceOrder)).thenReturn(Optional.of(order));
        when(repository.saveAndFlush(any(ServiceOrder.class))).thenReturn(order);


        service.executeService(idServiceOrder);

        assertEquals(StatusServiceOrder.IN_PROGRESS, order.getStatus());


        verify(repository, times(1)).findById(idServiceOrder);


    }

    @Test
    void mustFinishedService() {

        Long idServiceOrder = 1L;

        ServiceOrder order = new ServiceOrder();
        order.setStatus(StatusServiceOrder.OPEN);


        Machine machine = new Machine();
        machine.setId(2L);
        order.setMachine(machine);

        Technician technician = new Technician();
        technician.setId(3L);
        order.setTechnician(technician);


        when(repository.findById(idServiceOrder)).thenReturn(Optional.of(order));
        when(repository.saveAndFlush(any(ServiceOrder.class))).thenReturn(order);


        service.finishService(idServiceOrder);

        assertEquals(StatusServiceOrder.FINISHED, order.getStatus());


        verify(repository, times(1)).findById(idServiceOrder);

    }

    @Test
    void mustCanceledOrder() {

        Long idServiceOrder = 1L;

        ServiceOrder order = new ServiceOrder();
        order.setStatus(StatusServiceOrder.OPEN);


        Machine machine = new Machine();
        machine.setId(2L);
        order.setMachine(machine);

        Technician technician = new Technician();
        technician.setId(3L);
        order.setTechnician(technician);


        when(repository.findById(idServiceOrder)).thenReturn(Optional.of(order));
        when(repository.saveAndFlush(any(ServiceOrder.class))).thenReturn(order);


        service.canceledOrder(idServiceOrder);

        assertEquals(StatusServiceOrder.CANCELED, order.getStatus());


        verify(repository, times(1)).findById(idServiceOrder);

    }

    @Test
    void shouldReturnOrdersByGivenStatus() {

        ServiceOrder order1 = new ServiceOrder();
        order1.setId(1L);
        order1.setStatus(StatusServiceOrder.OPEN);

        ServiceOrder order2 = new ServiceOrder();
        order2.setId(2L);
        order2.setStatus(StatusServiceOrder.FINISHED);

        ServiceOrder order3 = new ServiceOrder();
        order3.setId(3L);
        order3.setStatus(StatusServiceOrder.OPEN);

        List<ServiceOrder> mockOrders = List.of(order1, order2, order3);


        when(repository.findAll()).thenReturn(mockOrders);


        List<ResponseServiceOrderDTO> result = service.findOrderByStatus("OPEN");


        assertEquals(2, result.size());
    }


    @Test
    void shoudThrowExceptionIfGetFindByIdNotFound() {


        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.canceledOrder(anyLong());
        });

        assertEquals("Service Order not found", exception.getMessage());
        assertEquals(404, exception.getStatus());
    }

}