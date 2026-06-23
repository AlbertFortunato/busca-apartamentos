package br.com.centralizador.buscaapartamentos;

import br.com.centralizador.buscaapartamentos.config.MercadoLivreProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConfigurationProperties(MercadoLivreProperties.class)
public class BuscaApartamentosApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuscaApartamentosApplication.class, args);
    }
}
