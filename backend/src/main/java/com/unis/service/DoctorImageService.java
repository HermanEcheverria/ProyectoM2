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

    public Optional<DoctorImage> getDoctorImageByDoctorId(Long idDoctor) {
        return doctorImageRepository.find("doctor.idDoctor", idDoctor).firstResultOptional();
    }

    @Transactional
    public void updateDoctorImage(Long idDoctor, byte[] fotografia, byte[] fotoTitulo) {
        Optional<DoctorImage> existingImage = doctorImageRepository.find("doctor.idDoctor", idDoctor).firstResultOptional();
        if (existingImage.isPresent()) {
            DoctorImage doctorImage = existingImage.get();
            doctorImage.setFotografia(fotografia);
            doctorImage.setFotoTitulo(fotoTitulo);
            doctorImageRepository.persist(doctorImage);
        }
    }

    @Transactional
    public void deleteDoctorImage(Long idDoctor) {
        doctorImageRepository.delete("doctor.idDoctor", idDoctor);
    }
}
