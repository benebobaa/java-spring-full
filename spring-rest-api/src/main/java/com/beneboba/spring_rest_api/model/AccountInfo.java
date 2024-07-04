package com.beneboba.spring_rest_api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountInfo {
    private Integer id;
    private Integer accountId;
    private String accountName;

    public AccountInfo(Integer accountId, String accountName){
        this.accountId = accountId;
        this.accountName = accountName;
    }
}
