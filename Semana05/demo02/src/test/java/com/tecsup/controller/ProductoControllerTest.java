
package com.tecsup.controller;

import com.tecsup.dto.ProductoDTO;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // TEST POST
    @Test
    void testGuardarProducto() throws Exception {

        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Mouse");
        dto.setPrecio(80);
        dto.setStock(20);
        dto.setCategoria("Perifericos");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Mouse"));
    }

    // TEST GET
    @Test
    void testListarProductos() throws Exception {

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk());
    }

    // TEST VALIDACIÓN
    @Test
    void testValidacionNombreVacio() throws Exception {

        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("");
        dto.setPrecio(10);
        dto.setStock(2);
        dto.setCategoria("Perifericos");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // TEST PUT: Actualizar un producto
    @Test
    void testActualizarProducto() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Monitor actualizado");
        dto.setPrecio(700);
        dto.setStock(15);
        dto.setCategoria("Tecnologia");

        // Primero registramos un producto
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        // Después obtenemos el ID del producto registrado
        String respuesta = mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProductoDTO[] productos = objectMapper.readValue(
                respuesta, ProductoDTO[].class);

        Long id = productos[productos.length - 1].getId();

        // Actualizamos el producto
        mockMvc.perform(put("/api/productos/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre")
                        .value("Monitor actualizado"));
    }

    // TEST DELETE: Eliminar un producto
    @Test
    void testEliminarProducto() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Producto para eliminar");
        dto.setPrecio(100);
        dto.setStock(5);
        dto.setCategoria("Prueba");

        // Registramos un producto para eliminarlo
        String respuesta = mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProductoDTO guardado = objectMapper.readValue(
                respuesta, ProductoDTO.class);

        Long id = guardado.getId();

        // Eliminamos el producto
        mockMvc.perform(delete("/api/productos/" + id))
                .andExpect(status().isNoContent());

        // Comprobamos que ya no existe
        mockMvc.perform(get("/api/productos/" + id))
                .andExpect(status().isNotFound());
    }

}
