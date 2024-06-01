package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.dto.EstablishmentDto;
import com.svalero.glutenvoid.domain.entity.Establishment;
import com.svalero.glutenvoid.exception.EstablishmentNotFoundException;

import java.util.List;
import java.util.Map;

public interface EstablishmentService {

    List<EstablishmentDto> findAll();

    EstablishmentDto findById(long id) throws EstablishmentNotFoundException;

    List<EstablishmentDto> filterByGlutenFree(boolean glutenFree) throws EstablishmentNotFoundException;
    List<EstablishmentDto> filterByCity(String city) throws EstablishmentNotFoundException;

    EstablishmentDto addEstablishment(EstablishmentDto establishmentDto);

    void deleteEstablishment(long id) throws EstablishmentNotFoundException;

    EstablishmentDto updateEstablishmentByField(long id, Map<String, Object> updates) throws EstablishmentNotFoundException;
}
