package com.beneboba.spring_orm_kotlin.controller

import BlogRepository
import OwnerRepository
import com.beneboba.spring_orm.entity.Blog
import com.beneboba.spring_orm.entity.Owner
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/blog")
@Slf4j
class BlogController {

    @Autowired
    private val ownerRepository: OwnerRepository? = null

    @Autowired
    private val blogRepository: BlogRepository? = null

    @PostMapping("/saveOwner")
    fun saveOwner(@RequestBody owner: Owner): ResponseEntity<Owner> {
        BlogController.log.info("Owner save called...")

        // a new Owner
        val ownerIn = Owner(owner.getName(), owner.getEmail())

        // list of Blog
        val blogs: MutableList<Blog> = ArrayList()
        for (blogIn in owner.getBlogList()) {
            // new Blog
            val blog = Blog(blogIn.getTitle(), blogIn.getCategory(), blogIn.getContent())
            // set owner to Blog
            blog.setOwner(ownerIn)
            // add blog to list
            blogs.add(blog)
        }

        // add blog list to Owner
        ownerIn.setBlogList(blogs)

        // save Owner
        val ownerOut: Owner = ownerRepository.save(ownerIn)
        BlogController.log.info("Owner out :: $ownerOut")

        BlogController.log.info("Saved!!!")
        return ResponseEntity.ok(ownerOut)
    }

    @PostMapping("/saveBlog")
    fun saveBlog(@RequestParam(name = "id") id: String): ResponseEntity<Owner> {
        BlogController.log.info("Blog save called...")

        // fetch Ower
        val ownerTemp: Owner = ownerRepository.findById(id.toLong()).get()

        // list of Blog
        val blogs: MutableList<Blog> = ArrayList()

        // new Blog
        var blog = Blog(
            "Build application server using NodeJs", "nodeJs",
            "We will build REStful api using nodeJs."
        )
        // set owner to blog
        blog.setOwner(ownerTemp)
        // add Blog to list
        blogs.add(blog)

        blog = Blog(
            "Single Page Application using Angular", "Angular",
            "We can build robust application using Angular framework."
        )
        // set owner to blog
        blog.setOwner(ownerTemp)
        blogs.add(blog)

        // add Blog list to Owner
        ownerTemp.setBlogList(blogs)

        // save Owner
        val ownerSaved: Owner = ownerRepository.save(ownerTemp)

        BlogController.log.info("Saved!!!")
        return ResponseEntity.ok(ownerSaved)
    }

    @GetMapping("/getOwner/{id}")
    fun getOwner(@PathVariable(name = "id") id: String): ResponseEntity<Owner> {
        BlogController.log.info("Owner get called...")

        // fetch Owner
        val ownerOut: Owner = ownerRepository.findById(id.toLong()).get()
        BlogController.log.info("\nOwner details :: \n$ownerOut")
        BlogController.log.info(
            """
                
                List of Blogs :: 
                ${ownerOut.getBlogList()}
                """.trimIndent()
        )

        BlogController.log.info("\nDone!!!")
        return ResponseEntity.ok(ownerOut)
    }

    @GetMapping("/getBlog/{id}")
    fun getBlog(@PathVariable(name = "id") id: String): ResponseEntity<Blog> {
        BlogController.log.info("Blog get called...")

        // fetch Blog
        val blogOut: Blog = blogRepository.findById(id.toLong()).get()
        BlogController.log.info("\nBlog details :: \n$blogOut")
        BlogController.log.info(
            """
                
                Owner details :: 
                ${blogOut.getOwner()}
                """.trimIndent()
        )

        BlogController.log.info("\nDone!!!")
        return ResponseEntity.ok(blogOut)
    }
}