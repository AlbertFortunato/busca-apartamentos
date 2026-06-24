const form = document.querySelector("#apartment-search-form");
const button = document.querySelector("#search-button");
const grid = document.querySelector("#results-grid");
const empty = document.querySelector("#results-empty");
const count = document.querySelector("#results-count");
const providerBar = document.querySelector("#provider-bar");
const typeInputs = document.querySelectorAll("[name='tipo']");
const maxPriceInput = form.querySelector("[name='precoMaximo']");
const minPriceInput = form.querySelector("[name='precoMinimo']");

const money = new Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency: "BRL",
    maximumFractionDigits: 0
});

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const params = new URLSearchParams(new FormData(form));
    if (!form.querySelector("[name='aceitaPets']").checked) {
        params.set("aceitaPets", "false");
    }

    setLoading(true);
    renderProviders([]);

    try {
        const [apartments, providers] = await Promise.all([
            fetchJson(`/api/apartamentos?${params.toString()}`),
            fetchJson(`/api/apartamentos/provedores?${params.toString()}`)
        ]);

        renderApartments(apartments);
        renderProviders(providers);
    } catch (error) {
        grid.innerHTML = "";
        count.textContent = "0";
        empty.hidden = false;
        empty.textContent = "Nao foi possivel consultar a API agora. Verifique o console ou tente novamente.";
    } finally {
        setLoading(false);
    }
});

typeInputs.forEach((input) => {
    input.addEventListener("change", () => {
        const isPurchase = selectedType() === "COMPRA";
        maxPriceInput.placeholder = isPurchase ? "800000" : "4500";
        minPriceInput.step = isPurchase ? "10000" : "100";
        maxPriceInput.step = isPurchase ? "10000" : "100";
        if (!maxPriceInput.value || maxPriceInput.value === "4500" || maxPriceInput.value === "800000") {
            maxPriceInput.value = isPurchase ? "800000" : "4500";
        }
    });
});

async function fetchJson(url) {
    const response = await fetch(url, {
        headers: {
            "Accept": "application/json"
        }
    });

    if (!response.ok) {
        throw new Error(`API returned ${response.status}`);
    }

    return response.json();
}

function setLoading(isLoading) {
    button.disabled = isLoading;
    button.textContent = isLoading ? "Buscando..." : `Buscar apartamentos para ${selectedType() === "COMPRA" ? "comprar" : "alugar"}`;
    empty.hidden = false;
    empty.textContent = isLoading ? "Consultando API de apartamentos..." : empty.textContent;
}

function renderApartments(apartments) {
    count.textContent = apartments.length;
    grid.innerHTML = apartments.map(renderCard).join("");

    empty.hidden = apartments.length > 0;
    empty.textContent = "Nenhum apartamento encontrado para esses filtros.";
}

function renderProviders(providers) {
    const availableProviders = providers.filter((provider) => provider.searchUrl);
    providerBar.hidden = availableProviders.length === 0;
    providerBar.innerHTML = availableProviders
        .map((provider) => `
            <a href="${escapeAttribute(provider.searchUrl)}" target="_blank" rel="noreferrer">
                Abrir busca em <strong>${escapeHtml(provider.providerName)}</strong>
            </a>
        `)
        .join("");
}

function renderCard(apartment) {
    const condo = Number(apartment.condominio || 0);
    const area = Number(apartment.metragem || 0);
    const detailUrl = apartment.detalheUrl || "#";
    const isPurchase = selectedType() === "COMPRA";

    return `
        <article class="card">
            <img src="${escapeAttribute(apartment.imagemUrl || "/img/apartment-placeholder.svg")}" alt="${escapeAttribute(apartment.titulo)}">
            <div class="card-body">
                <div class="card-top">
                    <span class="provider">${escapeHtml(apartment.provedor || "Portal")}</span>
                    ${apartment.aceitaPets ? '<span class="pet">Pet friendly</span>' : ""}
                </div>
                <h3>${escapeHtml(apartment.titulo || "Apartamento")}</h3>
                <p class="location">${escapeHtml(apartment.bairro || "Bairro nao informado")}, ${escapeHtml(apartment.cidade || "Cidade nao informada")}</p>
                <div class="price">
                    <strong>${money.format(Number(apartment.aluguel || 0))}</strong>
                    <small>${isPurchase ? "Valor de venda" : condo > 0 ? `+ ${money.format(condo)} cond.` : "Condominio nao informado"}</small>
                </div>
                <dl>
                    <div><dt>Quartos</dt><dd>${Number(apartment.quartos || 0)}</dd></div>
                    <div><dt>Vagas</dt><dd>${Number(apartment.vagas || 0)}</dd></div>
                    <div><dt>Area</dt><dd>${area > 0 ? `${area} m2` : "N/I"}</dd></div>
                </dl>
                <a class="details" href="${escapeAttribute(detailUrl)}" target="_blank" rel="noreferrer">Ver no portal</a>
            </div>
        </article>
    `;
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function escapeAttribute(value) {
    return escapeHtml(value);
}

function selectedType() {
    return form.querySelector("[name='tipo']:checked")?.value || "ALUGUEL";
}
