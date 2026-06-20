package br.ufpr.oscar.service;

import br.ufpr.oscar.entity.Sessao;
import br.ufpr.oscar.entity.Usuario;
import br.ufpr.oscar.repository.SessaoRepository;
import br.ufpr.oscar.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepo;
    private final SessaoRepository  sessaoRepo;
    private final Random random = new Random();

    public AuthService(UsuarioRepository u, SessaoRepository s) {
        this.usuarioRepo = u;
        this.sessaoRepo  = s;
    }

    @Transactional
    public int autenticar(String login, String senha) {
        Optional<Usuario> opt = usuarioRepo.findByLoginAndSenha(login, senha);
        if (opt.isEmpty()) return -1;

        Usuario usuario = opt.get();
        int token = random.nextInt(101); 

        Sessao sessao = sessaoRepo.findByUsuario(usuario)
                .orElse(new Sessao(usuario, token));
        sessao.setToken(token);
        sessaoRepo.save(sessao);

        return token;
    }
}