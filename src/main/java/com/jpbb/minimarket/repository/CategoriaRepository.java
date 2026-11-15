package com.jpbb.minimarket.repository;

import com.jpbb.minimarket.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}