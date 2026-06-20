package br.ufpr.oscar.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "votos",
    uniqueConstraints = @UniqueConstraint(name = "uk_voto_usuario", columnNames = "usuario_id")
)
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "filme_id", nullable = false)
    private Filme filme;

    @ManyToOne
    @JoinColumn(name = "diretor_id", nullable = false)
    private Diretor diretor;

    @Column(name = "registrado_em", nullable = false)
    private LocalDateTime registradoEm;

    public Voto() {}

    public Voto(Usuario usuario, Filme filme, Diretor diretor) {
        this.usuario = usuario;
        this.filme = filme;
        this.diretor = diretor;
    }

    @PrePersist
    public void prePersist() {
        if (registradoEm == null) {
            registradoEm = LocalDateTime.now();
        }
    }

    public Long getId()         { return id; }
    public Usuario getUsuario() { return usuario; }
    public Filme getFilme()     { return filme; }
    public Diretor getDiretor() { return diretor; }
    public LocalDateTime getRegistradoEm() { return registradoEm; }
}
