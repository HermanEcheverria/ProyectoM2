package com.unis.service;

import com.unis.model.DoctorImage;
import com.unis.repository.DoctorImageRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class DoctorImageService {

    @Inject
    DoctorImageRepository doctorImageRepository;

    /**
     * Obtiene la imagen del doctor basado en el ID del doctor
     */
    public Optional<DoctorImage> getDoctorImageById(Long id) {
        return doctorImageRepository.findByIdOptional(id);
    }

    /**
     * Actualiza la imagen y el título de la imagen de un doctor
     */
    @Transactional
    public void updateDoctorImage(Long id, byte[] fotografia, byte[] fotoTitulo) {
        DoctorImage doctorImage = doctorImageRepository.findById(id);
        if (doctorImage != null) {
            doctorImage.setFotografia(fotografia);
            doctorImage.setFotoTitulo(fotoTitulo);
            doctorImageRepository.persist(doctorImage); // Guardar cambios en BD
        }
    }

    /**
     * Elimina la imagen de un doctor
     */
    @Transactional
    public void deleteDoctorImages(Long id) {
        DoctorImage doctorImage = doctorImageRepository.findById(id);
        if (doctorImage != null) {
            doctorImage.setFotografia(null);
            doctorImage.setFotoTitulo(null);
            doctorImageRepository.persist(doctorImage); // Guardar cambios en BD
        }
    }
}
