
package com.tecsup.controller;

import com.tecsup.dto.CategoriaDTO;
import com.tecsup.model.Categoria;
import com.tecsup.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    // Listar todas las categorias
    @GetMapping
    public List<CategoriaDTO> listar() {
        return service.listar()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener una categoria por ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obtener(@PathVariable Long id) {
        return service.obtener(id)
                .map(this::convertirADTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Registrar una categoria
    @PostMapping
    public ResponseEntity<CategoriaDTO> guardar(
            @Valid @RequestBody CategoriaDTO dto) {

        Categoria categoria = convertirAEntidad(dto);
        categoria.setId(null);

        Categoria guardada = service.guardar(categoria);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertirADTO(guardada));
    }

    // Actualizar una categoria
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaDTO dto) {

        return service.obtener(id)
                .map(categoriaExistente -> {
                    categoriaExistente.setNombre(dto.getNombre());
                    categoriaExistente.setDescripcion(dto.getDescripcion());

                    Categoria actualizada = service.guardar(categoriaExistente);

                    return ResponseEntity.ok(convertirADTO(actualizada));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar una categoria
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (service.obtener(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    // Convertir DTO a entidad
    private Categoria convertirAEntidad(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setId(dto.getId());
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return categoria;
    }

    // Convertir entidad a DTO
    private CategoriaDTO convertirADTO(Categoria categoria) {
        return new CategoriaDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }
}
