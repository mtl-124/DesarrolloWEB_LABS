
package com.tecsup.controller;

import com.tecsup.dto.CategoriaDTO;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegistrarCategoria() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Tecnologia");
        dto.setDescripcion("Productos tecnologicos");

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Tecnologia"));
    }

    @Test
    void testListarCategorias() throws Exception {
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerCategoriaNoExiste() throws Exception {
        mockMvc.perform(get("/api/categorias/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarCategoria() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Categoria actualizada");
        dto.setDescripcion("Descripcion actualizada");

        String respuesta = mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CategoriaDTO guardada =
                objectMapper.readValue(respuesta, CategoriaDTO.class);

        dto.setNombre("Categoria modificada");

        mockMvc.perform(put("/api/categorias/" + guardada.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Categoria modificada"));
    }

    @Test
    void testEliminarCategoria() throws Exception {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Categoria para eliminar");
        dto.setDescripcion("Prueba de eliminacion");

        String respuesta = mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CategoriaDTO guardada =
                objectMapper.readValue(respuesta, CategoriaDTO.class);

        Long id = guardada.getId();

        mockMvc.perform(delete("/api/categorias/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/categorias/" + id))
                .andExpect(status().isNotFound());
    }
}
