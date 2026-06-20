package br.ufpr.oscar.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VotoRequest {

    private Integer token;

    @JsonAlias({"idFilme", "id_filme"})
    private Long filmeId;

    @JsonAlias({"idDiretor", "id_diretor"})
    private Long diretorId;

    public VotoRequest() {}

    public VotoRequest(Integer token, Long filmeId, Long diretorId) {
        this.token = token;
        this.filmeId = filmeId;
        this.diretorId = diretorId;
    }

    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    public Long getFilmeId() {
        return filmeId;
    }

    public void setFilmeId(Long filmeId) {
        this.filmeId = filmeId;
    }

    public Long getDiretorId() {
        return diretorId;
    }

    public void setDiretorId(Long diretorId) {
        this.diretorId = diretorId;
    }
}
