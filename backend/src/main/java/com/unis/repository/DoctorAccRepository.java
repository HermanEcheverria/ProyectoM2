package com.unis.repository;

import com.unis.model.DoctorAcc;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DoctorAccRepository implements PanacheRepository<DoctorAcc> {
}
