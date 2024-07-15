package com.example.demo.repository;

import com.example.demo.model.Idioma;
import com.example.demo.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByIdioma(Idioma idioma);

    boolean existsByTitulo(String titulo);

}
