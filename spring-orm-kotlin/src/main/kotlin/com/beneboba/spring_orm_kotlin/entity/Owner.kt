package com.beneboba.spring_orm.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import lombok.ToString
import java.util.List

@ToString(exclude = "blogList")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "OWNER_DETAILS")
class Owner(
    @field:Column(name = "name") private val name: String,
    @field:Column(name = "email") private val email: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private val id: Long? = null

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
    private val blogList: List<Blog>? = null
}