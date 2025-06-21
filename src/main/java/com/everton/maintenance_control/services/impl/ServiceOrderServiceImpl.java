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
import com.everton.maintenance_control.services.ServiceOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceOrderServiceImpl implements ServiceOrderService {

    private final ServiceOrderRepository repository;
    private final TechnicianRepository technicianRepository;
    private final MachineRepository machineRepository;


    @Override
    public ServiceOrder createOrder(RequestServiceOrderDTO dto) {


        Technician technician = technicianRepository.findById(dto.getTechnicianId())
                .orElseThrow(() -> new NotFoundException("Technician not found", 404));
        Machine machine = machineRepository.findById(dto.getMachineId())
                .orElseThrow(() -> new NotFoundException("Machine not found", 404));

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setName(dto.getName());
        serviceOrder.setOpeningDate(LocalDateTime.now());
        serviceOrder.setClosingDate(null);
        serviceOrder.setProblemDescription(dto.getProblemDescription());
        serviceOrder.setMaintenanceType(dto.getMaintenanceType());
        serviceOrder.setTechnician(technician);
        serviceOrder.setMachine(machine);
        serviceOrder.setStatus(StatusServiceOrder.OPEN);

        return repository.saveAndFlush(serviceOrder);
    }

    @Override
    public ServiceOrder closeOrder(Long idOrder) {
      ServiceOrder order =  repository.findById(idOrder).orElseThrow(
                ()-> new NotFoundException("Order not Found",404));

        if(order.getStatus() == StatusServiceOrder.FINISHED) {
            throw new AlreadyModifiedException("The order is already closed ",409);
        }
        order.setClosingDate(LocalDateTime.now());
        order.setStatus(StatusServiceOrder.FINISHED);
        return repository.saveAndFlush(order);
    }

    @Override
    public ResponseServiceOrderDTO findOrderById(Long idOrder) {
       ServiceOrder order = repository.findById(idOrder).orElseThrow(
               ()-> new NotFoundException(" Service Order not found",404));

       ResponseServiceOrderDTO dto = new ResponseServiceOrderDTO();
       dto.setId(order.getId());
       dto.setName(order.getName());
       dto.setOpeningDate(order.getOpeningDate());
       dto.setOpeningDate(order.getOpeningDate());
       dto.setMachineId(order.getMachine().getId());
       dto.setTechnicianId(order.getTechnician().getId());
       dto.setProblemDescription(order.getProblemDescription());
       dto.setMaintenanceType(order.getMaintenanceType());


        return dto;
    }

    @Override
    public List<ResponseServiceOrderDTO> allOrders() {
        List<ServiceOrder> list = repository.findAll();
        if(list.isEmpty()) {
            throw new NotFoundException("The Service List is Empty",404);
        }
       return list.stream().map(order -> {
            ResponseServiceOrderDTO dto = new ResponseServiceOrderDTO();
            dto.setId(order.getId());
            dto.setName(order.getName());
            dto.setOpeningDate(order.getOpeningDate());
            dto.setOpeningDate(order.getOpeningDate());
            dto.setMachineId(order.getMachine().getId());
            dto.setTechnicianId(order.getTechnician().getId());
            dto.setProblemDescription(order.getProblemDescription());
            dto.setMaintenanceType(order.getMaintenanceType());
            return dto;
        }).toList();

    }

    @Override
    public ServiceOrder updateOrder(Long idOrder, RequestServiceOrderDTO updateOrder) {
        ServiceOrder orderExist = getServiceOrderById(idOrder);

        Machine machine = null;
        if (updateOrder.getMachineId() != null) {
            machine = machineRepository.findById(updateOrder.getMachineId())
                    .orElseThrow(() -> new NotFoundException("Machine not found", 404));
        }

        Technician technician = null;
        if (updateOrder.getTechnicianId() != null) {
            technician = technicianRepository.findById(updateOrder.getTechnicianId())
                    .orElseThrow(() -> new NotFoundException("Technician not found", 404));
        }

        if (updateOrder.getName() != null) {
            orderExist.setName(updateOrder.getName());
        }
        if (updateOrder.getStatus() != null) {
            orderExist.setStatus(updateOrder.getStatus());
        }
        if (updateOrder.getProblemDescription() != null) {
            orderExist.setProblemDescription(updateOrder.getProblemDescription());
        }
        if (updateOrder.getMaintenanceType() != null) {
            orderExist.setMaintenanceType(updateOrder.getMaintenanceType());
        }
        if (updateOrder.getMachineId() != null) {
            orderExist.setMachine(machine);
        }
        if (updateOrder.getTechnicianId() != null) {
            orderExist.setTechnician(technician);
        }

        return repository.saveAndFlush(orderExist);
    }

    @Override
    public void deleteOrder(Long idOrder) {

        repository.deleteById(idOrder);

    }

    @Override
    public OrderServiceStatusDTO veryOrderStatus(Long idOrder) {
        ServiceOrder serviceOrder = getServiceOrderById(idOrder);

        OrderServiceStatusDTO dto = new OrderServiceStatusDTO();
       dto.setId(serviceOrder.getId());
       dto.setStatusServiceOrder(serviceOrder.getStatus());
       dto.setMachineId(serviceOrder.getMachine().getId());
       dto.setTechnicianId(serviceOrder.getTechnician().getId());
       dto.setProblemDescription(serviceOrder.getProblemDescription());


        return dto;
    }

    @Override
    public void executeService(Long idOrder) {
        ServiceOrder serviceOrder = getServiceOrderById(idOrder);

        serviceOrder.setStatus(StatusServiceOrder.IN_PROGRESS);

        repository.saveAndFlush(serviceOrder);

    }

    @Override
    public void finishService(Long idOrder) {
        ServiceOrder serviceOrder = getServiceOrderById(idOrder);

        serviceOrder.setStatus(StatusServiceOrder.FINISHED);

        repository.saveAndFlush(serviceOrder);
    }

    @Override
    public void canceledOrder(Long idOrder) {
        ServiceOrder serviceOrder = getServiceOrderById(idOrder);

        serviceOrder.setStatus(StatusServiceOrder.CANCELED);

        repository.saveAndFlush(serviceOrder);

    }

    @Override
    public List<ResponseServiceOrderDTO> findOrderByStatus(String status) {
        List<ServiceOrder> orderList = repository.findAll();

       return orderList.stream().filter( order -> order.getStatus().name().equalsIgnoreCase(status))
                .map(serviceOrder -> new ResponseServiceOrderDTO())
                .toList();



    }


    private ServiceOrder getServiceOrderById(Long idOrder) {
        return repository.findById(idOrder).orElseThrow(()->
                new NotFoundException("Service Order not found",404));
    }


}
