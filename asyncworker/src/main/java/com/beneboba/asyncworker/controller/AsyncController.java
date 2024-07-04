package com.beneboba.asyncworker.controller;

import com.beneboba.asyncworker.model.GithubUser;
import com.beneboba.asyncworker.service.GitHubLookupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class AsyncController {

    @Autowired
    GitHubLookupService service;

    @GetMapping("/github/user")
    public ResponseEntity<List<GithubUser>> findGithubUser() throws Exception{
        // Start the clock
        long start = System.currentTimeMillis();

        // Kick of multiple, asynchronous lookups
        CompletableFuture<GithubUser> page1 = service.findUser("PivotalSoftware");
        CompletableFuture<GithubUser> page2 = service.findUser("CloudFoundry");
        CompletableFuture<GithubUser> page3 = service.findUser("Spring-Projects");

        service.findUser("PivotalSoftware");
        service.findUser("CloudFoundry");
        service.findUser("Spring-Projects");
        // Wait until they are all done
        CompletableFuture.allOf(page1,page2,page3).join();

        // Print results, including elapsed time
        log.info("Elapsed time: " + (System.currentTimeMillis() - start));
        log.info("--> " + page1.get());
        log.info("--> " + page2.get());
        log.info("--> " + page3.get());
        List<GithubUser> list = new ArrayList<>();
        list.add(page1.get());
        list.add(page2.get());
        list.add(page3.get());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/github/user2")
    public ResponseEntity<List<GithubUser>> findGithubUser2() throws Exception{
        // Start the clock
        long start = System.currentTimeMillis();

        // Kick of multiple, asynchronous lookups
        GithubUser page1 = service.findUser2("PivotalSoftware");
        GithubUser page2 = service.findUser2("CloudFoundry");
        GithubUser page3 = service.findUser2("Spring-Projects");

        service.findUser("PivotalSoftware");
        service.findUser("CloudFoundry");
        service.findUser("Spring-Projects");
        // Wait until they are all done

        // Print results, including elapsed time
        log.info("Elapsed time: " + (System.currentTimeMillis() - start));
        log.info("--> " + page1);
        log.info("--> " + page2);
        log.info("--> " + page3);
        List<GithubUser> list = new ArrayList<>();
        list.add(page1);
        list.add(page2);
        list.add(page3);

        return ResponseEntity.ok(list);
    }
}