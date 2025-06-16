package com.everton.maintenance_control.services.impl;

import com.everton.maintenance_control.dtos.ResponseServiceOrderDTO;
import com.everton.maintenance_control.dtos.TechnicianRequestDTO;
import com.everton.maintenance_control.dtos.TechnicianResponseDTO;
import com.everton.maintenance_control.exceptions.custom.NotFoundException;
import com.everton.maintenance_control.model.ServiceOrder;
import com.everton.maintenance_control.model.Technician;
import com.everton.maintenance_control.repositories.ServiceOrderRepository;
import com.everton.maintenance_control.repositories.TechnicianRepository;
import com.everton.maintenance_control.services.TechnicianService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnicianServiceImpl implements TechnicianService {

    private final TechnicianRepository repository;

    private final ServiceOrderRepository serviceOrderRepository;

    @Override
    public Technician createTech(TechnicianRequestDTO dto) {

        Technician technician = new Technician();
        technician.setName(dto.getName());
        technician.setRegistration(dto.getRegistration());

        return repository.saveAndFlush(technician);

    }

    @Override
    public TechnicianResponseDTO findTechById(Long idTech) {

        Technician technician = repository.findById(idTech).orElseThrow(
                ()-> new NotFoundException("Technician not found", 404));

        TechnicianResponseDTO techDto = new TechnicianResponseDTO();
        techDto.setId(technician.getId());
        techDto.setName(technician.getName());
        techDto.setRegistration(technician.getRegistration());


        return techDto;
    }

    @Override
    public List<TechnicianResponseDTO> allTech() {
        List<Technician> techniciansList = repository.findAll();
        if(techniciansList.isEmpty()) {
            throw new NotFoundException("The Technician list is Empty", 404);
        }
        return techniciansList.stream().map( technician -> {
            TechnicianResponseDTO responseDTO = new TechnicianResponseDTO();
            responseDTO.setId(technician.getId());
            responseDTO.setName(technician.getName());
            responseDTO.setRegistration(technician.getRegistration());
            return responseDTO;
        }).toList();

    }

    @Override
    public Technician updateTech(Long idTech, TechnicianRequestDTO requestDTO) {
        Technician technicianExist = getTechEntityById(idTech);

        if(requestDTO.getName() != null) {
            technicianExist.setName(requestDTO.getName());
        } if(requestDTO.getRegistration() != null) {
            technicianExist.setRegistration(requestDTO.getRegistration());
        }

        return repository.saveAndFlush(technicianExist);
    }

    @Override
    public void deleteTech(Long idTech) {

        repository.deleteById(idTech);

    }

    @Override
    public List<ResponseServiceOrderDTO> findOrderByTech(Long idTech) {
        List<ServiceOrder> list = serviceOrderRepository.findByTechId(idTech);


        return list.stream()
                .map(order ->{
                    ResponseServiceOrderDTO o = new ResponseServiceOrderDTO();
                    o.setMachineId(order.getId());
                    o.setName(order.getName());
                    o.setId(order.getId());
                    o.setMaintenanceType(order.getMaintenanceType());
                    o.setProblemDescription(order.getProblemDescription());
                    o.setTechnicianId(o.getTechnicianId());
                    o.setCreateDate(order.getCreateDate());
                    o.setScheduling(order.getScheduling());
                    o.setStatus(order.getStatus());

                    return o;


                }).toList();



    }

    private Technician getTechEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Technician not found with id: " + id, 404));
    }
}
