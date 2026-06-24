package br.com.centralizador.buscaapartamentos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "apartments.mercadolivre")
public class MercadoLivreProperties {

    private String baseUrl = "https://api.mercadolibre.com";
    private String siteId = "MLB";
    private String rentCategoryId = "MLB1459";
    private String saleCategoryId = "MLB1459";
    private String accessToken = "";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getRentCategoryId() {
        return rentCategoryId;
    }

    public void setRentCategoryId(String rentCategoryId) {
        this.rentCategoryId = rentCategoryId;
    }

    public String getSaleCategoryId() {
        return saleCategoryId;
    }

    public void setSaleCategoryId(String saleCategoryId) {
        this.saleCategoryId = saleCategoryId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
