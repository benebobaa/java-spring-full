package com.beneboba.spring_rest_api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountWithBalance {
    private Integer id;
    private String accountName;
    private float balance;
}
