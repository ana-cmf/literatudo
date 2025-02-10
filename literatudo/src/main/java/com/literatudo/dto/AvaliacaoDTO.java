package com.literatudo.dto;

import com.literatudo.model.Avaliacao;

public record AvaliacaoDTO(Long id, String conteudo, LivroDTO livro) {
    public AvaliacaoDTO(Avaliacao avaliacao){
        this(avaliacao.getId(), avaliacao.getConteudo(), new LivroDTO(avaliacao.getLivro()));
    }
}
