package com.spring.redis.repository;

import com.spring.redis.model.Product;
import org.springframework.stereotype.Repository;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;


@Repository
public class ProductRepo {

    private RedisTemplate redisTemplate;

    public ProductRepo(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String HASH_KEY = "Product";

    public Product saveProduct(Product product){
        redisTemplate.opsForHash().put(HASH_KEY,product.getId(),product);
        return product;
    }

    public List<Product> getAllProduct(){
        return redisTemplate.opsForHash().values(HASH_KEY);
    }

    public Product findById(long id){
        System.out.println("Fetching from DB -> "+id);
        return (Product) redisTemplate.opsForHash().get(HASH_KEY,id);
    }

    public String deleteProduct(long id){
        redisTemplate.opsForHash().delete(HASH_KEY,id);
        return "Product deleted....!";
    }
}
