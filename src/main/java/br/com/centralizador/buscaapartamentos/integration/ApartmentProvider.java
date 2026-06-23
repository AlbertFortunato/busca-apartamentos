package br.com.centralizador.buscaapartamentos.integration;

import br.com.centralizador.buscaapartamentos.dto.SearchCriteria;
import br.com.centralizador.buscaapartamentos.model.ProviderSearchResult;

public interface ApartmentProvider {

    ProviderSearchResult search(SearchCriteria criteria);
}
