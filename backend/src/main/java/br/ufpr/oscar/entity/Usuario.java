package br.ufpr.oscar.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "usuarios",
    indexes = @Index(name = "idx_usuario_login", columnList = "login"),
    uniqueConstraints = @UniqueConstraint(name = "uk_usuario_login", columnNames = "login")
)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String login;

    @Column(nullable = false, length = 255)
    private String senha;

    public Usuario() {}

    public Usuario(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    public Long getId()             { return id; }
    public String getLogin()        { return login; }
    public String getSenha()        { return senha; }
    public void setSenha(String s)  { this.senha = s; }
}