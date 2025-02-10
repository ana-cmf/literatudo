package com.literatudo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.literatudo.model.Avaliacao;
import com.literatudo.model.Livro;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long>{

    public List<Avaliacao> findByLivro(Livro livro);

}
