package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {

    List<Product> findAll();

Product findById(long id);

    List<Product> filterByGluten(boolean hasGluten);
    List<Product> filterByCompany(String company);

    Product addProduct(Product product);

    void deleteProduct(long id);

    Product updateProductByField(long id, Map<String, Object> updates);

}
