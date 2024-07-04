package com.beneboba.spring_orm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/blog")
public class BlogController {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private BlogRepository blogRepository;

    @PostMapping("/saveOwner")
    public ResponseEntity<Owner> saveOwner(@RequestBody Owner owner) {

//        Owner ownerIn = new Owner(owner.getName(), owner.getEmail());

//        List<Blog> blogs = new ArrayList<>();
//        for (Blog blogIn : owner.getBlogList()) {
//            Blog blog = new Blog(blogIn.getTitle(), blogIn.getCategory(), blogIn.getContent());
//            blog.setOwner(ownerIn);
//            blogs.add(blog);
//        }

//        ownerIn.setBlogList(blogs);
        Owner ownerOut = ownerRepository.save(owner);

        blogRepository.saveAll(owner.getBlogList());

        return ResponseEntity.ok(ownerOut);
    }

    @PostMapping("/saveBlog")
    public ResponseEntity<Owner> saveBlog(@RequestParam(name = "id") String id) {

        Owner ownerTemp = ownerRepository.findById(Long.valueOf(id)).get();

        List<Blog> blogs = new ArrayList<>();

        Blog blog = new Blog("Build application server using NodeJs", "nodeJs",
                "We will build REStful api using nodeJs.");
        blog.setOwner(ownerTemp);
        blogs.add(blog);

        blog = new Blog("Single Page Application using Angular", "Angular",
                "We can build robust application using Angular framework.");
        blog.setOwner(ownerTemp);
        blogs.add(blog);

        ownerTemp.setBlogList(blogs);

        Owner ownerSaved = ownerRepository.save(ownerTemp);

        return ResponseEntity.ok(ownerSaved);
    }

    @GetMapping("/getOwner/{id}")
    public ResponseEntity<Owner> getOwner(@PathVariable(name = "id") String id) {

        Owner ownerOut = ownerRepository.findById(Long.valueOf(id)).get();

        return ResponseEntity.ok(ownerOut);
    }

    @GetMapping("/getBlog/{id}")
    public ResponseEntity<Blog> getBlog(@PathVariable(name = "id") String id) {

        Blog blogOut = blogRepository.findById(Long.valueOf(id)).get();

        return ResponseEntity.ok(blogOut);
    }
}