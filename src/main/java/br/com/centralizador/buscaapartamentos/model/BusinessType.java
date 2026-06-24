package br.com.centralizador.buscaapartamentos.model;

public enum BusinessType {
    ALUGUEL("aluguel", "alugar"),
    COMPRA("compra", "comprar");

    private final String queryTerm;
    private final String actionLabel;

    BusinessType(String queryTerm, String actionLabel) {
        this.queryTerm = queryTerm;
        this.actionLabel = actionLabel;
    }

    public String getQueryTerm() {
        return queryTerm;
    }

    public String getActionLabel() {
        return actionLabel;
    }
}
