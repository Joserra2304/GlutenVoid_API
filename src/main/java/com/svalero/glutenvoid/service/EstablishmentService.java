package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.entity.Establishment;
import com.svalero.glutenvoid.exception.EstablishmentNotFoundException;

import java.util.List;
import java.util.Map;

public interface EstablishmentService {

    List<Establishment> findAll();

    Establishment findById(long id) throws EstablishmentNotFoundException;

    List<Establishment> filterByGlutenFree(boolean glutenFree) throws EstablishmentNotFoundException;
    List<Establishment> filterByCity(String city) throws EstablishmentNotFoundException;

    Establishment addEstablishment(Establishment establishment);

    void deleteEstablishment(long id) throws EstablishmentNotFoundException;

    Establishment updateEstablishmentByField(long id, Map<String, Object> updates) throws EstablishmentNotFoundException;
}
