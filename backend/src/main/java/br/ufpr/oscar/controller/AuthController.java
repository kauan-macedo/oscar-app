package br.ufpr.oscar.controller;

import br.ufpr.oscar.dto.LoginRequest;
import br.ufpr.oscar.dto.LoginResponse;
import br.ufpr.oscar.entity.Voto;
import br.ufpr.oscar.repository.VotoRepository;
import br.ufpr.oscar.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;
    private final VotoRepository votoRepository;

    public AuthController(AuthService a, VotoRepository votoRepository) {
        this.authService = a;
        this.votoRepository = votoRepository;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        if (request.getLogin() == null || request.getLogin().isBlank() ||
            request.getSenha() == null || request.getSenha().isBlank()) {

            return ResponseEntity.badRequest()
                    .body(new LoginResponse(false, "Login e senha são obrigatórios.", null));
        }

        Optional<AuthService.Autenticacao> autenticacao =
                authService.autenticar(request.getLogin(), request.getSenha());

        if (autenticacao.isEmpty()) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse(false, "Usuário ou senha incorretos.", null));
        }

        // O app usa estes dados para bloquear edicao quando o usuario ja votou.
        Optional<Voto> voto = votoRepository.findByUsuario(autenticacao.get().usuario());

        return ResponseEntity.ok(
                new LoginResponse(
                        true,
                        "Login realizado com sucesso.",
                        autenticacao.get().token(),
                        voto.isPresent(),
                        voto.map(Voto::getFilme).orElse(null),
                        voto.map(Voto::getDiretor).orElse(null)));
    }
}
