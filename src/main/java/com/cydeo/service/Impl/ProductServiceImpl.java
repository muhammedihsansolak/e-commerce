package com.cydeo.service.Impl;

import com.cydeo.dto.ProductDTO;
import com.cydeo.dto.ProductRequest;
import com.cydeo.entity.Product;
import com.cydeo.mapper.Mapper;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
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
    public ProductDTO updateProduct(ProductDTO productDTO) {
        Product foundProduct = productRepository.findById(productDTO.getId()).orElseThrow();
        Product convertedProduct = mapper.convert(productDTO, new Product());
        convertedProduct.setId(foundProduct.getId());
        productRepository.save(convertedProduct);
        return productDTO;
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        productRepository.save( mapper.convert(productDTO, new Product()) );
        return productDTO;
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
