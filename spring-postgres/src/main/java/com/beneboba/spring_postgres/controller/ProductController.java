package com.beneboba.spring_postgres.controller;

import com.beneboba.spring_postgres.model.Product;
import com.beneboba.spring_postgres.repository.ProductRepository;
import com.beneboba.spring_postgres.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/findByPrice")
    public @ResponseBody List<Product> findProduct(@RequestParam Double price, @RequestParam Integer page, @RequestParam Integer size){
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAllByPrice(price, pageable);
    }

    @GetMapping(path = "/findByPriceSort")
    public @ResponseBody List<Product> findProductSort(@RequestParam Double price, @RequestParam Integer page, @RequestParam Integer size, String sortColumn){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortColumn).descending());
        return productRepository.findAllByPrice(price, pageable);
    }

    @PostMapping(path = "/generate")
    public @ResponseBody String generateProduct(){
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            Product product = new Product();
            product.setPrice(rand.nextDouble(1000));
            product.setName(UUID.randomUUID().toString());
            productRepository.save(product);
        }
        return "generate success";
    }

    @GetMapping(path = "/all")
    public @ResponseBody List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @GetMapping(path = "/allp")
    public @ResponseBody Page<Product> getAllProductsPagination(@RequestParam Integer page, @RequestParam Integer size){
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

    @GetMapping(path = "/search")
    public @ResponseBody List<Product> findByName(@RequestParam String name){
        return productRepository.findByName(name);
    }
}
