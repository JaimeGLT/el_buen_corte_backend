package com.el_buen_corte.el_buen_corte.product;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long>{
    @Query("select p from Product p where p.minimumStock >= p.initialStock ")
    public List<Product> findProductsWithLowStock();

    @Query("select sum(p.initialStock * p.price) from Product p")
    public double totalProductsValue();

}
