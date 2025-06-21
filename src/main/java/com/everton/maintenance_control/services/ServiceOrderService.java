package com.everton.maintenance_control.services;

import com.everton.maintenance_control.dtos.OrderServiceStatusDTO;
import com.everton.maintenance_control.dtos.RequestServiceOrderDTO;
import com.everton.maintenance_control.dtos.ResponseServiceOrderDTO;
import com.everton.maintenance_control.model.ServiceOrder;

import java.util.List;

public interface ServiceOrderService {

    public ServiceOrder createOrder(RequestServiceOrderDTO dto);
    public ServiceOrder closeOrder(Long idOrder);
    public ResponseServiceOrderDTO findOrderById(Long idOrder);
    public List<ResponseServiceOrderDTO> allOrders();
    public ServiceOrder updateOrder(Long idOrder,RequestServiceOrderDTO updateOrder);
    public void deleteOrder(Long idOrder);
    public OrderServiceStatusDTO veryOrderStatus(Long idOrder);
    public void executeService(Long idOrder);
    public void finishService(Long idOrder);
    public void canceledOrder(Long idOrder);
    public List<ResponseServiceOrderDTO> findOrderByStatus(String status);


}
