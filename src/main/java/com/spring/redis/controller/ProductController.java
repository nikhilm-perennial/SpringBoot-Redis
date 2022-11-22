package com.spring.redis.controller;

import com.spring.redis.model.Product;
import com.spring.redis.repository.ProductRepo;
import org.apache.catalina.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private ProductRepo productRepo;

    public ProductController(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @PostMapping("/product")
    public ResponseEntity<String> saveProduct(@RequestBody Product product){
        Product result = productRepo.saveProduct(product);
        if (result!=null)
            return ResponseEntity.ok("Product Added....");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/product")
    public ResponseEntity<List<Product>> getAllProduct(){
        List<Product> products = productRepo.getAllProduct();
        if (!products.isEmpty())
            return ResponseEntity.ok(products);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/product/{id}")  //#result.price < 45000 -> fetch data from db; #result.price > 45000 -> fetch data from db
    @Cacheable(key = "#id",value = "Product",unless = "#result.price > 45000")
    public Product getProductById(@PathVariable long id){
        return productRepo.findById(id);
    }

    @DeleteMapping("/product/{id}")
    @CacheEvict(key = "#id",value = "Product")
    public String deleteProduct(@PathVariable long id){
        productRepo.deleteProduct(id);
        return "Product deleted Successfully....";
    }
}
