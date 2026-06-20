package br.ufpr.oscar.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "filmes")
public class Filme {

    @Id
    private Long id;      

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(nullable = false, length = 100)
    private String genero;

    @Column(nullable = false, length = 500)
    private String foto;

    public Filme() {}

    public Filme(Long id, String nome, String genero, String foto) {
        this.id = id;
        this.nome = nome;
        this.genero = genero;
        this.foto = foto;
    }

    public Long getId()     { return id; }
    public String getNome() { return nome; }
    public String getGenero() { return genero; }
    public String getFoto() { return foto; }
}