package com.everton.maintenance_control.controllers;

import com.everton.maintenance_control.dtos.OrderServiceStatusDTO;
import com.everton.maintenance_control.dtos.RequestServiceOrderDTO;
import com.everton.maintenance_control.dtos.ResponseServiceOrderDTO;
import com.everton.maintenance_control.enums.StatusServiceOrder;
import com.everton.maintenance_control.model.ServiceOrder;
import com.everton.maintenance_control.model.Technician;
import com.everton.maintenance_control.services.ServiceOrderService;
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
class ServiceOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServiceOrderService service;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void shouldCreateOrder() throws Exception {

        RequestServiceOrderDTO requestDTO = new RequestServiceOrderDTO();
        requestDTO.setName("Order Test");

        ServiceOrder createdOrder = new ServiceOrder();
        createdOrder.setId(1L);
        createdOrder.setName("Order Test");

        when(service.createOrder(any(RequestServiceOrderDTO.class))).thenReturn(createdOrder);

        mockMvc.perform(post("http://localhost:8080/serviceOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Order Test"));
    }

    @Test
    void shouldFindOrderById() throws Exception {

        ServiceOrder order = new ServiceOrder();
        order.setId(10L);
        order.setName("Order");

        ResponseServiceOrderDTO orderDTO = new ResponseServiceOrderDTO();
        orderDTO.setId(10L);
        order.setName("Order");

        when(service.findOrderById(10L)).thenReturn(orderDTO);

        mockMvc.perform(get("http://localhost:8080/serviceOrder/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));


    }

    @Test
    void shouldGetAllOrders() throws Exception {
        ResponseServiceOrderDTO dto = new ResponseServiceOrderDTO();
        dto.setId(1L);
        dto.setName("Test Order");
        List<ResponseServiceOrderDTO> list = List.of(dto);

        when(service.allOrders()).thenReturn(list);

        mockMvc.perform(get("/serviceOrder"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test Order"));
    }


    @Test
    void shouldUpdateOrder() throws Exception {
        RequestServiceOrderDTO requestDTO = new RequestServiceOrderDTO();
        requestDTO.setTechnicianId(3L);

        ServiceOrder updatedOrder = new ServiceOrder();
        updatedOrder.setId(10L);
        updatedOrder.setTechnician(new Technician());
        updatedOrder.getTechnician().setId(3L);

        when(service.updateOrder(eq(10L), any(RequestServiceOrderDTO.class))).thenReturn(updatedOrder);

        mockMvc.perform(put("http://loclhost:8080/serviceOrder/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.technician.id").value(3));
    }

    @Test
    void shouldDeleteOrder() throws Exception {
        doNothing().when(service).deleteOrder(10L);

        mockMvc.perform(delete("/serviceOrder/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldVeryOrderStatus() throws Exception {

        Long idOrder = 10L;

        OrderServiceStatusDTO dto = new OrderServiceStatusDTO();
        dto.setId(10L);
        dto.setStatusServiceOrder(StatusServiceOrder.OPEN);

        when(service.veryOrderStatus(idOrder)).thenReturn(dto);

        mockMvc.perform(get("http://localhost:8080/serviceOrder/{idOrder}/status",idOrder))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.statusServiceOrder").value("OPEN"));
    }

    @Test
    void shouldExecuteService() throws Exception {

        Long idOrder = 1L;

        doNothing().when(service).executeService(idOrder);

        mockMvc.perform(patch("http://localhost:8080/serviceOrder/{idOrder}",idOrder))
                .andExpect(status().isOk());

    }

    @Test
    void shouldFinishService() throws Exception {

        Long idOrder = 1L;

        doNothing().when(service).finishService(idOrder);

        mockMvc.perform(patch("http://localhost:8080/serviceOrder/{idOrder}/finish", idOrder))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCanceledOrder() throws Exception {

        Long idOder = 1L;

        doNothing().when(service).canceledOrder(idOder);

        mockMvc.perform(patch("http://localhost:8080/serviceOrder/{idOrder}/cancellation", idOder))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOderByStatus() throws Exception {

        ResponseServiceOrderDTO orderDTO = new ResponseServiceOrderDTO();
        orderDTO.setId(10L);
        orderDTO.setStatus(StatusServiceOrder.OPEN);

        List<ResponseServiceOrderDTO> list = List.of(orderDTO);

        when(service.findOrderByStatus("OPEN")).thenReturn(list);

        mockMvc.perform(get("http://localhost:8080/serviceOrder/statusOrder")
                        .param("status","OPEN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("OPEN"))
                .andExpect(jsonPath("$[0].id").value(10L));

    }
}