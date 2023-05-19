package com.microservice.productservice.service;

import com.microservice.productservice.dto.ProductRequest;
import com.microservice.productservice.dto.ProductResponse;
import com.microservice.productservice.model.Product;
import com.microservice.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder().
                name(productRequest.getName()).
                price(productRequest.getPrice()).
                description(productRequest.getDescription()).
                build();
        productRepository.save(product);
        log.info("Product {} is saved",product.getId());
    }

    public List<ProductResponse> getAllProduct() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(product -> mapToProductResponce(product)).collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponce(Product product) {
        ProductResponse response = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
        return response;
    }

    public void updateProduct(ProductRequest productRequest) {

    }
}
