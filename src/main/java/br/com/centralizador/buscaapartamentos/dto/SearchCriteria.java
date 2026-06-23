package br.com.centralizador.buscaapartamentos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class SearchCriteria {

    @NotBlank(message = "Informe uma cidade")
    private String cidade = "Sao Paulo";

    private String bairro = "";

    @Min(value = 0, message = "O preco minimo nao pode ser negativo")
    private Integer precoMinimo;

    @Min(value = 0, message = "O preco maximo nao pode ser negativo")
    private Integer precoMaximo = 4500;

    @Min(value = 0, message = "Quartos nao pode ser negativo")
    private Integer quartos = 2;

    @Min(value = 0, message = "Vagas nao pode ser negativo")
    private Integer vagas;

    private Boolean aceitaPets = false;

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getPrecoMinimo() {
        return precoMinimo;
    }

    public void setPrecoMinimo(Integer precoMinimo) {
        this.precoMinimo = precoMinimo;
    }

    public Integer getPrecoMaximo() {
        return precoMaximo;
    }

    public void setPrecoMaximo(Integer precoMaximo) {
        this.precoMaximo = precoMaximo;
    }

    public Integer getQuartos() {
        return quartos;
    }

    public void setQuartos(Integer quartos) {
        this.quartos = quartos;
    }

    public Integer getVagas() {
        return vagas;
    }

    public void setVagas(Integer vagas) {
        this.vagas = vagas;
    }

    public Boolean getAceitaPets() {
        return aceitaPets;
    }

    public void setAceitaPets(Boolean aceitaPets) {
        this.aceitaPets = aceitaPets;
    }
}
