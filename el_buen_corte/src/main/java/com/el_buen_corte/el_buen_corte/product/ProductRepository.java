package com.el_buen_corte.el_buen_corte.product;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
  @Query("select p from Product p where p.minimumStock >= p.initialStock ")
  public List<Product> findProductsWithLowStock();

  @Query("select sum(p.initialStock * p.price) from Product p")
  public double totalProductsValue();

  @Query("""
      SELECT coalesce(sum(p.price * p.initialStock))
      FROM Product p WHERE
        p.creationDate >= :startDate
        AND p.creationDate <= :endDate
      """)
  Double sumTotalPriceCreatedByDate(@Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  @Query("""
          SELECT MONTH(p.creationDate) AS month, SUM(p.price * p.initialStock) AS total
          FROM Product p
          WHERE p.creationDate BETWEEN :startDate AND :endDate
          GROUP BY MONTH(p.creationDate)
          ORDER BY MONTH(p.creationDate)
      """)
  List<Object[]> getMonthlyProductStockValue(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

  @Query("SELECT COUNT(p) FROM Product p WHERE p.initialStock <= p.minimumStock")
  Long lowStockProducts();

  @Query("SELECT p FROM Product p WHERE p.initialStock <= p.minimumStock")
  List<Product> findStockBajo(Pageable pageable);

  // Búsqueda por nombre para el chat
  List<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
