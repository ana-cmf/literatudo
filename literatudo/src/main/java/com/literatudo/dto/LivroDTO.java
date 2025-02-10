package com.literatudo.dto;

import com.literatudo.model.Livro;

public record LivroDTO(String isbn, String titulo, AutorDTO autor, int ano) {

    public LivroDTO(Livro livro) {
        this(livro.getIsbn(), livro.getTitulo(), new AutorDTO(livro.getAutor()), livro.getAno());
    }
}
