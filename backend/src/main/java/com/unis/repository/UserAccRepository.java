package com.unis.repository;

import com.unis.model.UserAcc;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserAccRepository implements PanacheRepository<UserAcc> {
}
