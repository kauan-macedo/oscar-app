package br.ufpr.oscar.service;

import br.ufpr.oscar.dto.VotoRequest;
import br.ufpr.oscar.entity.Diretor;
import br.ufpr.oscar.entity.Filme;
import br.ufpr.oscar.entity.Sessao;
import br.ufpr.oscar.entity.Usuario;
import br.ufpr.oscar.entity.Voto;
import br.ufpr.oscar.exception.TokenInvalidoException;
import br.ufpr.oscar.exception.VotoInvalidoException;
import br.ufpr.oscar.repository.DiretorRepository;
import br.ufpr.oscar.repository.FilmeRepository;
import br.ufpr.oscar.repository.SessaoRepository;
import br.ufpr.oscar.repository.VotoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VotoService {

    private final SessaoRepository sessaoRepo;
    private final VotoRepository votoRepo;
    private final FilmeRepository filmeRepo;
    private final DiretorRepository diretorRepo;

    public VotoService(SessaoRepository sessaoRepo,
            VotoRepository votoRepo,
            FilmeRepository filmeRepo,
            DiretorRepository diretorRepo) {
        this.sessaoRepo = sessaoRepo;
        this.votoRepo = votoRepo;
        this.filmeRepo = filmeRepo;
        this.diretorRepo = diretorRepo;
    }

    @Transactional
    public Voto registrar(VotoRequest request) {
        validarRequest(request);

        Sessao sessao = sessaoRepo.findByToken(request.getToken())
                .orElseThrow(() -> new TokenInvalidoException("Token inválido."));

        Usuario usuario = sessao.getUsuario();

        // Cada usuario pode confirmar apenas um voto.
        if (votoRepo.existsByUsuario(usuario)) {
            throw new VotoInvalidoException("Usuário já possui voto registrado.");
        }

        Filme filme = filmeRepo.findById(request.getFilmeId())
                .orElseThrow(() -> new VotoInvalidoException("Filme inválido."));

        Diretor diretor = diretorRepo.findById(request.getDiretorId())
                .orElseThrow(() -> new VotoInvalidoException("Diretor inválido."));

        try {
            return votoRepo.saveAndFlush(new Voto(usuario, filme, diretor));
        } catch (DataIntegrityViolationException e) {
            throw new VotoInvalidoException("Usuário já possui voto registrado.", e);
        }
    }

    private void validarRequest(VotoRequest request) {
        if (request == null) {
            throw new VotoInvalidoException("JSON do voto é obrigatório.");
        }

        if (request.getToken() == null) {
            throw new VotoInvalidoException("Token é obrigatório.");
        }

        if (request.getFilmeId() == null) {
            throw new VotoInvalidoException("filmeId é obrigatório.");
        }

        if (request.getDiretorId() == null) {
            throw new VotoInvalidoException("diretorId é obrigatório.");
        }

        if (request.getFilmeId() <= 0) {
            throw new VotoInvalidoException("filmeId inválido.");
        }

        if (request.getDiretorId() <= 0) {
            throw new VotoInvalidoException("diretorId inválido.");
        }
    }
}
