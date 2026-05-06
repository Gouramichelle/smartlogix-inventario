package com.smartlogix.inventario.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartlogix.inventario.model.Producto;
import com.smartlogix.inventario.service.ProductoService;

import lombok.RequiredArgsConstructor;

@RestController // Indica que es un controlador que devuelve JSON (nuestra "Vista")
@RequestMapping("/api/inventario/productos") // La URL base
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // Endpoint GET para listar productos
    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    // Endpoint POST para crear un producto
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardarProducto(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);

    
    }
    // Endpoint PUT para modificar un producto existente por su ID
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto productoDetalles) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, productoDetalles);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            // Si el servicio lanza el error de que no existe, devolvemos un código 404 (Not Found)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // (Bono) Endpoint DELETE para eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.noContent().build(); // Devuelve 204 No Content (Éxito al borrar)
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}