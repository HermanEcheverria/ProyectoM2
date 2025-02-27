package com.unis.repository;

import com.unis.model.Agenda;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AgendaRepository implements PanacheRepository<Agenda> {
   
}
