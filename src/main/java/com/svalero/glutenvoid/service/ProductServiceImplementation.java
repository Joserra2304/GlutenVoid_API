package com.svalero.glutenvoid.service;

import com.svalero.glutenvoid.domain.Product;
import com.svalero.glutenvoid.domain.User;
import com.svalero.glutenvoid.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    @Override
    public Product updateProductByField(long id, Map<String, Object> updates) {
        Product newUpdate = findById(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    newUpdate.setName((String) value);
                    break;
                case "description":
                    newUpdate.setDescription((String) value);
                    break;
                case "company":
                    newUpdate.setCompany((String) value);
                    break;
                case "hasGluten":
                    newUpdate.setHasGluten(Boolean.parseBoolean(value.toString()));
                    break;
                case "rating":
                    newUpdate.setRating(Double.parseDouble(value.toString()));
                    break;
            }
        });


        return productRepository.save(newUpdate);

    }
}
