package com.literatudo.model;

import com.literatudo.dto.AvaliacaoDTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "AVALIACAO")
@Entity(name = "avaliacao")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String conteudo;
    private Livro livro;

    public Avaliacao(AvaliacaoDTO avaliacaoDTO) {
        this.id = avaliacaoDTO.id();
        this.conteudo = avaliacaoDTO.conteudo();
        this.livro = new Livro(avaliacaoDTO.livro());
    }

}
