package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.Establishment;
import com.svalero.glutenvoid.exception.EstablishmentNotFoundException;
import com.svalero.glutenvoid.repository.EstablishmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EstablishmentServiceImplementation implements EstablishmentService {

    @Autowired
    EstablishmentRepository establishmentRepository;

    @Override
    public List<Establishment> findAll() {
        return establishmentRepository.findAll();
    }

    @Override
    public Establishment findById(long id) throws EstablishmentNotFoundException {
        return establishmentRepository.findById(id).orElseThrow(EstablishmentNotFoundException::new);
    }

    @Override
    public List<Establishment> filterByGlutenFree(boolean glutenFree) throws EstablishmentNotFoundException {
        return establishmentRepository.findByGlutenFreeOption(glutenFree);
    }

    @Override
    public List<Establishment> filterByCity(String city) throws EstablishmentNotFoundException {
        return establishmentRepository.findByCity(city);
    }

    @Override
    public Establishment addEstablishment(Establishment establishment) {
        return establishmentRepository.save(establishment);
    }

    @Override
    public void deleteEstablishment(long id) throws EstablishmentNotFoundException{
        Establishment deleteEstablishment = establishmentRepository.findById(id)
                .orElseThrow(EstablishmentNotFoundException::new);
        establishmentRepository.delete(deleteEstablishment);
    }

    @Override
    public Establishment updateEstablishmentByField(long id, Map<String, Object> updates)
            throws EstablishmentNotFoundException {
        Establishment newUpdate = findById(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    newUpdate.setName((String) value);
                    break;
                case "description":
                    newUpdate.setDescription((String) value);
                    break;
                case "phoneNumber":
                    newUpdate.setPhoneNumber(Integer.parseInt(value.toString()));
                    break;
                case "address":
                    newUpdate.setAddress((String) value);
                    break;
                case "city":
                    newUpdate.setCity((String) value);
                    break;
                case "latitude":
                    newUpdate.setLatitude(Double.parseDouble(value.toString()));
                    break;
                case "longitude":
                    newUpdate.setLongitude(Double.parseDouble(value.toString()));
                    break;
                case "rating":
                    newUpdate.setRating(Double.parseDouble(value.toString()));
                    break;
                case "glutenFreeOption":
                    newUpdate.setGlutenFreeOption(Boolean.parseBoolean(value.toString()));
                    break;
            }
        });


        return  establishmentRepository.save(newUpdate);
    }
}
