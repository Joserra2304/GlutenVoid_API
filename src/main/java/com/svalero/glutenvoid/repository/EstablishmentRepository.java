package com.svalero.glutenvoid.repository;

import com.svalero.glutenvoid.domain.entity.Establishment;
import com.svalero.glutenvoid.exception.EstablishmentNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstablishmentRepository extends CrudRepository<Establishment, Long> {

   List<Establishment> findAll();

   List<Establishment> findByCity(String city) throws EstablishmentNotFoundException;

   List<Establishment> findByGlutenFreeOption(boolean glutenFree) throws EstablishmentNotFoundException;

}
