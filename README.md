# Busca Apartamentos

Aplicacao Spring Boot para centralizar buscas de apartamentos para aluguel em uma unica interface.

## O que ja vem pronto

- Front responsivo com filtros de compra/aluguel, cidade, bairro, preco, quartos, vagas e aceita pets.
- Backend Java Spring Boot com MVC e API REST.
- Camada de provedores para plugar portais de aluguel.
- Integracao HTTP real com Mercado Livre Imoveis via API.
- Provider demonstrativo opcional, desligado por padrao.

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
GET /api/apartamentos?tipo=ALUGUEL&cidade=Sao%20Paulo&bairro=Pinheiros&precoMaximo=4500&quartos=2
```

Para compra:

```http
GET /api/apartamentos?tipo=COMPRA&cidade=Sao%20Paulo&bairro=Pinheiros&precoMaximo=800000&quartos=2
```

## Sobre integracoes com portais

VivaReal, ZAP Imoveis, QuintoAndar e portais similares normalmente exigem API oficial, parceria, permissao de uso ou regras especificas para coleta de dados. Por isso, esta versao evita scraping direto. A integracao real disponivel no projeto e a do Mercado Livre Imoveis; outros portais devem ser conectados por API autorizada.

Para ligar dados reais dentro do app, crie uma implementacao de `ApartmentProvider` usando uma API autorizada e registre como `@Component`.

## Integracoes reais

### Mercado Livre Imoveis

Ativo por padrao:

```properties
apartments.mercadolivre.enabled=true
apartments.mercadolivre.base-url=https://api.mercadolibre.com
apartments.mercadolivre.site-id=MLB
apartments.mercadolivre.rent-category-id=MLB1459
apartments.mercadolivre.sale-category-id=MLB1459
apartments.mercadolivre.access-token=
```

Se a API exigir token no seu ambiente/conta, preencha `apartments.mercadolivre.access-token`.

Quando o Mercado Livre bloquear a chamada anonima, o endpoint de provedores retorna status `ERROR` com a mensagem do problema:

```http
GET /api/apartamentos/provedores?tipo=ALUGUEL&cidade=Sao%20Paulo
```

Exemplo:

```json
{
  "providerName": "Mercado Livre Imoveis",
  "status": "ERROR",
  "errorMessage": "Mercado Livre retornou HTTP 403. Configure apartments.mercadolivre.access-token ou valide permissao da API."
}
```

### VivaReal, ZAP Imoveis e QuintoAndar

Nao implementei scraping desses portais. Para integracao real com eles, voce precisa de API/parceria autorizada e as credenciais correspondentes. Com isso em maos, basta criar uma classe que implemente `ApartmentProvider` e mapear o JSON retornado para `ApartmentListing`.

### Modo demonstrativo

Para reativar cards ficticios durante desenvolvimento:

```properties
apartments.demo.enabled=true
```
