package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.Establishment;
import com.svalero.glutenvoid.domain.Product;

import java.util.List;
import java.util.Map;

public interface EstablishmentService {

    List<Establishment> findAll();

    Establishment findById(long id);

    List<Establishment> filterByGlutenFree(boolean glutenFree);
    List<Establishment> filterByCity(String city);

    Establishment addEstablishment(Establishment establishment);

    void deleteEstablishment(long id);

    Establishment updateEstablishmentByField(long id, Map<String, Object> updates);
}
