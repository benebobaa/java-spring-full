package com.beneboba.consume_quoters.controller;

import com.beneboba.consume_quoters.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/quotes")
public class ConsumeController {

    @Autowired
    RestTemplate restTemplate;

    static final String API_QUOTE_URL = "http://localhost:8080/api";

    @GetMapping
    public HttpEntity<Quote[]> allQuotes(){
        ResponseEntity<Quote[]> responseEntity = restTemplate.getForEntity(API_QUOTE_URL, Quote[].class);
        Quote[] objects = responseEntity.getBody();
        return new ResponseEntity<>(objects, HttpStatus.OK);

    }

    @GetMapping("/random")
    public ResponseEntity<Quote> getQuoteRandom(){
        Quote quote = restTemplate.getForObject(
                API_QUOTE_URL.concat("/random"), Quote.class);
        return new ResponseEntity<>(quote, HttpStatus.OK);

    }

    @GetMapping("/list")
    public ResponseEntity<?> listQuotes(){
        try {
            ResponseEntity<List<Quote>> responseEntity = restTemplate.exchange(API_QUOTE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<Quote>>(){});
            List<Quote> quotes = responseEntity.getBody();
            return ResponseEntity.ok(quotes);
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
