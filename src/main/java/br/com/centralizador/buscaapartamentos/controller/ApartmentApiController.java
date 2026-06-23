package br.com.centralizador.buscaapartamentos.controller;

import br.com.centralizador.buscaapartamentos.dto.SearchCriteria;
import br.com.centralizador.buscaapartamentos.model.ApartmentListing;
import br.com.centralizador.buscaapartamentos.model.ProviderSearchResult;
import br.com.centralizador.buscaapartamentos.service.ApartmentSearchService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/apartamentos")
public class ApartmentApiController {

    private final ApartmentSearchService apartmentSearchService;

    public ApartmentApiController(ApartmentSearchService apartmentSearchService) {
        this.apartmentSearchService = apartmentSearchService;
    }

    @GetMapping
    public List<ApartmentListing> search(@Valid @ModelAttribute SearchCriteria criteria) {
        return apartmentSearchService.search(criteria);
    }

    @GetMapping("/provedores")
    public List<ProviderSearchResult> providers(@Valid @ModelAttribute SearchCriteria criteria) {
        return apartmentSearchService.searchByProvider(criteria);
    }
}
