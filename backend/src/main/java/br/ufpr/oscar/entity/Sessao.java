package br.ufpr.oscar.entity;

import jakarta.persistence.*;


@Entity
@Table(
    name = "sessoes",
    uniqueConstraints = @UniqueConstraint(name = "uk_sessao_token", columnNames = "token")
)
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false, unique = true)
    private Integer token;

    public Sessao() {}

    public Sessao(Usuario usuario, Integer token) {
        this.usuario = usuario;
        this.token = token;
    }

    public Long getId()         { return id; }
    public Usuario getUsuario() { return usuario; }
    public Integer getToken()   { return token; }
    public void setToken(Integer t) { this.token = t; }
}