package br.com.centralizador.buscaapartamentos.integration;

import br.com.centralizador.buscaapartamentos.dto.SearchCriteria;
import br.com.centralizador.buscaapartamentos.model.ApartmentListing;
import br.com.centralizador.buscaapartamentos.model.ProviderSearchResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "apartments.demo.enabled", havingValue = "true")
public class PortalLinkProvider implements ApartmentProvider {

    @Override
    public ProviderSearchResult search(SearchCriteria criteria) {
        return new ProviderSearchResult(
                "Portais parceiros",
                buildUnifiedSearchUrl(criteria),
                List.of(
                        sample(criteria, "Apartamento iluminado perto do metro", "VivaReal", 0),
                        sample(criteria, "Studio mobiliado com varanda", "QuintoAndar", 1),
                        sample(criteria, "Dois quartos em rua tranquila", "ZAP Imoveis", 2)
                )
        );
    }

    private String buildUnifiedSearchUrl(SearchCriteria criteria) {
        var location = String.join(" ", criteria.getBairro(), criteria.getCidade()).trim();
        return UriComponentsBuilder
                .fromUriString("https://www.google.com/search")
                .queryParam("q", location + " apartamento " + criteria.getTipo().getQueryTerm())
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
    }

    private ApartmentListing sample(SearchCriteria criteria, String title, String provider, int index) {
        var city = blankToDefault(criteria.getCidade(), "Sao Paulo");
        var neighborhood = blankToDefault(criteria.getBairro(), switch (index) {
            case 1 -> "Vila Mariana";
            case 2 -> "Consolacao";
            default -> "Pinheiros";
        });
        var defaultPrice = criteria.getTipo().getQueryTerm().equals("compra") ? 650000 : 4500;
        var maxPrice = criteria.getPrecoMaximo() == null ? defaultPrice : criteria.getPrecoMaximo();
        var minimum = criteria.getTipo().getQueryTerm().equals("compra") ? 250000 : 1800;
        var rent = BigDecimal.valueOf(Math.max(minimum, maxPrice - (index * 650L)));

        return new ApartmentListing(
                UUID.nameUUIDFromBytes((provider + title + city + neighborhood).getBytes(StandardCharsets.UTF_8)).toString(),
                title,
                neighborhood,
                city,
                rent,
                BigDecimal.valueOf(520 + (index * 130L)),
                criteria.getQuartos() == null || criteria.getQuartos() == 0 ? 1 + index : criteria.getQuartos(),
                criteria.getVagas() == null ? index % 2 : criteria.getVagas(),
                42 + (index * 18),
                Boolean.TRUE.equals(criteria.getAceitaPets()) || index != 1,
                provider,
                "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&w=900&q=80",
                buildProviderSearchUrl(provider, criteria)
        );
    }

    private String buildProviderSearchUrl(String provider, SearchCriteria criteria) {
        var location = String.join(" ", criteria.getBairro(), criteria.getCidade()).trim();
        var query = location.isBlank()
                ? "apartamento " + criteria.getTipo().getQueryTerm()
                : location + " apartamento " + criteria.getTipo().getQueryTerm();

        return switch (provider) {
            case "QuintoAndar" -> UriComponentsBuilder.fromUriString("https://www.quintoandar.com.br/" + criteria.getTipo().getActionLabel() + "/imovel")
                    .queryParam("q", query)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            case "ZAP Imoveis" -> UriComponentsBuilder.fromUriString("https://www.zapimoveis.com.br/" + criteria.getTipo().getQueryTerm() + "/apartamentos/")
                    .queryParam("onde", query)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            default -> UriComponentsBuilder.fromUriString("https://www.vivareal.com.br/" + criteria.getTipo().getQueryTerm() + "/")
                    .queryParam("onde", query)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
        };
    }

    private String blankToDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
