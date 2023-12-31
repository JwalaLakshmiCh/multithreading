package com.jwala.learning.multithreading.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="USER_TABLE")
public class User {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String email;
    private String gender;
}
