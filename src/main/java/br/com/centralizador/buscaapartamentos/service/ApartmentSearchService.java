package br.com.centralizador.buscaapartamentos.service;

import br.com.centralizador.buscaapartamentos.dto.SearchCriteria;
import br.com.centralizador.buscaapartamentos.integration.ApartmentProvider;
import br.com.centralizador.buscaapartamentos.model.ApartmentListing;
import br.com.centralizador.buscaapartamentos.model.ProviderSearchResult;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ApartmentSearchService {

    private final List<ApartmentProvider> providers;

    public ApartmentSearchService(List<ApartmentProvider> providers) {
        this.providers = providers;
    }

    public List<ProviderSearchResult> searchByProvider(SearchCriteria criteria) {
        return providers.stream()
                .map(provider -> searchSafely(provider, criteria))
                .toList();
    }

    public List<ApartmentListing> search(SearchCriteria criteria) {
        return searchByProvider(criteria).stream()
                .flatMap(result -> result.listings().stream())
                .filter(listing -> criteria.getPrecoMinimo() == null
                        || listing.aluguel().intValue() >= criteria.getPrecoMinimo())
                .filter(listing -> criteria.getPrecoMaximo() == null
                        || listing.aluguel().intValue() <= criteria.getPrecoMaximo())
                .filter(listing -> !Boolean.TRUE.equals(criteria.getAceitaPets()) || listing.aceitaPets())
                .sorted(Comparator.comparing(ApartmentListing::aluguel))
                .toList();
    }

    private ProviderSearchResult searchSafely(ApartmentProvider provider, SearchCriteria criteria) {
        try {
            return provider.search(criteria);
        } catch (RuntimeException exception) {
            return new ProviderSearchResult(provider.getClass().getSimpleName(), "", List.of());
        }
    }
}
