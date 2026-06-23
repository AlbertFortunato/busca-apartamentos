package br.com.centralizador.buscaapartamentos.service;

import br.com.centralizador.buscaapartamentos.dto.SearchCriteria;
import br.com.centralizador.buscaapartamentos.integration.ApartmentProvider;
import br.com.centralizador.buscaapartamentos.model.ApartmentListing;
import br.com.centralizador.buscaapartamentos.model.ProviderSearchResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ApartmentSearchServiceTest {

    @Test
    void filtersListingsByMaximumRent() {
        var criteria = new SearchCriteria();
        criteria.setCidade("Sao Paulo");
        criteria.setPrecoMaximo(3200);

        var service = new ApartmentSearchService(List.of(fakeProvider()));

        assertThat(service.search(criteria))
                .isNotEmpty()
                .allSatisfy(listing -> assertThat(listing.aluguel().intValue()).isLessThanOrEqualTo(3200));
    }

    @Test
    void returnsEmptyProviderResultWhenIntegrationFails() {
        var service = new ApartmentSearchService(List.of(criteria -> {
            throw new IllegalStateException("Provider unavailable");
        }));

        assertThat(service.searchByProvider(new SearchCriteria()))
                .singleElement()
                .satisfies(result -> assertThat(result.listings()).isEmpty());
    }

    private ApartmentProvider fakeProvider() {
        return criteria -> new ProviderSearchResult("Fake", "https://example.com", List.of(
                listing("1", 2800),
                listing("2", 4200)
        ));
    }

    private ApartmentListing listing(String id, int rent) {
        return new ApartmentListing(
                id,
                "Apartamento",
                "Pinheiros",
                "Sao Paulo",
                BigDecimal.valueOf(rent),
                BigDecimal.ZERO,
                2,
                1,
                50,
                true,
                "Fake",
                "",
                ""
        );
    }
}
