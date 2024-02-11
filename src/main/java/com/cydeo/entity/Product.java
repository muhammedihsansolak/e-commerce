package com.cydeo.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity{

    private BigDecimal price;
    private Integer remainingQuantity;
    private String name;

    @Column(unique = true)
    private String productCode;

    @ManyToMany
    @JoinTable(name = "product_category_rel",
            joinColumns = @JoinColumn(name="product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categoryList;

}
