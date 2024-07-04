package com.beneboba.spring_orm;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@ToString(exclude = "blogList")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "OWNER_DETAILS")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Blog> blogList;

    public Owner(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

