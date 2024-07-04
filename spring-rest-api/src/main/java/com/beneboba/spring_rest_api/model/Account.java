package com.beneboba.spring_rest_api.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Account Number is mandatory")
    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Min(1)
    private float balance;

    public Account(Integer id, String accountNumber, float balance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Account( String accountNumber, float balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
}
