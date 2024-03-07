package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.Product;
import com.svalero.glutenvoid.domain.Recipe;
import com.svalero.glutenvoid.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(name="company", required = false, defaultValue = "") String company,
            @RequestParam(name="hasGluten", required = false, defaultValue = "") String hasGluten) {

        if(!company.isEmpty()){
            return ResponseEntity.ok(productService.filterByCompany(company));
        } else if (!hasGluten.isEmpty()) {
            return ResponseEntity.ok(productService.filterByGluten(Boolean.parseBoolean(hasGluten)));
        } else{
            return ResponseEntity.ok(productService.findAll());
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id){
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product){
        Product newProduct = productService.addProduct(product);
        return  ResponseEntity.ok(newProduct);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id){
        productService.deleteProduct(id);

        String deleteMessage = "Product deleted successfully";
        return  ResponseEntity.ok(deleteMessage);

    }
}
