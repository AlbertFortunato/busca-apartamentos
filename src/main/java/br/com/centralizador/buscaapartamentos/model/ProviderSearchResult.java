package br.com.centralizador.buscaapartamentos.model;

import java.util.List;

public record ProviderSearchResult(
        String providerName,
        String searchUrl,
        List<ApartmentListing> listings,
        String status,
        String errorMessage
) {

    public static ProviderSearchResult ok(String providerName, String searchUrl, List<ApartmentListing> listings) {
        return new ProviderSearchResult(providerName, searchUrl, listings, "OK", "");
    }

    public static ProviderSearchResult unavailable(String providerName, String searchUrl, String errorMessage) {
        return new ProviderSearchResult(providerName, searchUrl, List.of(), "ERROR", errorMessage);
    }
}
