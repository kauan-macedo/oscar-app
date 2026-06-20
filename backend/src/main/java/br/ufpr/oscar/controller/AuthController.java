package br.ufpr.oscar.controller;

import br.ufpr.oscar.dto.LoginRequest;
import br.ufpr.oscar.dto.LoginResponse;
import br.ufpr.oscar.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService a) {
        this.authService = a;
    }

    /**
     * POST /login
     *
     * Request:
     *   { "login": "alice", "senha": "senha123" }
     *
     * Response 200:
     *   { "sucesso": true, "mensagem": "Login realizado com sucesso.", "token": 42 }
     *
     * Response 401:
     *   { "sucesso": false, "mensagem": "Usuário ou senha incorretos.", "token": null }
     *
     * Response 400:
     *   { "sucesso": false, "mensagem": "Login e senha são obrigatórios.", "token": null }
     */
    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        // 400 — campos ausentes ou em branco
        if (request.getLogin() == null || request.getLogin().isBlank() ||
            request.getSenha() == null || request.getSenha().isBlank()) {

            return ResponseEntity.badRequest()
                    .body(new LoginResponse(false, "Login e senha são obrigatórios.", null));
        }

        int token = authService.autenticar(request.getLogin(), request.getSenha());

        // 401 — credenciais inválidas
        if (token == -1) {
            return ResponseEntity.status(401)
                    .body(new LoginResponse(false, "Usuário ou senha incorretos.", null));
        }

        // 200 — sucesso
        return ResponseEntity.ok(
                new LoginResponse(true, "Login realizado com sucesso.", token));
    }
}