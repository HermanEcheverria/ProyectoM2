package com.unis.repository;

import com.unis.model.DoctorImage;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DoctorImageRepository implements PanacheRepository<DoctorImage> {
}
