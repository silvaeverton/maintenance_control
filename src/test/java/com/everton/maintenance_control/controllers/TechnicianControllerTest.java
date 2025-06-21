package com.everton.maintenance_control.controllers;

import com.everton.maintenance_control.dtos.ResponseServiceOrderDTO;
import com.everton.maintenance_control.dtos.TechnicianRequestDTO;
import com.everton.maintenance_control.dtos.TechnicianResponseDTO;
import com.everton.maintenance_control.model.Technician;
import com.everton.maintenance_control.services.TechnicianService;
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
class TechnicianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TechnicianService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateTech() throws Exception {

        TechnicianRequestDTO dto = new TechnicianRequestDTO();
        dto.setName("New Tech");

        Technician technician = new Technician();
        technician.setId(10L);
        technician.setName("New Tech");

        when(service.createTech(any(TechnicianRequestDTO.class))).thenReturn(technician);

        mockMvc.perform(post("/technician")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Tech"))
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    void shouldSearchTechById() throws Exception {
        Long idTech = 10L;

        TechnicianResponseDTO responseDTO = new TechnicianResponseDTO();
        responseDTO.setId(10L);
        responseDTO.setName("Tech Expected");

        when(service.findTechById(idTech)).thenReturn(responseDTO);

        mockMvc.perform(get("/technician/{idTech}",idTech))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tech Expected"))
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    void shouldReturnAllTech() throws Exception {

        TechnicianResponseDTO responseDTO = new TechnicianResponseDTO();
        responseDTO.setRegistration("Test");
        responseDTO.setId(10L);

        List<TechnicianResponseDTO> list = List.of(responseDTO);

        when(service.allTech()).thenReturn(list);

        mockMvc.perform(get("/technician")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].registration").value("Test"))
                .andExpect(jsonPath("$[0].id").value(10L));
    }

    @Test
    void shouldUpdateTech() throws Exception {
        Long idTech = 10L;
        TechnicianRequestDTO requestDTO = new TechnicianRequestDTO();
        requestDTO.setName("Update Tech");
        requestDTO.setId(10L);

        Technician technician = new Technician();
        technician.setName("Update Tech");
        requestDTO.setId(10L);

        when(service.updateTech(eq(10L),any(TechnicianRequestDTO.class))).thenReturn(technician);

        mockMvc.perform(put("/technician/{idTech}",idTech)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Update Tech"));

    }

    @Test
    void shouldDeleteTech() throws Exception {

        Long idTech = 10L;

        doNothing().when(service).deleteTech(idTech);

        mockMvc.perform(delete("/technician/{idTech}", idTech))
                .andExpect(status().isNoContent());


    }

    @Test
    void shouldSearchOrderByTech() throws Exception {

        Long idTech = 1L;

        ResponseServiceOrderDTO response = new ResponseServiceOrderDTO();
        response.setId(10L);

        List<ResponseServiceOrderDTO> list = List.of(response);

        when(service.findOrderByTech(idTech)).thenReturn(list);

        mockMvc.perform(get("/technician/order/{idTech}",idTech)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].size()").value(10))
                .andExpect(jsonPath("$[0].id").value(10));

    }
}