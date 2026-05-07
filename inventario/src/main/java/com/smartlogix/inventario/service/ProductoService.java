package com.smartlogix.inventario.service;


import com.smartlogix.inventario.model.Producto;
import com.smartlogix.inventario.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // Lombok inyecta automáticamente el repositorio
public class ProductoService {

    private final ProductoRepository productoRepository;

    // Método para obtener todos los productos
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // Método para guardar un nuevo producto
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // Método para actualizar un producto existente
    public Producto actualizarProducto(Long id, Producto productoDetalles) {
        // Buscamos si el producto existe. Si existe, lo actualizamos; si no, lanzamos un error.
        return productoRepository.findById(id).map(productoExistente -> {
            productoExistente.setNombre(productoDetalles.getNombre());
            productoExistente.setStock(productoDetalles.getStock());
            productoExistente.setPrecio(productoDetalles.getPrecio());
            // Guardamos los cambios en la base de datos
            return productoRepository.save(productoExistente);
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado con el ID: " + id));
    }

    
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
    }
