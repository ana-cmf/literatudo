package com.literatudo.model;

import com.literatudo.dto.LivroDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "LIVRO")
@Entity(name = "livro")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "isbn")
public class Livro {

    @Id
    private String isbn;

    private String titulo;

    private Autor autor;

    private int ano;

    public Livro(LivroDTO livroDTO) {
        this.isbn = livroDTO.isbn();
        this.titulo = livroDTO.titulo();
        this.autor = new Autor(livroDTO.autor()); // Assuming AutorDTO has a getAutorDTO() method
        this.ano = livroDTO.ano();
    }
}
