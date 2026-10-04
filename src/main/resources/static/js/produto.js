document.addEventListener(
    "DOMContentLoaded",
    function () {

        configurarToasts();
        configurarResumoProduto();
        configurarAlertaEstoque();
    }
);

function configurarToasts() {

    document.querySelectorAll(
        ".produto-toast"
    ).forEach(
        function (toast) {

            const close =
                toast.querySelector(
                    ".produto-toast-close"
                );

            if (close) {

                close.addEventListener(
                    "click",
                    function () {
                        toast.remove();
                    }
                );
            }

            window.setTimeout(
                function () {

                    if (toast.isConnected) {
                        toast.remove();
                    }
                },
                6500
            );
        }
    );
}

function configurarResumoProduto() {

    const nome =
        document.getElementById(
            "nome"
        );

    const unidade =
        document.getElementById(
            "unidade"
        );

    const quantidade =
        document.getElementById(
            "quantidade"
        );

    const valor =
        document.getElementById(
            "valor"
        );

    const fornecedor =
        document.getElementById(
            "fornecedorCodigo"
        );

    const preview =
        document.getElementById(
            "product-preview-text"
        );

    if (!preview) {
        return;
    }

    function atualizar() {

        const nomeValor =
            nome && nome.value.trim()
                ? nome.value.trim()
                : "Produto sem nome";

        const quantidadeValor =
            quantidade && quantidade.value !== ""
                ? quantidade.value
                : "0";

        const unidadeValor =
            unidade && unidade.value.trim()
                ? unidade.value.trim()
                : "un.";

        const valorFormatado =
            formatarMoeda(
                valor
                    ? valor.value
                    : null
            );

        const fornecedorNome =
            obterTextoSelecionado(
                fornecedor,
                "Sem fornecedor"
            );

        preview.textContent =
            nomeValor
            + " • Estoque: "
            + quantidadeValor
            + " "
            + unidadeValor
            + " • "
            + valorFormatado
            + " • "
            + fornecedorNome;
    }

    [
        nome,
        unidade,
        quantidade,
        valor,
        fornecedor
    ].forEach(
        function (campo) {

            if (!campo) {
                return;
            }

            campo.addEventListener(
                "input",
                atualizar
            );

            campo.addEventListener(
                "change",
                atualizar
            );
        }
    );

    atualizar();
}

function configurarAlertaEstoque() {

    const quantidade =
        document.getElementById(
            "quantidade"
        );

    const minimo =
        document.getElementById(
            "quantidadeMinima"
        );

    const alerta =
        document.getElementById(
            "stock-alert"
        );

    const texto =
        document.getElementById(
            "stock-alert-text"
        );

    if (!quantidade
            || !minimo
            || !alerta
            || !texto) {
        return;
    }

    function atualizar() {

        const estoque =
            Number(
                quantidade.value || 0
            );

        const estoqueMinimo =
            Number(
                minimo.value || 0
            );

        const possuiMinimo =
            minimo.value !== "";

        const estoqueBaixo =
            possuiMinimo
            && estoque <= estoqueMinimo;

        alerta.classList.toggle(
            "visible",
            estoqueBaixo
        );

        if (estoqueBaixo) {

            texto.textContent =
                "O estoque atual é "
                + estoque
                + " e atingiu o limite mínimo de "
                + estoqueMinimo
                + ".";
        }
    }

    quantidade.addEventListener(
        "input",
        atualizar
    );

    minimo.addEventListener(
        "input",
        atualizar
    );

    atualizar();
}

function obterTextoSelecionado(
    select,
    fallback
) {

    if (!select
            || !select.value) {
        return fallback;
    }

    const option =
        select.options[
            select.selectedIndex
        ];

    if (!option) {
        return fallback;
    }

    return option.textContent.trim();
}

function formatarMoeda(
    value
) {

    if (value === null
            || value === undefined
            || value === "") {

        return "R$ 0,00";
    }

    const numero =
        Number(
            String(value)
                .replace(",", ".")
        );

    if (Number.isNaN(numero)) {
        return "R$ 0,00";
    }

    return numero.toLocaleString(
        "pt-BR",
        {
            style: "currency",
            currency: "BRL"
        }
    );
}