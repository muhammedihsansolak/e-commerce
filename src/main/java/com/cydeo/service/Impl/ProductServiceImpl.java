package com.cydeo.service.Impl;

import com.cydeo.dto.ProductDTO;
import com.cydeo.dto.request.ProductRequest;
import com.cydeo.entity.Product;
import com.cydeo.exception.ProductNotFoundException;
import com.cydeo.mapper.Mapper;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Mapper mapper;

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream().map(product -> mapper.convert(product, new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, String productCode) {
        Product foundProduct = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with product code: "+ productCode));

        Product product = mapper.convert(productDTO, new Product());
        product.setId(foundProduct.getId());
        product.setProductCode(productCode);
        Product savedProduct = productRepository.save(product);

        return mapper.convert(savedProduct, new ProductDTO());
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product productToSave = mapper.convert(productDTO, new Product());

        if (productDTO.getProductCode() == null) {
            String productCode = UUID.randomUUID().toString();
            productToSave.setProductCode(productCode);
        }

        Product savedProduct = productRepository.save(productToSave);
        return mapper.convert(savedProduct, new ProductDTO());
    }

    @Override
    public ProductDTO getProductByName(String name) {
        Product entity = productRepository.findFirstByName(name);
        return mapper.convert(entity,new ProductDTO());
    }

    @Override
    public List<ProductDTO> getTop3Product() {
        return productRepository
                .findTop3ByOrderByPriceDesc().stream()
                .map(product -> mapper.convert(product,new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public Long countProductByPriceGreaterThan(Integer price) {
        return (long) productRepository.countProductByPriceGreaterThan(BigDecimal.valueOf(price));
    }

    @Override
    public List<ProductDTO> findProductByProductRequest(ProductRequest productRequest) {
        List<Product> products = productRepository.retrieveProductListByCategoryAndPrice(productRequest.getCategoryList(), productRequest.getPrice());

        return products.stream().map(product -> mapper.convert(product, new ProductDTO())).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByPriceAndQuantity(BigDecimal price, Integer quantity) {
        List<Product> products = productRepository.retrieveProductListGreaterThanPriceAndLowerThanRemainingQuantity(price, quantity);

        return products.stream()
                .map(product -> mapper.convert(product,new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.retrieveProductListByCategory(categoryId)
                .stream().map(product -> mapper.convert(product, new ProductDTO()))
                .collect(Collectors.toList());
    }
}
