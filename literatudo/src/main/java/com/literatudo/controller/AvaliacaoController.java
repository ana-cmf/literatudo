package com.literatudo.controller;

import java.util.List;

import com.literatudo.dto.AvaliacaoDTO;
import com.literatudo.dto.LivroDTO;
import com.literatudo.exception.AvaliacaoInvalidaException;
import com.literatudo.model.Avaliacao;
import com.literatudo.model.Livro;
import com.literatudo.repository.AvaliacaoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AvaliacaoController {

    private AvaliacaoRepository avaliacaoRepository;

    public AvaliacaoDTO avaliarLivro(AvaliacaoDTO avaliacaoDTO) throws AvaliacaoInvalidaException {
        if(avaliacaoDTO.livro() != null && !avaliacaoDTO.conteudo().isEmpty()){
            return new AvaliacaoDTO(avaliacaoRepository.save(new Avaliacao(avaliacaoDTO)));
        }else{
            throw new AvaliacaoInvalidaException("Formato da avaliação inválido.");
        }
    }

    public List<AvaliacaoDTO> listarAvaliacoesDoLivro(LivroDTO livro){
        List<AvaliacaoDTO> avaliacoesDoLivro = avaliacaoRepository.findByLivro(new Livro(livro)).stream().map(AvaliacaoDTO::new).toList();
        return avaliacoesDoLivro;
    }
}
