package com.cydeo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Data
public class Category extends BaseEntity {
    private String name;
}