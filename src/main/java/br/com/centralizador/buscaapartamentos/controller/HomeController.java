package br.com.centralizador.buscaapartamentos.controller;

import br.com.centralizador.buscaapartamentos.dto.SearchCriteria;
import br.com.centralizador.buscaapartamentos.service.ApartmentSearchService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

    private final ApartmentSearchService apartmentSearchService;

    public HomeController(ApartmentSearchService apartmentSearchService) {
        this.apartmentSearchService = apartmentSearchService;
    }

    @GetMapping("/")
    public String index(
            @Valid @ModelAttribute("criteria") SearchCriteria criteria,
            BindingResult bindingResult,
            Model model
    ) {
        if (!bindingResult.hasErrors()) {
            model.addAttribute("apartments", apartmentSearchService.search(criteria));
            model.addAttribute("providers", apartmentSearchService.searchByProvider(criteria));
        }
        return "index";
    }
}
