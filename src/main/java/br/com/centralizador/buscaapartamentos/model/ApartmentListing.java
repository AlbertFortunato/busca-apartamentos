package br.com.centralizador.buscaapartamentos.model;

import java.math.BigDecimal;

public record ApartmentListing(
        String id,
        String titulo,
        String bairro,
        String cidade,
        BigDecimal aluguel,
        BigDecimal condominio,
        Integer quartos,
        Integer vagas,
        Integer metragem,
        boolean aceitaPets,
        String provedor,
        String imagemUrl,
        String detalheUrl
) {
}
