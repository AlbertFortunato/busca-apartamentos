package br.com.centralizador.buscaapartamentos.integration;

import br.com.centralizador.buscaapartamentos.config.MercadoLivreProperties;
import br.com.centralizador.buscaapartamentos.dto.SearchCriteria;
import br.com.centralizador.buscaapartamentos.model.ApartmentListing;
import br.com.centralizador.buscaapartamentos.model.BusinessType;
import br.com.centralizador.buscaapartamentos.model.ProviderSearchResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "apartments.mercadolivre.enabled", havingValue = "true", matchIfMissing = true)
public class MercadoLivreProvider implements ApartmentProvider {

    private static final String PROVIDER_NAME = "Mercado Livre Imoveis";
    private static final String FALLBACK_IMAGE = "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&w=900&q=80";

    private final RestClient restClient;
    private final MercadoLivreProperties properties;

    public MercadoLivreProvider(RestClient.Builder restClientBuilder, MercadoLivreProperties properties) {
        this.properties = properties;
        this.restClient = restClientBuilder.baseUrl(properties.getBaseUrl()).build();
    }

    @Override
    public ProviderSearchResult search(SearchCriteria criteria) {
        var uri = buildSearchUri(criteria);

        JsonNode response = restClient.get()
                .uri(uri)
                .headers(headers -> {
                    if (properties.getAccessToken() != null && !properties.getAccessToken().isBlank()) {
                        headers.setBearerAuth(properties.getAccessToken());
                    }
                })
                .retrieve()
                .body(JsonNode.class);

        return new ProviderSearchResult(PROVIDER_NAME, properties.getBaseUrl() + uri, mapListings(response, criteria));
    }

    private String buildSearchUri(SearchCriteria criteria) {
        var businessType = criteria.getTipo();
        var query = String.join(" ", criteria.getBairro(), criteria.getCidade(), "apartamento", businessType.getQueryTerm()).trim();
        var builder = UriComponentsBuilder
                .fromPath("/sites/{siteId}/search")
                .queryParam("category", categoryIdFor(businessType))
                .queryParam("q", query)
                .queryParam("limit", 24);

        if (criteria.getPrecoMinimo() != null) {
            builder.queryParam("price", criteria.getPrecoMinimo() + "-*");
        }
        if (criteria.getPrecoMaximo() != null) {
            var minimum = criteria.getPrecoMinimo() == null ? "*" : criteria.getPrecoMinimo();
            builder.replaceQueryParam("price", minimum + "-" + criteria.getPrecoMaximo());
        }

        return builder.buildAndExpand(properties.getSiteId())
                .encode(StandardCharsets.UTF_8)
                .toUriString();
    }

    private String categoryIdFor(BusinessType businessType) {
        return businessType == BusinessType.COMPRA ? properties.getSaleCategoryId() : properties.getRentCategoryId();
    }

    private List<ApartmentListing> mapListings(JsonNode response, SearchCriteria criteria) {
        if (response == null || !response.has("results") || !response.get("results").isArray()) {
            return List.of();
        }

        var listings = new ArrayList<ApartmentListing>();
        for (JsonNode item : response.get("results")) {
            listings.add(mapListing(item, criteria));
        }
        return listings;
    }

    private ApartmentListing mapListing(JsonNode item, SearchCriteria criteria) {
        var location = item.path("location");
        var city = firstText(location.path("city").path("name"), criteria.getCidade());
        var neighborhood = firstText(location.path("neighborhood").path("name"), criteria.getBairro());

        return new ApartmentListing(
                text(item, "id", ""),
                text(item, "title", "Apartamento para " + criteria.getTipo().getQueryTerm()),
                blankToDefault(neighborhood, "Bairro nao informado"),
                blankToDefault(city, "Cidade nao informada"),
                money(item.path("price")),
                BigDecimal.ZERO,
                attributeAsInt(item, "BEDROOMS", criteria.getQuartos()),
                attributeAsInt(item, "PARKING_LOTS", criteria.getVagas()),
                attributeAsInt(item, "COVERED_AREA", null),
                attributeAsBoolean(item, "ACCEPTS_PETS"),
                PROVIDER_NAME,
                firstText(item.path("thumbnail"), FALLBACK_IMAGE),
                text(item, "permalink", "")
        );
    }

    private Integer attributeAsInt(JsonNode item, String id, Integer fallback) {
        var value = attributeValue(item, id);
        if (value == null || value.isBlank()) {
            return fallback == null ? 0 : fallback;
        }

        var digits = value.replaceAll("[^0-9]", "");
        if (digits.isBlank()) {
            return fallback == null ? 0 : fallback;
        }
        return Integer.parseInt(digits);
    }

    private boolean attributeAsBoolean(JsonNode item, String id) {
        var value = attributeValue(item, id);
        return value != null && List.of("sim", "yes", "true").contains(value.toLowerCase());
    }

    private String attributeValue(JsonNode item, String id) {
        var attributes = item.path("attributes");
        if (!attributes.isArray()) {
            return null;
        }

        for (JsonNode attribute : attributes) {
            if (id.equalsIgnoreCase(text(attribute, "id", ""))) {
                return text(attribute, "value_name", "");
            }
        }
        return null;
    }

    private BigDecimal money(JsonNode node) {
        if (node == null || !node.isNumber()) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(node.asDouble());
    }

    private String text(JsonNode node, String field, String fallback) {
        return firstText(node.path(field), fallback);
    }

    private String firstText(JsonNode node, String fallback) {
        return node != null && node.isTextual() && !node.asText().isBlank() ? node.asText() : fallback;
    }

    private String blankToDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
