package com.everton.maintenance_control.controllers;

import com.everton.maintenance_control.dtos.MachineRequestDTO;
import com.everton.maintenance_control.dtos.MachineResponseDTO;
import com.everton.maintenance_control.dtos.ResponseServiceOrderDTO;
import com.everton.maintenance_control.enums.StatusMachine;
import com.everton.maintenance_control.model.Machine;
import com.everton.maintenance_control.services.MachineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MachineControllerTest {


    @Autowired
    private  MockMvc mockMvc;

    @MockitoBean
    private MachineService service;

    @Autowired
    private  ObjectMapper objectMapper;

    @Test
    void shouldCreateMachine() throws Exception {

        MachineRequestDTO request = new MachineRequestDTO();
        request.setManufacturer("Siemens");
        request.setSector("Montagem");


        Machine machine = new Machine();
    machine.setManufacturer("Siemens");
        machine.setSector("Montagem");


        when(service.createMachine(any(MachineRequestDTO.class))).thenReturn(machine);

        mockMvc.perform(post("/machine")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.manufacturer").value("Siemens"))
                .andExpect(jsonPath("$.sector").value("Montagem"));
    }

    @Test
    void shouldSearchMachineById() throws Exception {

        Long idMachine = 1L;

        Machine machine = new Machine();
        machine.setName("Machine Expected");
        machine.setId(1L);

        MachineResponseDTO dto = new MachineResponseDTO();
        dto.setName("Machine Expected");
        dto.setId(1L);

        when(service.findMachineById(idMachine)).thenReturn(dto);

        mockMvc.perform(get("/machine/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Machine Expected"));

    }

    @Test
    void shouldReturnAllMachines() throws Exception {

        MachineResponseDTO dto = new MachineResponseDTO();
        dto.setId(1L);
        dto.setName("Test Machine");

        List<MachineResponseDTO> list = List.of(dto);

        when(service.allMachine()).thenReturn(list);

        mockMvc.perform(get("/machine")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test Machine"));

    }

    @Test
    void shouldUpdateMachine() throws Exception {

        MachineRequestDTO dto = new MachineRequestDTO();
        dto.setId(1L);
        dto.setStatusMachine(StatusMachine.ACTIVE);
        dto.setManufacturer("Siemens");
        dto.setSector("Montagem");



        Machine machine = new Machine();
        machine.setId(1L);
        machine.setManufacturer("Siemens");

        when(service.updateMachine(eq(1L),any(MachineRequestDTO.class))).thenReturn(machine);

        mockMvc.perform(put("/machine/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.manufacturer").value("Siemens"))
                .andExpect(jsonPath("$.id").value(1L));



    }

    @Test
    void shouldDeleteMachine() throws Exception {
       Long idMachine = 1L;

        doNothing().when(service).deleteMachine(idMachine);

        mockMvc.perform(delete("/machine/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnOrderByMachine() throws Exception {

        Long idMachine = 1L;


        ResponseServiceOrderDTO orderDTO = new ResponseServiceOrderDTO();
        orderDTO.setId(10L);

        List<ResponseServiceOrderDTO> orders = List.of(orderDTO);

        when(service.findOrderByMachine(idMachine)).thenReturn(orders);

        mockMvc.perform(get("http://localhost:8080/machine/order/{idMachine}", idMachine)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(10L));
    }
}