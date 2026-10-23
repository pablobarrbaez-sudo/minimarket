package com.jpbb.minimarket.repository;

import com.jpbb.minimarket.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}