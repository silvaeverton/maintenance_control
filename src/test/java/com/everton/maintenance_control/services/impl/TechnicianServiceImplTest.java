package com.everton.maintenance_control.services.impl;

import com.everton.maintenance_control.dtos.ResponseServiceOrderDTO;
import com.everton.maintenance_control.dtos.TechnicianRequestDTO;
import com.everton.maintenance_control.dtos.TechnicianResponseDTO;
import com.everton.maintenance_control.enums.StatusServiceOrder;
import com.everton.maintenance_control.exceptions.custom.NotFoundException;
import com.everton.maintenance_control.model.ServiceOrder;
import com.everton.maintenance_control.model.Technician;
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
class TechnicianServiceImplTest {

    @InjectMocks
    TechnicianServiceImpl technicianService;

    @Mock
    TechnicianRepository repository;

    @Mock
    ServiceOrderRepository serviceOrderRepository;

    @Test
    void mustCreateTech() {

        TechnicianRequestDTO requestDTO = new TechnicianRequestDTO();
        requestDTO.setName("Daniel Santos");
        requestDTO.setId(1L);

        Technician tech = new Technician();
        tech.setName("Daniel Santos");
        tech.setId(1L);

        when(repository.saveAndFlush(any(Technician.class))).thenReturn(tech);

        Technician result = technicianService.createTech(requestDTO);

        assertNotNull(result);
        assertEquals("Daniel Santos",result.getName());
        assertEquals(1L,result.getId());

        verify(repository, times(1)).saveAndFlush(any(Technician.class));



    }

    @Test
    void shouldReturnTechIfIdExist() {

        Long idTech = 1L;
        Technician techExistent = new Technician();
        techExistent.setId(idTech);
        techExistent.setName("Daniel Santos");

        when(repository.findById(idTech)).thenReturn(Optional.of(techExistent));

        TechnicianResponseDTO result = technicianService.findTechById(idTech);

        assertNotNull(result);
        assertEquals(1L,result.getId());
        assertEquals("Daniel Santos",result.getName());

        verify(repository, times(1)).findById(idTech);
    }

    @Test
    void shouldThrowExceptionIfIdTechNotExist() {

        Long idTech = 1L;
        when(repository.findById(idTech)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            technicianService.findTechById(idTech);
        });


    }

    @Test
    void mustReturnTechList() {
        when(repository.findAll()).thenReturn(List.of(new Technician()));

        List<TechnicianResponseDTO> result = technicianService.allTech();

        assertNotNull(result);
        assertEquals(1,result.size());

        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldThrowExceptionIfListEmpty() {

        when(repository.findAll()).thenReturn(List.of());
        assertThrows(NotFoundException.class, () -> {
            technicianService.allTech();
        });


    }

    @Test
    void mustUpdateTechIfIdExist() {
        Long idTech = 1L;
        Technician technicianExistent = new Technician();
        technicianExistent.setName("Daniel Santos");
        technicianExistent.setRegistration("TEC001");
        technicianExistent.setId(idTech);

        TechnicianRequestDTO dto = new TechnicianRequestDTO();
        dto.setName("Daniel Santos Pereira");
        dto.setRegistration("TEC001");
        dto.setId(idTech);

        when(repository.findById(idTech)).thenReturn(Optional.of(technicianExistent));
        when(repository.saveAndFlush(any(Technician.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Technician result = technicianService.updateTech(idTech,dto);

        assertNotNull(result);
        assertEquals("Daniel Santos Pereira", result.getName());
        assertEquals("TEC001", result.getRegistration());
        assertEquals(1L,result.getId());

        verify(repository, times(1)).findById(idTech);
        verify(repository, times(1)).saveAndFlush(technicianExistent);

    }
    @Test
    void shouldUpdateOnlyName() {
        Long idTech = 1L;
        Technician technicianExistent = new Technician();
        technicianExistent.setName("old name");
        technicianExistent.setRegistration("TEC001");
        technicianExistent.setId(idTech);

        TechnicianRequestDTO dto = new TechnicianRequestDTO();
        dto.setName("new Name");

        when(repository.findById(idTech)).thenReturn(Optional.of(technicianExistent));
        when(repository.saveAndFlush(any(Technician.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Technician updateTech = technicianService.updateTech(idTech,dto);

        assertEquals("new Name", updateTech.getName());
        assertEquals("TEC001", updateTech.getRegistration());

        verify(repository,times(1)).saveAndFlush(technicianExistent);




    }

    @Test
    void shouldUpdateOnlyRegistration() {
        Long idTech = 1L;
        Technician technicianExistent = new Technician();
        technicianExistent.setName("Daniel Santos");
        technicianExistent.setRegistration("Old Registration");
        technicianExistent.setId(idTech);

        TechnicianRequestDTO dto = new TechnicianRequestDTO();
        dto.setRegistration("new Registration");

        when(repository.findById(idTech)).thenReturn(Optional.of(technicianExistent));
        when(repository.saveAndFlush(any(Technician.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Technician updateTech = technicianService.updateTech(idTech,dto);

        assertEquals("Daniel Santos", updateTech.getName());
        assertEquals("new Registration", updateTech.getRegistration());

        verify(repository,times(1)).saveAndFlush(technicianExistent);




    }

    @Test
    void mustToThrowExceptionWhenTechNotFound() {

        Long idTech = 99L;
        TechnicianRequestDTO dto = new TechnicianRequestDTO();
        dto.setName("Tech not exist");

        when(repository.findById(idTech)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, ()-> {
            technicianService.updateTech(idTech,dto);
        });

        verify(repository, times(1)).findById(idTech);
        verify(repository, never()).saveAndFlush(any());
    }

    @Test
    void mustDeleteTech() {

        technicianService.deleteTech(anyLong());

        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void mustReturnListOrderByTech() {
        Long idTech = 1L;

        ServiceOrder order = new ServiceOrder();
        order.setId(100L);
        order.setName("Daniel Santos");
        order.setMaintenanceType("Manutenção ");
        order.setStatus(StatusServiceOrder.IN_PROGRESS);
        order.setOpeningDate(LocalDateTime.now());

        List<ServiceOrder> orderList = List.of(order);

        when(serviceOrderRepository.findByTechId(idTech)).thenReturn(orderList);

        List<ResponseServiceOrderDTO> result = technicianService.findOrderByTech(idTech);

        assertNotNull(result);
        assertEquals(1,result.size());
        ResponseServiceOrderDTO dto = result.get(0);
        assertEquals(order.getId(),dto.getId());
        assertEquals(order.getName(),dto.getName());
        assertEquals(order.getMaintenanceType(),dto.getMaintenanceType());
        assertEquals(order.getStatus(),dto.getStatus());
        assertEquals(order.getOpeningDate(),dto.getOpeningDate());

        verify(serviceOrderRepository,times(1)).findByTechId(idTech);


    }
}