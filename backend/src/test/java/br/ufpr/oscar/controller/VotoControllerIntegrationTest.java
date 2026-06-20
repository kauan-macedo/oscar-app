package br.ufpr.oscar.controller;

import br.ufpr.oscar.entity.Sessao;
import br.ufpr.oscar.entity.Usuario;
import br.ufpr.oscar.repository.SessaoRepository;
import br.ufpr.oscar.repository.UsuarioRepository;
import br.ufpr.oscar.repository.VotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VotoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private SessaoRepository sessaoRepo;

    @Autowired
    private VotoRepository votoRepo;

    @Test
    void tokenValidoEUsuarioSemVotoRegistraVoto() throws Exception {
        Usuario usuario = usuarioRepo.findByLogin("bob").orElseThrow();
        sessaoRepo.save(new Sessao(usuario, 77));
        long votosAntes = votoRepo.count();

        mockMvc.perform(post("/voto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":77,\"filmeId\":3,\"diretorId\":4}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucesso", is(true)))
                .andExpect(jsonPath("$.mensagem", is("Voto registrado com sucesso.")));

        assertEquals(votosAntes + 1, votoRepo.count());
    }

    @Test
    void tokenInvalidoNaoRegistraVoto() throws Exception {
        long votosAntes = votoRepo.count();

        mockMvc.perform(post("/voto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":999,\"filmeId\":3,\"diretorId\":4}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.sucesso", is(false)))
                .andExpect(jsonPath("$.mensagem", is("Token inválido.")));

        assertEquals(votosAntes, votoRepo.count());
    }

    @Test
    void usuarioQueJaVotouRecebeErroENaoAlteraVotoAnterior() throws Exception {
        long votosAntes = votoRepo.count();

        mockMvc.perform(post("/voto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":42,\"filmeId\":3,\"diretorId\":4}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.sucesso", is(false)))
                .andExpect(jsonPath("$.mensagem", is("Usuário já possui voto registrado.")));

        assertEquals(votosAntes, votoRepo.count());
    }

    @Test
    void idsInvalidosNaoRegistramVoto() throws Exception {
        Usuario usuario = usuarioRepo.findByLogin("carol").orElseThrow();
        sessaoRepo.save(new Sessao(usuario, 78));
        long votosAntes = votoRepo.count();

        mockMvc.perform(post("/voto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":78,\"filmeId\":999,\"diretorId\":4}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.sucesso", is(false)))
                .andExpect(jsonPath("$.mensagem", is("Filme inválido.")));

        assertEquals(votosAntes, votoRepo.count());
    }

    @Test
    void requestInvalidoRetornaJsonComErroClaro() throws Exception {
        mockMvc.perform(post("/voto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":77,\"diretorId\":4}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.sucesso", is(false)))
                .andExpect(jsonPath("$.mensagem", is("filmeId é obrigatório.")));
    }
}
