package com.example.bussinessSystem.Repositories;

import com.example.bussinessSystem.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
