package com.cydeo.service;

import com.cydeo.dto.ProductDTO;
import com.cydeo.dto.request.ProductRequest;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO updateProduct(ProductDTO productDTO, String productCode);
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO getProductByName(String name);
    List<ProductDTO> getTop3Product();
    Long countProductByPriceGreaterThan(Integer price);
    List<ProductDTO> findProductByProductRequest(ProductRequest productRequest);
    List<ProductDTO> getProductsByPriceAndQuantity(BigDecimal price, Integer quantity);
    List<ProductDTO> getProductsByCategory(Long categoryId);
    ProductDTO getProductByProductCode(String productCode);
}
