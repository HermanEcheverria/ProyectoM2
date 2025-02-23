package com.unis.service;

import com.unis.model.DoctorAcc;
import com.unis.repository.DoctorAccRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class DoctorAccService {
    @Inject
    DoctorAccRepository doctorAccRepository;

    public Optional<DoctorAcc> getDoctorById(Long id) {
        return doctorAccRepository.find("usuario.idUsuario", id).firstResultOptional();
    }

    @Transactional
    public void updateDoctor(Long id, DoctorAcc doctorData) {
        Optional<DoctorAcc> existingDoctor = doctorAccRepository.find("usuario.idUsuario", id).firstResultOptional();
        if (existingDoctor.isPresent()) {
            DoctorAcc doctor = existingDoctor.get();
            doctor.setApellido(doctorData.getApellido());
            doctor.setDocumento(doctorData.getDocumento());
            doctor.setFechaNacimiento(doctorData.getFechaNacimiento());
            doctor.setGenero(doctorData.getGenero());
            doctor.setTelefono(doctorData.getTelefono());
            doctor.setEspecialidad(doctorData.getEspecialidad());
            doctor.setNumeroColegiado(doctorData.getNumeroColegiado());
            doctor.setFechaGraduacion(doctorData.getFechaGraduacion());
            doctor.setUniversidadGraduacion(doctorData.getUniversidadGraduacion());
            doctor.setDisponibilidad(doctorData.getDisponibilidad());
            doctorAccRepository.persist(doctor);
        }
    }
}
