package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();


    List<Product> filterByGluten(boolean hasGluten);
    List<Product> filterByCompany(String company);

    Product addProduct(Product product);

}
