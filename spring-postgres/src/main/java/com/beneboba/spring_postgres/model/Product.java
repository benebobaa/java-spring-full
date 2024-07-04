package com.beneboba.spring_postgres.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Double price;

    public Product() {
        super();
    }


    public Product(String name, Double price) {
        super();
        this.name = name;
        this.price = price;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Double getPrice() {
        return price;
    }


    public void setPrice(Double price) {
        this.price = price;
    }


    public Long getId() {
        return id;
    }


    @Override
    public String toString() {
        return "Product [id=" +
                id +
                ", name=" +
                name +
                ", price=" +
                price +
                "]";
    }
}
