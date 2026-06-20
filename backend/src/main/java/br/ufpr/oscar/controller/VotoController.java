package br.ufpr.oscar.controller;

import br.ufpr.oscar.dto.VotoRequest;
import br.ufpr.oscar.dto.VotoResponse;
import br.ufpr.oscar.entity.Voto;
import br.ufpr.oscar.exception.TokenInvalidoException;
import br.ufpr.oscar.exception.VotoInvalidoException;
import br.ufpr.oscar.service.VotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voto")
public class VotoController {

    private final VotoService votoService;

    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @PostMapping
    public ResponseEntity<VotoResponse> registrar(@RequestBody(required = false) VotoRequest request) {
        try {
            Voto voto = votoService.registrar(request);
            return ResponseEntity.ok(
                    VotoResponse.sucesso("Voto registrado com sucesso.", voto.getRegistradoEm()));
        } catch (TokenInvalidoException e) {
            return ResponseEntity.status(401).body(VotoResponse.erro(e.getMessage()));
        } catch (VotoInvalidoException e) {
            return ResponseEntity.badRequest().body(VotoResponse.erro(e.getMessage()));
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<VotoResponse> jsonInvalido() {
        return ResponseEntity.badRequest().body(VotoResponse.erro("JSON inválido."));
    }
}
