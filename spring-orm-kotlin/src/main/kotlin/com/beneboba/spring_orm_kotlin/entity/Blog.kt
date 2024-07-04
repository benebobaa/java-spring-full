package com.beneboba.spring_orm.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.Data
import lombok.NoArgsConstructor

@Data
@NoArgsConstructor
@Entity
@Table(name = "BLOG_DETAILS")
class Blog(
    @field:Column(name = "title") private val title: String,
    @field:Column(name = "category") private val category: String,
    @field:Column(
        name = "content"
    ) private val content: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private val id: Long? = null

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private val owner: Owner? = null
}