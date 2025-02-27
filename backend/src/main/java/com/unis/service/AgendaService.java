package com.unis.service;

import java.util.List;

import com.unis.model.Agenda;
import com.unis.repository.AgendaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AgendaService {

    @Inject
    AgendaRepository agendaRepository;

    public List<Agenda> obtenerAgendasPorDoctor(Long idDoctor) {
        return agendaRepository.find("idDoctor", idDoctor).list();
    }

    @Transactional
    public void crearAgenda(Agenda agenda) {
        agendaRepository.persist(agenda);
    }

    @Transactional
    public void actualizarAgenda(Long id, Agenda agendaActualizada) {
        Agenda agenda = agendaRepository.findById(id);
        if (agenda == null) {
            throw new IllegalArgumentException("No se encontró la agenda con id: " + id);
        }
        agenda.setDiasAtencion(agendaActualizada.getDiasAtencion());
        agenda.setHoraInicio(agendaActualizada.getHoraInicio());
        agenda.setHoraFin(agendaActualizada.getHoraFin());
        agenda.setNotas(agendaActualizada.getNotas());
        // Los cambios se persisten al finalizar la transacción.
    }
}
