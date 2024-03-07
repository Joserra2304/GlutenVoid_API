package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.Product;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImplementation implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(long id) {
        return productRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Product> filterByGluten(boolean hasGluten) {
        return productRepository.findByHasGluten(hasGluten);
    }

    @Override
    public List<Product> filterByCompany(String company) {
        return productRepository.findByCompany(company);
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(long id) {
        Product deleteProduct = productRepository.findById(id).orElseThrow();
        productRepository.delete(deleteProduct);
    }
}
