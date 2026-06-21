package br.ufpr.oscar.dto;

import br.ufpr.oscar.entity.Diretor;
import br.ufpr.oscar.entity.Filme;

public class LoginResponse {
    private boolean sucesso;
    private String mensagem;
    private Integer token;
    private boolean votoConfirmado;
    private Filme filme;
    private Diretor diretor;

    public LoginResponse(boolean sucesso, String mensagem, Integer token) {
        this(sucesso, mensagem, token, false, null, null);
    }

    public LoginResponse(boolean sucesso, String mensagem, Integer token,
                         boolean votoConfirmado, Filme filme, Diretor diretor) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.token = token;
        this.votoConfirmado = votoConfirmado;
        this.filme = filme;
        this.diretor = diretor;
    }

    public boolean isSucesso()   { return sucesso; }
    public String getMensagem()  { return mensagem; }
    public Integer getToken()    { return token; }
    public boolean isVotoConfirmado() { return votoConfirmado; }
    public Filme getFilme() { return filme; }
    public Diretor getDiretor() { return diretor; }
}
