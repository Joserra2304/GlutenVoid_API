package com.svalero.glutenvoid.controller;

import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.Product;
import com.svalero.glutenvoid.domain.Recipe;
import com.svalero.glutenvoid.exception.ErrorMessage;
import com.svalero.glutenvoid.exception.ProductNotFoundException;
import com.svalero.glutenvoid.exception.UserNotFoundException;
import com.svalero.glutenvoid.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(name="company", required = false, defaultValue = "") String company,
            @RequestParam(name="hasGluten", required = false, defaultValue = "") String hasGluten)
            throws ProductNotFoundException {

        if(!company.isEmpty()){
            return ResponseEntity.ok(productService.filterByCompany(company));
        } else if (!hasGluten.isEmpty()) {
            return ResponseEntity.ok(productService.filterByGluten(Boolean.parseBoolean(hasGluten)));
        } else{
            return ResponseEntity.ok(productService.findAll());
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id) throws ProductNotFoundException{
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product){
        Product newProduct = productService.addProduct(product);
        return  ResponseEntity.ok(newProduct);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) throws ProductNotFoundException{
        productService.deleteProduct(id);

        String deleteMessage = "Product deleted successfully";
        return  ResponseEntity.ok(deleteMessage);

    }

    @PatchMapping("/products/{id}")
    public ResponseEntity<Product> updateProductPartially(
            @PathVariable long id, @RequestBody Map<String, Object> updates) throws ProductNotFoundException{
        Product updateProduct = productService.updateProductByField(id, updates);
        return ResponseEntity.ok(updateProduct);

    }

    @ExceptionHandler(ProductNotFoundException.class)
    public  ResponseEntity<ErrorMessage> productNotFoundException(ProductNotFoundException pnfe){
        //Logger
        ErrorMessage notFound = new ErrorMessage(404, pnfe.getMessage());
        return new ResponseEntity<>(notFound, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> badRequest(MethodArgumentNotValidException manve){
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldname = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldname, message);
        });

        //Logger
        ErrorMessage badRequest = new ErrorMessage(400, "Bad Request", errors);
        return new ResponseEntity<>(badRequest, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        //Logger
        ErrorMessage errorMessage = new ErrorMessage(500, "Internal Server Error");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }


 }

