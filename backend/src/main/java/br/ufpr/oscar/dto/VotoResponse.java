package br.ufpr.oscar.dto;

import java.time.LocalDateTime;

public class VotoResponse {

    private final boolean sucesso;
    private final String mensagem;
    private final LocalDateTime registradoEm;

    private VotoResponse(boolean sucesso, String mensagem, LocalDateTime registradoEm) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.registradoEm = registradoEm;
    }

    public static VotoResponse sucesso(String mensagem, LocalDateTime registradoEm) {
        return new VotoResponse(true, mensagem, registradoEm);
    }

    public static VotoResponse erro(String mensagem) {
        return new VotoResponse(false, mensagem, null);
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getRegistradoEm() {
        return registradoEm;
    }
}
