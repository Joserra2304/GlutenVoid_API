package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.Establishment;

import java.util.List;

public interface EstablishmentService {

    List<Establishment> findAll();

    List<Establishment> filterByGlutenFree(boolean glutenFree);
    List<Establishment> filterByCity(String city);

    Establishment addEstablishment(Establishment establishment);
}
