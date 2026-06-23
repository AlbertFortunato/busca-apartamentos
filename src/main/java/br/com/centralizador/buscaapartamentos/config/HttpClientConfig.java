package br.com.centralizador.buscaapartamentos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class HttpClientConfig {

    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .requestInterceptor((request, body, execution) -> {
                    request.getHeaders().set("User-Agent", "BuscaApartamentos/0.1");
                    return execution.execute(request, body);
                });
    }
}
