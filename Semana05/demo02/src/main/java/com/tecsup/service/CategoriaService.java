
package com.tecsup.service;

import com.tecsup.model.Categoria;
import com.tecsup.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) {
        this.repo = repo;
    }

    public List<Categoria> listar() {
        return repo.findAll();
    }

    public Optional<Categoria> obtener(Long id) {
        return repo.findById(id);
    }

    public Categoria guardar(Categoria categoria) {
        return repo.save(categoria);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
