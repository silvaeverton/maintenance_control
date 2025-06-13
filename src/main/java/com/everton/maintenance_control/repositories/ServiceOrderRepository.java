package com.everton.maintenance_control.repositories;

import com.everton.maintenance_control.model.ServiceOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder,Long> {

    @Query("SELECT o FROM ServiceOrder o JOIN FETCH o.machine WHERE o.machine.id = :machineId")
    List<ServiceOrder> findByMachineId(Long machineId);

    @Query("SELECT o FROM ServiceOrder o JOIN FETCH o.machine WHERE o.technician.id = :technicianId")
    List<ServiceOrder> findByTechId(Long technicianId);

    @Query("SELECT o FROM ServiceOrder o JOIN FETCH o.technician")
    List<ServiceOrder> findAllWithTechnician();

    @Query("SELECT o FROM ServiceOrder o JOIN FETCH o.technician t JOIN FETCH o.machine m")
    List<ServiceOrder> findAllWithTechnicianAndMachine();

    @EntityGraph(attributePaths = {"technician", "machine"})
    Optional<ServiceOrder> findWithTechnicianAndMachineById(Long id);
}
