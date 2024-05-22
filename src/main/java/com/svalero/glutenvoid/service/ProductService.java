package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.Product;
import com.svalero.glutenvoid.exception.ProductNotFoundException;

import java.util.List;
import java.util.Map;

public interface ProductService {

    List<Product> findAll();

Product findById(long id) throws ProductNotFoundException;

    List<Product> filterByGluten(boolean hasGluten) throws ProductNotFoundException;
    List<Product> filterByCompany(String company) throws ProductNotFoundException;

    Product addProduct(Product product);

    void deleteProduct(long id) throws ProductNotFoundException;

    Product updateProductByField(long id, Map<String, Object> updates) throws ProductNotFoundException;

}
