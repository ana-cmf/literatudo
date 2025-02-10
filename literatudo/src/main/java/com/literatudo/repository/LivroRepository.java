package com.literatudo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.literatudo.model.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, String>{

    public Optional<Livro> findByTitulo(String titulo);
}
