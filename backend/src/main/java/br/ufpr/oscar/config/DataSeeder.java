package br.ufpr.oscar.config;

import br.ufpr.oscar.entity.*;
import br.ufpr.oscar.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepo;
    private final SessaoRepository  sessaoRepo;
    private final FilmeRepository   filmeRepo;
    private final DiretorRepository diretorRepo;
    private final VotoRepository    votoRepo;

    public DataSeeder(UsuarioRepository u, SessaoRepository s,
                      FilmeRepository f, DiretorRepository d, VotoRepository v) {
        this.usuarioRepo = u;
        this.sessaoRepo  = s;
        this.filmeRepo   = f;
        this.diretorRepo = d;
        this.votoRepo    = v;
    }

    @Override
    public void run(String... args) {
        seedUsuarios();
        seedFilmes();
        seedDiretores();
        seedVotoConfirmado();
    }


    private void seedUsuarios() {
        if (usuarioRepo.count() > 0) return;   // evita duplicar se ddl-auto=update

        usuarioRepo.save(new Usuario("alice", "senha123"));   // terá voto confirmado
        usuarioRepo.save(new Usuario("bob",   "senha123"));   // sem voto
        usuarioRepo.save(new Usuario("carol", "senha123"));   // sem voto
        usuarioRepo.save(new Usuario("dave",  "senha123"));   // nunca usado
        usuarioRepo.save(new Usuario("eve",   "senha123"));   // nunca usado
    }


    private void seedFilmes() {
        if (filmeRepo.count() > 0) return;

        // Ajuste para bater com o filme.json real em 200.236.3.97
        filmeRepo.save(new Filme(1L,  "Piratas do Caribe", "Aventura",
                "http://200.236.3.97/imagens/piratas.jpeg"));
        filmeRepo.save(new Filme(10L, "La La Land",        "Musical",
                "http://200.236.3.97/imagens/land.jpeg"));
        // Adicione os demais filmes do JSON aqui
    }


    private void seedDiretores() {
        if (diretorRepo.count() > 0) return;

        // Ajuste para bater com o diretor.json real em 200.236.3.97
        diretorRepo.save(new Diretor(1L,  "James Cameron"));
        diretorRepo.save(new Diretor(15L, "Steven Spielberg"));
        // Adicione os demais diretores do JSON aqui
    }


    private void seedVotoConfirmado() {
        if (votoRepo.count() > 0) return;

        Usuario alice   = usuarioRepo.findByLogin("alice").orElseThrow();
        Filme   filme   = filmeRepo.findById(1L).orElseThrow();
        Diretor diretor = diretorRepo.findById(1L).orElseThrow();

        if (sessaoRepo.findByUsuario(alice).isEmpty()) {
            sessaoRepo.save(new Sessao(alice, 42));
        }

        votoRepo.save(new Voto(alice, filme, diretor));
    }
}