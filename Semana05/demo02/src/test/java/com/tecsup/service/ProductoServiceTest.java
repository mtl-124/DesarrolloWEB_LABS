
package com.tecsup.service;

import com.tecsup.model.Producto;
import com.tecsup.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductoServiceTest {

    @Mock
    private ProductoRepository repo;

    @InjectMocks
    private ProductoService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // TEST LISTAR
    @Test
    void testListarProductos() {

        List<Producto> lista = List.of(
                new Producto(1L, "Monitor", 500, 10, "Tecnologia")
        );

        when(repo.findAll()).thenReturn(lista);

        List<Producto> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("Monitor", resultado.get(0).getNombre());

        verify(repo, times(1)).findAll();
    }

    // TEST GUARDAR
    @Test
    void testGuardarProducto() {

        Producto producto = new Producto(
                1L, "Laptop", 3000, 5, "Computacion"
        );

        when(repo.save(producto)).thenReturn(producto);

        Producto resultado = service.guardar(producto);

        assertNotNull(resultado);
        assertEquals("Laptop", resultado.getNombre());

        verify(repo).save(producto);
    }

    // TEST OBTENER
    @Test
    void testObtenerProducto() {

        Producto producto = new Producto(
                1L, "Teclado", 100, 20, "Perifericos"
        );

        when(repo.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = service.obtener(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Teclado", resultado.get().getNombre());

        verify(repo).findById(1L);
    }

    // TEST OBTENER PRODUCTO QUE NO EXISTE
    @Test
    void testObtenerProductoNoExiste() {

        when(repo.findById(99L)).thenReturn(Optional.empty());

        Optional<Producto> resultado = service.obtener(99L);

        assertTrue(resultado.isEmpty());

        verify(repo).findById(99L);
    }

    // TEST ELIMINAR
    @Test
    void testEliminarProducto() {

        doNothing().when(repo).deleteById(1L);

        service.eliminar(1L);

        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void testActualizarProducto() {
        Producto existente = new Producto(
                1L, "Monitor", 500, 10, "Tecnologia"
        );
        Producto actualizado = new Producto(
                1L, "Monitor Samsung", 700, 15, "Tecnologia"
        );
        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.save(existente)).thenReturn(existente);

        Producto resultado = service.actualizar(1L, actualizado);

        assertNotNull(resultado);
        assertEquals("Monitor Samsung", resultado.getNombre());
        assertEquals(700, resultado.getPrecio());
        assertEquals(15, resultado.getStock());

        verify(repo).findById(1L);
        verify(repo).save(existente);
    }
    @Test
    void testActualizarProductoNoExiste() {
        Producto actualizado = new Producto(
                99L, "Monitor", 500, 10, "Tecnologia"
        );

        when(repo.findById(99L)).thenReturn(Optional.empty());

        Producto resultado = service.actualizar(99L, actualizado);

        assertNull(resultado);
        verify(repo).findById(99L);
        verify(repo, never()).save(any(Producto.class));
    }
}
