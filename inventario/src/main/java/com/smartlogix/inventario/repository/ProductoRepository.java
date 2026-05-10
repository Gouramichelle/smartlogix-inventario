package com.smartlogix.inventario.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartlogix.inventario.model.Producto;

@Repository // PATRÓN DE DISEÑO: Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
          
    Optional<Producto> findBySku(String sku);
    boolean existsBySku(String sku);
}