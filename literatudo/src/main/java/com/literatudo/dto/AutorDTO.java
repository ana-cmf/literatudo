package com.literatudo.dto;

import com.literatudo.model.Autor;

public record AutorDTO(int id, String nome) {
    public AutorDTO(Autor autor){
        this(autor.getId(), autor.getNome());
    }
}
