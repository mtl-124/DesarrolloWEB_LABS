package com.tecsup.controller;

import com.tecsup.dto.ProductoDTO;
import com.tecsup.model.Producto;
import com.tecsup.service.ProductoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // Listar todos los productos
    @GetMapping
    public List<ProductoDTO> listar() {
        return service.listar()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Buscar un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtener(@PathVariable Long id) {
        return service.obtener(id)
                .map(this::convertirADTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Buscar productos por nombre
    @GetMapping("/buscar")
    public List<ProductoDTO> buscarPorNombre(
            @RequestParam String nombre) {

        return service.buscarPorNombre(nombre)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Registrar un producto
    @PostMapping
    public ResponseEntity<ProductoDTO> guardar(
            @Valid @RequestBody ProductoDTO dto) {

        Producto producto = convertirAEntidad(dto);
        producto.setId(null);

        Producto guardado = service.guardar(producto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(convertirADTO(guardado));
    }

    // Actualizar un producto
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoDTO dto) {

        return service.obtener(id)
                .map(productoExistente -> {
                    productoExistente.setNombre(dto.getNombre());
                    productoExistente.setPrecio(dto.getPrecio());
                    productoExistente.setStock(dto.getStock());
                    productoExistente.setCategoria(dto.getCategoria());

                    Producto actualizado =
                            service.guardar(productoExistente);

                    return ResponseEntity.ok(
                            convertirADTO(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (service.obtener(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    // Convertir DTO a entidad
    private Producto convertirAEntidad(ProductoDTO dto) {
        Producto producto = new Producto();

        producto.setId(dto.getId());
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setCategoria(dto.getCategoria());

        return producto;
    }

    // Convertir entidad a DTO
    private ProductoDTO convertirADTO(Producto producto) {
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria()
        );
    }
}