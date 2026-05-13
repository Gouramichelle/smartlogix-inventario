package com.smartlogix.inventario;

import com.smartlogix.inventario.controller.ProductoController;
import com.smartlogix.inventario.repository.ProductoRepository;
import com.smartlogix.inventario.service.ProductoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@DisplayName("Pruebas de la Aplicación Inventario")
class InventarioApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("El contexto de la aplicación debe cargarse correctamente")
    void contextoCarga() {
        // Assert
        assertThat(applicationContext).isNotNull();
    }

    @Test
    @DisplayName("ProductoController debe estar en el contexto de Spring")
    void productoControllerDebeEstarEnContexto() {
        // Assert
        assertThat(applicationContext.containsBean("productoController")).isTrue();
    }

    @Test
    @DisplayName("ProductoService debe estar en el contexto de Spring")
    void productoServiceDebeEstarEnContexto() {
        // Assert
        assertThat(applicationContext.containsBean("productoService")).isTrue();
    }

    @Test
    @DisplayName("ProductoRepository debe estar en el contexto de Spring")
    void productoRepositoryDebeEstarEnContexto() {
        // Assert
        assertThat(applicationContext.containsBean("productoRepository")).isTrue();
    }

    @Test
    @DisplayName("ProductoController debe ser inyectable")
    void productoControllerDebeSerInyectable() {
        // Arrange & Act
        ProductoController controller = applicationContext.getBean(ProductoController.class);

        // Assert
        assertThat(controller).isNotNull();
    }

    @Test
    @DisplayName("ProductoService debe ser inyectable")
    void productoServiceDebeSerInyectable() {
        // Arrange & Act
        ProductoService service = applicationContext.getBean(ProductoService.class);

        // Assert
        assertThat(service).isNotNull();
    }

    @Test
    @DisplayName("ProductoRepository debe ser inyectable")
    void productoRepositoryDebeSerInyectable() {
        // Arrange & Act
        ProductoRepository repository = applicationContext.getBean(ProductoRepository.class);

        // Assert
        assertThat(repository).isNotNull();
    }

    @Test
    @DisplayName("La aplicación debe iniciar sin errores")
    void applicationDebeIniciarSinErrores() {
        // Esta prueba valida que el contexto se carga sin excepciones
        // Assert
        assertThat(applicationContext).isNotNull();
    }

    @Test
    @DisplayName("Todos los beans requeridos deben estar registrados")
    void todosLosBeansCoreDebenEstarRegistrados() {
        // Assert
        assertThat(applicationContext.containsBean("productoController")).isTrue();
        assertThat(applicationContext.containsBean("productoService")).isTrue();
        assertThat(applicationContext.containsBean("productoRepository")).isTrue();
    }
}
