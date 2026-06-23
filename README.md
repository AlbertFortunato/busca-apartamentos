# Busca Apartamentos

Aplicacao Spring Boot para centralizar buscas de apartamentos para aluguel em uma unica interface.

## O que ja vem pronto

- Front responsivo com filtros de cidade, bairro, preco, quartos, vagas e aceita pets.
- Backend Java Spring Boot com MVC e API REST.
- Camada de provedores para plugar portais de aluguel.
- Integracoes iniciais por deep link com VivaReal, ZAP Imoveis e QuintoAndar.
- Cards demonstrativos para validar a experiencia enquanto APIs oficiais nao estiverem conectadas.

## Como rodar

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Depois acesse:

```text
http://localhost:8080
```

## API

```http
GET /api/apartamentos?cidade=Sao%20Paulo&bairro=Pinheiros&precoMaximo=4500&quartos=2
```

## Sobre integracoes com portais

VivaReal, ZAP Imoveis, QuintoAndar e portais similares normalmente exigem API oficial, parceria, permissao de uso ou regras especificas para coleta de dados. Por isso, esta versao inicial evita scraping direto e usa deep links para abrir buscas filtradas nos portais.

Para ligar dados reais dentro do app, crie uma implementacao de `ApartmentProvider` usando uma API autorizada e registre como `@Component`.
