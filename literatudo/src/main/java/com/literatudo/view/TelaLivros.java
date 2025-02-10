package com.literatudo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.literatudo.controller.LivroController;
import com.literatudo.dto.LivroDTO;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Component
public class TelaLivros extends JFrame{

    @Autowired
    private LivroController livroController;

    public TelaLivros() {
        livroController = new LivroController();
        exibir();
    }

    @PostConstruct
    public void exibir(){
        setTitle("Literatudo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setResizable(false);
        setBackground(Color.BLUE);
        //setLayout(null);
        
        List<LivroDTO> livros = livroController.listarLivros();
        
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(livros.size()/3, 3, 10, 10));  // 2 colunas, espaçamento de 10px entre itens
        
        for (LivroDTO livro : livros) {
            JPanel card = new JPanel();
            card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            card.setBackground(Color.LIGHT_GRAY);
            card.add(new JLabel(livro.titulo()));
            card.add(new JLabel(livro.autor().nome()));
            card.add(new JLabel("ISBN: "+livro.isbn()));
            card.add(new JLabel("Ano: "+livro.ano()));
            gridPanel.add(card);
        }
        
        add(new JScrollPane(gridPanel), BorderLayout.CENTER);
        setSize(400, 300);
        setVisible(true);
    }

}
