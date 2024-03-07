package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.Establishment;
import com.svalero.glutenvoid.repository.EstablishmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstablishmentServiceImplementation implements EstablishmentService {

    @Autowired
    EstablishmentRepository establishmentRepository;

    @Override
    public List<Establishment> findAll() {
        return establishmentRepository.findAll();
    }

    @Override
    public Establishment findById(long id) {
        return establishmentRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Establishment> filterByGlutenFree(boolean glutenFree) {
        return establishmentRepository.findByGlutenFreeOption(glutenFree);
    }

    @Override
    public List<Establishment> filterByCity(String city) {
        return establishmentRepository.findByCity(city);
    }

    @Override
    public Establishment addEstablishment(Establishment establishment) {
        return establishmentRepository.save(establishment);
    }

    @Override
    public void deleteEstablishment(long id) {
        Establishment deleteEstablishment = establishmentRepository.findById(id).orElseThrow();
        establishmentRepository.delete(deleteEstablishment);
    }
}
