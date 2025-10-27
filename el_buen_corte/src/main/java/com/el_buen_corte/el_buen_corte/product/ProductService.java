package com.el_buen_corte.el_buen_corte.product;

import java.time.LocalDate;
import java.util.List;

import com.el_buen_corte.el_buen_corte.movement.MovementRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import com.el_buen_corte.el_buen_corte.category.Category;
import com.el_buen_corte.el_buen_corte.category.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MovementRepository movementRepository;

    public ProductResponse createProduct(ProductRequest request) {

        Category category = categoryRepository.findById(request.getCategory())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = Product.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .initialStock(request.getInitialStock())
                .price(request.getPrice())
                .creationDate(LocalDate.now())
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

    public ProductReportResponse reports() {

        LocalDate today = LocalDate.now();

        int totalProducts = productRepository.findAll().size();
        int totalProductWithLowStock = productRepository.findProductsWithLowStock().size();
        double totalProductsValue = productRepository.totalProductsValue();
        int totalMovementsToday = movementRepository.movementsByDate(today, today).size();

        return ProductReportResponse.builder()
                .totalProducts(totalProducts)
                .totalMovementsToday(totalMovementsToday)
                .totalLowStock(totalProductWithLowStock)
                .totalValue(totalProductsValue)
                .build();
    }

    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .initialStock(product.getInitialStock())
                .creationDate(LocalDate.now())
                .minimumStock(product.getMinimumStock())
                .price(product.getPrice())
                .supplier(product.getSupplier())
                .category(product.getCategory())
                .build();
    }

    public List<ProductResponse> getAllProductsWithLowStock() {
        List<Product> products = productRepository.findProductsWithLowStock();
        return products.stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toResponse(product);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        if(request.getCategory() != null) {
            Category category = categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            product.setCategory(category);
        }

        if(request.getName() != null)
            product.setName(request.getName());

        if(request.getBrand() != null)
            product.setBrand(request.getBrand());

        if(request.getInitialStock() != null)
            product.setInitialStock(request.getInitialStock());

        if(request.getMinimumStock() != null)
            product.setMinimumStock(request.getMinimumStock());

        if(request.getPrice() != null)
            product.setPrice(request.getPrice());

        if(request.getSupplier() != null)
            product.setSupplier(request.getSupplier());

        productRepository.save(product);

        return toResponse(product);
    }
}
