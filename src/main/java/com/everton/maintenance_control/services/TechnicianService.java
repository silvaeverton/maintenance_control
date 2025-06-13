package com.everton.maintenance_control.services;

import com.everton.maintenance_control.dtos.ResponseServiceOrderDTO;
import com.everton.maintenance_control.dtos.TechnicianRequestDTO;
import com.everton.maintenance_control.dtos.TechnicianResponseDTO;
import com.everton.maintenance_control.model.Technician;

import java.util.List;

public interface TechnicianService {

    public Technician createTech(TechnicianRequestDTO dto);
    public TechnicianResponseDTO findTechById(Long idTech);
    public List<TechnicianResponseDTO> allTech();
    public Technician updateTech(Long idTech, TechnicianRequestDTO requestDTO);
    public void deleteTech(Long idTech);
    public List<ResponseServiceOrderDTO> findOrderByTech(Long idTech);
}
