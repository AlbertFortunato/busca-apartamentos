package br.com.centralizador.buscaapartamentos.controller;

import br.com.centralizador.buscaapartamentos.dto.SearchCriteria;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(
            @ModelAttribute("criteria") SearchCriteria criteria,
            Model model
    ) {
        return "index";
    }
}
