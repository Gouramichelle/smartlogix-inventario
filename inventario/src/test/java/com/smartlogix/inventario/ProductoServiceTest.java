package com.smartlogix.inventario;

import com.smartlogix.inventario.model.Producto;
import com.smartlogix.inventario.model.ProductoDTO;
import com.smartlogix.inventario.repository.ProductoRepository;
import com.smartlogix.inventario.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;
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
        productoDTO.setNombre("Camiseta XL");
        productoDTO.setPrecio(11990.0);
        productoDTO.setStock(30);
    }

    @Test
    @DisplayName("obtenerTodos() retorna lista de productos")
    void obtenerTodos_retornaLista() {

        when(productoRepository.findAll()).thenReturn(List.of(producto));

        var resultado = productoService.obtenerTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getSku()).isEqualTo("SKU-001");
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("guardarProducto() persiste el producto cuando el SKU no existe")
    void guardarProducto_skuNuevo_persisteProducto() {
        // sku no existe aún en bd
        when(productoRepository.existsBySku("SKU-001")).thenReturn(false);
        when(productoRepository.save(producto)).thenReturn(producto);

        var resultado = productoService.guardarProducto(producto);

        assertThat(resultado.getNombre()).isEqualTo("Camiseta");
        verify(productoRepository).existsBySku("SKU-001");
        verify(productoRepository).save(producto);
    }

    @Test
    @DisplayName("guardarProducto() lanza excepción si el SKU ya existe (validación de negocio)")
    void guardarProducto_skuDuplicado_lanzaExcepcion() {
        // sku ya registrado
        when(productoRepository.existsBySku("SKU-001")).thenReturn(true);

        assertThatThrownBy(() -> productoService.guardarProducto(producto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("SKU-001");

        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizarProducto() modifica nombre, precio y stock correctamente")
    void actualizarProducto_modificaCampos() {
        // recibe ProductoDTO según el código real del servicio
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));

        var resultado = productoService.actualizarProducto(1L, productoDTO);

        assertThat(resultado.getNombre()).isEqualTo("Camiseta XL");
        assertThat(resultado.getPrecio()).isEqualTo(11990.0);
        assertThat(resultado.getStock()).isEqualTo(30);
    }

    @Test
    @DisplayName("actualizarProducto() lanza excepción si el producto no existe")
    void actualizarProducto_idInexistente_lanzaExcepcion() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.actualizarProducto(99L, productoDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("eliminarProducto() invoca deleteById una sola vez")
    void eliminarProducto_invocaDeleteById() {
        doNothing().when(productoRepository).deleteById(1L);

        productoService.eliminarProducto(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }
}
