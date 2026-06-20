package br.ufpr.oscar.dto;

public class LoginResponse {
    private boolean sucesso;
    private String mensagem;
    private Integer token;

    public LoginResponse(boolean sucesso, String mensagem, Integer token) {
        this.sucesso  = sucesso;
        this.mensagem = mensagem;
        this.token    = token;
    }

    public boolean isSucesso()   { return sucesso; }
    public String getMensagem()  { return mensagem; }
    public Integer getToken()    { return token; }
}