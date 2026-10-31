
package com.tecsup.service;

import com.tecsup.model.Categoria;
import com.tecsup.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository repo;

    @InjectMocks
    private CategoriaService service;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(
                1L,
                "Tecnologia",
                "Productos tecnologicos"
        );
    }

    @Test
    void testListarCategorias() {
        when(repo.findAll()).thenReturn(Arrays.asList(categoria));

        List<Categoria> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("Tecnologia", resultado.get(0).getNombre());

        verify(repo).findAll();
    }

    @Test
    void testObtenerCategoriaExiste() {
        when(repo.findById(1L)).thenReturn(Optional.of(categoria));

        Optional<Categoria> resultado = service.obtener(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Tecnologia", resultado.get().getNombre());

        verify(repo).findById(1L);
    }

    @Test
    void testObtenerCategoriaNoExiste() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Optional<Categoria> resultado = service.obtener(99L);

        assertTrue(resultado.isEmpty());

        verify(repo).findById(99L);
    }

    @Test
    void testGuardarCategoria() {
        when(repo.save(categoria)).thenReturn(categoria);

        Categoria resultado = service.guardar(categoria);

        assertNotNull(resultado);
        assertEquals("Tecnologia", resultado.getNombre());

        verify(repo).save(categoria);
    }

    @Test
    void testEliminarCategoria() {
        doNothing().when(repo).deleteById(1L);

        service.eliminar(1L);

        verify(repo).deleteById(1L);
    }
}
