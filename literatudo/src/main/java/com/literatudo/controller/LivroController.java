package com.literatudo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.literatudo.dto.LivroDTO;
import com.literatudo.exception.NenhumLivroException;
import com.literatudo.model.Livro;
import com.literatudo.repository.LivroRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;
    

    public List<LivroDTO> listarLivros(){
        List<LivroDTO> livros = livroRepository.findAll().stream().map(LivroDTO::new).toList();
        return livros;
    }

    public LivroDTO buscarLivro(LivroDTO livro) throws NenhumLivroException{
        Optional<Livro> livroEncontrado = null;
        if(!livro.isbn().equals(null)){
            livroEncontrado = livroRepository.findById(livro.isbn());
        }else if(!livro.titulo().equals(null)){
            livroEncontrado = livroRepository.findByTitulo(livro.titulo());
        }

        if(livroEncontrado.isPresent()){
            return new LivroDTO(livroEncontrado.get());
        }else{
            throw new NenhumLivroException("Nenhum livro encontrado.");
        }

    }

}
