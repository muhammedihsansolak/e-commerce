package com.cydeo.repository;

import com.cydeo.entity.Category;
import com.cydeo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findTop3ByOrderByPriceDesc();


    Product findFirstByName(String name);


    List<Product> findAllByCategoryListContaining(Category category);

    Integer countProductByPriceGreaterThan(BigDecimal price);

    List<Product> findAllByQuantityIsGreaterThanEqual(int quantity);

    @Query(value = "select * from product p where p.price > ?1 and p.remaining_quantity < ?2", nativeQuery = true)
    List<Product> retrieveProductListGreaterThanPriceAndLowerThanRemainingQuantity(BigDecimal price, int remainingQuantity);

    @Query(value = "select * from product p join product_category_rel pl on pl.p_id = p.id where pl.c_id = ?1", nativeQuery = true)
    List<Product> retrieveProductListByCategory(Long categoryId);

    @Query(value = "select * from product p join product_category_rel pl on pl.p_id = p.id where pl.c_id in(?1) and p.price > ?2 " , nativeQuery = true)
    List<Product> retrieveProductListByCategoryAndPrice(List<Long> categoryId, BigDecimal price);

}
