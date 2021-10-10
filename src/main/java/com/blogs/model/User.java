package com.blogs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users", schema = "blog")
public class User {
    @Id
    private String email;

    private String name;

    private String password;

    private String role;

    private int userId;
}
