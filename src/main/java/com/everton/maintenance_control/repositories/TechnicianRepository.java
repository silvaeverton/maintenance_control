package com.everton.maintenance_control.repositories;

import com.everton.maintenance_control.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician,Long> {
}
