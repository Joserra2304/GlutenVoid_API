package com.svalero.glutenvoid.repository;

import com.svalero.glutenvoid.domain.Product;
import com.svalero.glutenvoid.exception.ProductNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findAll();

    List<Product> findByCompany(String company) throws ProductNotFoundException;

    List<Product> findByHasGluten(boolean hasGluten) throws ProductNotFoundException;


}
