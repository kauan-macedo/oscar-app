package br.ufpr.oscar.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "diretores")
public class Diretor {

    @Id
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    public Diretor() {}

    public Diretor(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public void atualizar(String nome) {
        this.nome = nome;
    }

    public Long getId()     { return id; }
    public String getNome() { return nome; }
}
