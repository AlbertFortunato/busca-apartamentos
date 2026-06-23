package br.com.centralizador.buscaapartamentos.service;

import br.com.centralizador.buscaapartamentos.dto.SearchCriteria;
import br.com.centralizador.buscaapartamentos.integration.PortalLinkProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApartmentSearchServiceTest {

    @Test
    void filtersListingsByMaximumRent() {
        var criteria = new SearchCriteria();
        criteria.setCidade("Sao Paulo");
        criteria.setPrecoMaximo(3200);

        var service = new ApartmentSearchService(java.util.List.of(new PortalLinkProvider()));

        assertThat(service.search(criteria))
                .isNotEmpty()
                .allSatisfy(listing -> assertThat(listing.aluguel().intValue()).isLessThanOrEqualTo(3200));
    }
}
