package com.el_buen_corte.el_buen_corte.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.category.Category;
import com.el_buen_corte.el_buen_corte.category.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponse createProduct(ProductRequest request) {

        Category category = categoryRepository.findById(request.getCategory())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .initialStock(request.getInitialStock())
                .minimumStock(request.getMinimumStock())
                .supplier(request.getSupplier())
                .category(category)
                .build();
        
        productRepository.save(product);

        return toResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .initialStock(product.getInitialStock())
                .minimumStock(product.getMinimumStock())
                .supplier(product.getSupplier())
                .category(product.getCategory())
                .build();
    }

    public List<ProductResponse> getAllProductsWithLowStock() {
        List<Product> products = productRepository.findAll();
        List<Product> productsWithLowStock = products.stream()
                .filter(product -> product.getInitialStock() < product.getMinimumStock())
                .toList();

        return productsWithLowStock.stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toResponse(product);
    }
    
}
