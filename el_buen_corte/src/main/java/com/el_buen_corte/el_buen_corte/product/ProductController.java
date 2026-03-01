package com.el_buen_corte.el_buen_corte.product;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ESTILISTA')")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {

        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ESTILISTA')")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ESTILISTA')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ESTILISTA')")
    @GetMapping("/low_stock")
    public ResponseEntity<List<ProductResponse>> getProductsWithLowStock() {
        return ResponseEntity.ok(productService.getAllProductsWithLowStock());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ESTILISTA')")
    @GetMapping("/reports")
    public ResponseEntity<ProductReportResponse> reports() {
        return ResponseEntity.ok(productService.reports());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ESTILISTA')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

}
