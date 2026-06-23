package br.com.centralizador.buscaapartamentos.model;

import java.util.List;

public record ProviderSearchResult(
        String providerName,
        String searchUrl,
        List<ApartmentListing> listings
) {
}
