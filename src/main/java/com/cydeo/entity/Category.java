package com.cydeo.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
@Table(name = "categories")
public class Category extends BaseEntity {
    private String name;
}
