package com.beneboba.consume_quoters.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SmsGatewayResponse {

    private String transactionId;
    private String responseCode;
    private String responseMessage;
    private List<SmsInfo> smsInformation;
}