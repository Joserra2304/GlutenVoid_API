package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.dto.EstablishmentDto;
import com.svalero.glutenvoid.domain.entity.Establishment;
import com.svalero.glutenvoid.exception.EstablishmentNotFoundException;
import com.svalero.glutenvoid.repository.EstablishmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EstablishmentServiceImplementation implements EstablishmentService {

    @Autowired
    EstablishmentRepository establishmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<EstablishmentDto> findAll() {
        return establishmentRepository.findAll().stream()
                .map(establishment -> modelMapper.map(establishment, EstablishmentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public EstablishmentDto findById(long id) throws EstablishmentNotFoundException {
        Establishment establishment = establishmentRepository.findById(id)
                .orElseThrow(() -> new EstablishmentNotFoundException("Establishment not found with id: " + id));
        return modelMapper.map(establishment, EstablishmentDto.class);
    }

    @Override
    public List<EstablishmentDto> filterByGlutenFree(boolean glutenFree) throws EstablishmentNotFoundException {
        return establishmentRepository.findByGlutenFreeOption(glutenFree).stream()
                .map(establishment -> modelMapper.map(establishment, EstablishmentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<EstablishmentDto> filterByCity(String city) throws EstablishmentNotFoundException {
        return establishmentRepository.findByCity(city).stream()
                .map(establishment -> modelMapper.map(establishment, EstablishmentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public EstablishmentDto addEstablishment(EstablishmentDto establishmentDto) {
        Establishment establishment = modelMapper.map(establishmentDto, Establishment.class);
        Establishment savedEstablishment = establishmentRepository.save(establishment);
        return modelMapper.map(savedEstablishment, EstablishmentDto.class);
    }

    @Override
    public void deleteEstablishment(long id) throws EstablishmentNotFoundException {
        Establishment establishment = establishmentRepository.findById(id)
                .orElseThrow(() -> new EstablishmentNotFoundException("Establishment not found with id: " + id));
        establishmentRepository.delete(establishment);
    }

    @Override
    public EstablishmentDto updateEstablishmentByField(long id, Map<String, Object> updates)
            throws EstablishmentNotFoundException {
        Establishment establishment = establishmentRepository.findById(id)
                .orElseThrow(() -> new EstablishmentNotFoundException("Establishment not found with id: " + id));

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "name":
                        establishment.setName((String) value);
                        break;
                    case "description":
                        establishment.setDescription((String) value);
                        break;
                    case "phoneNumber":
                        establishment.setPhoneNumber(Integer.parseInt(value.toString()));
                        break;
                    case "address":
                        establishment.setAddress((String) value);
                        break;
                    case "city":
                        establishment.setCity((String) value);
                        break;
                    case "latitude":
                        establishment.setLatitude(Double.parseDouble(value.toString()));
                        break;
                    case "longitude":
                        establishment.setLongitude(Double.parseDouble(value.toString()));
                        break;
                    case "glutenFreeOption":
                        establishment.setGlutenFreeOption(Boolean.parseBoolean(value.toString()));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid property: " + key);
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid format for property " + key + ", value: " + value);
            }
        });

        Establishment updatedEstablishment = establishmentRepository.save(establishment);
        return modelMapper.map(updatedEstablishment, EstablishmentDto.class);
    }

}
