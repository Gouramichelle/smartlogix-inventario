package com.smartlogix.inventario;

import com.smartlogix.inventario.controller.ProductoController;
import com.smartlogix.inventario.model.Producto;
import com.smartlogix.inventario.model.ProductoDTO;
import com.smartlogix.inventario.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas del Controlador de Productos")
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    private Producto producto;
    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setSku("SKU-001");
        producto.setNombre("Camiseta");
        producto.setDescripcion("Camiseta talla M");
        producto.setPrecio(9990.0);
        producto.setStock(50);

        productoDTO = new ProductoDTO();
        productoDTO.setId(1L);
        productoDTO.setSku("SKU-001");
        productoDTO.setNombre("Camiseta");
        productoDTO.setDescripcion("Camiseta talla M");
        productoDTO.setPrecio(9990.0);
        productoDTO.setStock(50);
    }

    @Test
    @DisplayName("listarProductos() debe retornar lista completa de productos")
    void listarProductos_debeRetornarListaCompleta() {
        // Arrange
        Producto producto2 = new Producto(2L, "Pantalón", "SKU-002", "Pantalón azul", 100, 25990.0);
        List<Producto> productos = Arrays.asList(producto, producto2);
        when(productoService.obtenerTodos()).thenReturn(productos);

        // Act
        ResponseEntity<List<ProductoDTO>> resultado = productoController.listarProductos();

        // Assert
        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultado.getBody()).hasSize(2);
        assertThat(resultado.getBody().get(0).getNombre()).isEqualTo("Camiseta");
        assertThat(resultado.getBody().get(1).getNombre()).isEqualTo("Pantalón");
        verify(productoService, times(1)).obtenerTodos();
    }

    @Test
    @DisplayName("listarProductos() debe retornar lista vacía cuando no hay productos")
    void listarProductos_debeRetornarListaVacia() {
        // Arrange
        when(productoService.obtenerTodos()).thenReturn(List.of());

        // Act
        ResponseEntity<List<ProductoDTO>> resultado = productoController.listarProductos();

        // Assert
        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultado.getBody()).isEmpty();
        verify(productoService, times(1)).obtenerTodos();
    }

    @Test
    @DisplayName("obtenerPorSku() debe retornar producto cuando existe")
    void obtenerPorSku_debeRetornarProducto() {
        // Arrange
        when(productoService.findBySku("SKU-001")).thenReturn(Optional.of(producto));

        // Act
        ResponseEntity<ProductoDTO> resultado = productoController.obtenerPorSku("SKU-001");

        // Assert
        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultado.getBody()).isNotNull();
        assertThat(resultado.getBody().getNombre()).isEqualTo("Camiseta");
        assertThat(resultado.getBody().getSku()).isEqualTo("SKU-001");
        assertThat(resultado.getBody().getPrecio()).isEqualTo(9990.0);
        assertThat(resultado.getBody().getStock()).isEqualTo(50);
        verify(productoService, times(1)).findBySku("SKU-001");
    }

    @Test
    @DisplayName("obtenerPorSku() debe retornar 404 si no existe")
    void obtenerPorSku_debeRetornar404_cuandoNoExiste() {
        // Arrange
        when(productoService.findBySku("SKU-INEXISTENTE")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ProductoDTO> resultado = productoController.obtenerPorSku("SKU-INEXISTENTE");

        // Assert
        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(productoService, times(1)).findBySku("SKU-INEXISTENTE");
    }

    @Test
    @DisplayName("crearProducto() debe crear un nuevo producto exitosamente")
    void crearProducto_debeRetornar201Created() {
        // Arrange
        Producto nuevoProducto = new Producto(null, "Producto nuevo", "SKU-003", "Descripción", 10, 15000.0);
        Producto productoGuardado = new Producto(3L, "Producto nuevo", "SKU-003", "Descripción", 10, 15000.0);
        
        when(productoService.guardarProducto(any(Producto.class))).thenReturn(productoGuardado);

        // Act
        ResponseEntity<ProductoDTO> resultado = productoController.crearProducto(nuevoProducto);

        // Assert
        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resultado.getBody()).isNotNull();
        assertThat(resultado.getBody().getId()).isEqualTo(3L);
        assertThat(resultado.getBody().getNombre()).isEqualTo("Producto nuevo");
        assertThat(resultado.getBody().getSku()).isEqualTo("SKU-003");
        verify(productoService, times(1)).guardarProducto(any(Producto.class));
    }

    @Test
    @DisplayName("crearProducto() debe retornar 400 si hay error")
    void crearProducto_debeRetornar400_cuandoHayError() {
        // Arrange
        Producto productoConError = new Producto(null, "Producto", "SKU-001", "Descripción", 10, 15000.0);
        
        when(productoService.guardarProducto(any(Producto.class)))
                .thenThrow(new RuntimeException("Ya existe un producto con el SKU: SKU-001"));

        // Act
        ResponseEntity<ProductoDTO> resultado = productoController.crearProducto(productoConError);

        // Assert
        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(productoService, times(1)).guardarProducto(any(Producto.class));
    }

    @Test
    @DisplayName("actualizarProducto() debe actualizar un producto existente")
    void actualizarProducto_debeRetornarProductoActualizado() {
        // Arrange
        ProductoDTO actualizaciones = new ProductoDTO();
        actualizaciones.setNombre("Camiseta XL");
        actualizaciones.setPrecio(11990.0);
        actualizaciones.setStock(30);
        actualizaciones.setDescripcion("Camiseta talla XL");

        Producto productoActualizado = new Producto(1L, "Camiseta XL", "SKU-001", "Camiseta talla XL", 30, 11990.0);
        
        when(productoService.actualizarProducto(1L, actualizaciones)).thenReturn(productoActualizado);

        // Act
        ResponseEntity<ProductoDTO> resultado = productoController.actualizarProducto(1L, actualizaciones);

        // Assert
        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultado.getBody()).isNotNull();
        assertThat(resultado.getBody().getNombre()).isEqualTo("Camiseta XL");
        assertThat(resultado.getBody().getPrecio()).isEqualTo(11990.0);
        assertThat(resultado.getBody().getStock()).isEqualTo(30);
        verify(productoService, times(1)).actualizarProducto(1L, actualizaciones);
    }

    @Test
    @DisplayName("actualizarProducto() debe retornar 404 si no existe")
    void actualizarProducto_debeRetornar404_cuandoNoExiste() {
        // Arrange
        ProductoDTO actualizaciones = new ProductoDTO();
        actualizaciones.setNombre("Producto actualizado");
        
        when(productoService.actualizarProducto(999L, actualizaciones))
                .thenThrow(new RuntimeException("Producto no encontrado con el ID: 999"));

        // Act
        ResponseEntity<ProductoDTO> resultado = productoController.actualizarProducto(999L, actualizaciones);

        // Assert
        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(productoService, times(1)).actualizarProducto(999L, actualizaciones);
    }

    @Test
    @DisplayName("eliminarProducto() debe eliminar un producto exitosamente")
    void eliminarProducto_debeRetornar204NoContent() {
        // Arrange
        doNothing().when(productoService).eliminarProducto(1L);

        // Act
        ResponseEntity<Void> resultado = productoController.eliminarProducto(1L);

        // Assert
        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(productoService, times(1)).eliminarProducto(1L);
    }

    @Test
    @DisplayName("eliminarProducto() debe retornar 500 en caso de error")
    void eliminarProducto_debeRetornar500_cuandoHayError() {
        // Arrange
        doThrow(new RuntimeException("Error al eliminar"))
                .when(productoService).eliminarProducto(999L);

        // Act
        ResponseEntity<Void> resultado = productoController.eliminarProducto(999L);

        // Assert
        assertThat(resultado.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(productoService, times(1)).eliminarProducto(999L);
    }
}
