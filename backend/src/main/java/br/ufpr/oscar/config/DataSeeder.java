package br.ufpr.oscar.config;

import br.ufpr.oscar.entity.*;
import br.ufpr.oscar.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final String FILMES_URL = "http://200.236.3.97/filme.json";
    private static final String DIRETORES_URL = "http://200.236.3.97/diretor.json";

    private final UsuarioRepository usuarioRepo;
    private final SessaoRepository sessaoRepo;
    private final FilmeRepository filmeRepo;
    private final DiretorRepository diretorRepo;
    private final VotoRepository votoRepo;
    private final RestTemplate restTemplate = criarRestTemplate();

    public DataSeeder(UsuarioRepository u, SessaoRepository s,
            FilmeRepository f, DiretorRepository d, VotoRepository v) {
        this.usuarioRepo = u;
        this.sessaoRepo = s;
        this.filmeRepo = f;
        this.diretorRepo = d;
        this.votoRepo = v;
    }

    @Override
    public void run(String... args) {
        seedUsuarios();
        seedFilmes();
        seedDiretores();
        seedVotoConfirmado();
    }

    private void seedUsuarios() {
        // Usuarios extras deixam a demonstracao segura mesmo apos varios testes.
        criarUsuarioSeAusente("alice");
        criarUsuarioSeAusente("bob");
        criarUsuarioSeAusente("carol");
        criarUsuarioSeAusente("dave");
        criarUsuarioSeAusente("eve");
        criarUsuarioSeAusente("frank");
        criarUsuarioSeAusente("grace");
        criarUsuarioSeAusente("heidi");
        criarUsuarioSeAusente("ivan");
    }

    private void criarUsuarioSeAusente(String login) {
        if (usuarioRepo.findByLogin(login).isEmpty()) {
            usuarioRepo.save(new Usuario(login, "senha123"));
        }
    }

    private void seedFilmes() {
        // O enunciado pede que filmes venham do JSON externo da Academia.
        carregarFilmes().forEach(item -> {
            Long id = parseId(item.id, "filme");
            Filme filme = filmeRepo.findById(id)
                    .orElseGet(() -> new Filme(id, item.nome, item.genero, item.foto));
            filme.atualizar(item.nome, item.genero, item.foto);
            filmeRepo.save(filme);
        });
    }

    private void seedDiretores() {
        // Mantem o banco alinhado ao arquivo diretor.json.
        carregarDiretores().forEach(item -> {
            Long id = parseId(item.id, "diretor");
            Diretor diretor = diretorRepo.findById(id)
                    .orElseGet(() -> new Diretor(id, item.nome));
            diretor.atualizar(item.nome);
            diretorRepo.save(diretor);
        });
    }

    private void seedVotoConfirmado() {
        if (votoRepo.count() > 0)
            return;

        // Usuario com voto previo para demonstrar a regra de bloqueio.
        Usuario alice = usuarioRepo.findByLogin("alice").orElseThrow();
        Filme filme = filmeRepo.findById(1L).orElseThrow();
        Diretor diretor = diretorRepo.findById(1L).orElseThrow();

        if (sessaoRepo.findByUsuario(alice).isEmpty()) {
            sessaoRepo.save(new Sessao(alice, 42));
        }

        votoRepo.save(new Voto(alice, filme, diretor));
    }

    private List<FilmeJson> carregarFilmes() {
        FilmeJson[] itens = carregar(FILMES_URL, FilmeJson[].class);
        List<FilmeJson> filmes = Arrays.stream(validarItens(itens, "filmes"))
                .filter(Objects::nonNull)
                .filter(item -> item.id != null && item.nome != null && item.genero != null && item.foto != null)
                .toList();

        if (filmes.isEmpty()) {
            throw new IllegalStateException("JSON externo de filmes não retornou itens válidos.");
        }

        return filmes;
    }

    private List<DiretorJson> carregarDiretores() {
        DiretorJson[] itens = carregar(DIRETORES_URL, DiretorJson[].class);
        List<DiretorJson> diretores = Arrays.stream(validarItens(itens, "diretores"))
                .filter(Objects::nonNull)
                .filter(item -> item.id != null && item.nome != null)
                .toList();

        if (diretores.isEmpty()) {
            throw new IllegalStateException("JSON externo de diretores não retornou itens válidos.");
        }

        return diretores;
    }

    private <T> T carregar(String url, Class<T> tipo) {
        try {
            return restTemplate.getForObject(url, tipo);
        } catch (RestClientException e) {
            throw new IllegalStateException("Falha ao carregar JSON externo: " + url, e);
        }
    }

    private <T> T[] validarItens(T[] itens, String recurso) {
        if (itens == null || itens.length == 0) {
            throw new IllegalStateException("JSON externo de " + recurso + " está vazio.");
        }
        return itens;
    }

    private RestTemplate criarRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3));
        factory.setReadTimeout(Duration.ofSeconds(3));
        return new RestTemplate(factory);
    }

    private Long parseId(String valor, String recurso) {
        try {
            return Long.parseLong(valor.trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("ID de " + recurso + " inválido no JSON externo: " + valor, e);
        }
    }

    private static class FilmeJson {
        public String id;
        public String nome;
        public String genero;
        public String foto;

        public FilmeJson() {
        }
    }

    private static class DiretorJson {
        public String id;
        public String nome;

        public DiretorJson() {
        }
    }
}
