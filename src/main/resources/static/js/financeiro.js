document.addEventListener(
    "DOMContentLoaded",
    function () {

        configurarToasts();
        configurarCamposMoedaBrasileira();
        configurarPreviewMovimentacao();
        configurarPeriodo();
        configurarConta();
    }
);

function configurarToasts() {

    document.querySelectorAll(
        ".finance-toast"
    ).forEach(
        function (toast) {

            const close =
                toast.querySelector(
                    ".finance-toast-close"
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

function configurarCamposMoedaBrasileira() {

    document.querySelectorAll(
        "[data-money-br]"
    ).forEach(
        function (campo) {

            campo.addEventListener(
                "input",
                function () {

                    campo.value =
                        normalizarDigitacaoMoeda(
                            campo.value
                        );
                }
            );

            campo.addEventListener(
                "blur",
                function () {

                    const numero =
                        converterMoedaParaNumero(
                            campo.value
                        );

                    if (numero === null) {
                        return;
                    }

                    campo.value =
                        numero.toLocaleString(
                            "pt-BR",
                            {
                                minimumFractionDigits: 2,
                                maximumFractionDigits: 2
                            }
                        );
                }
            );
        }
    );
}

function normalizarDigitacaoMoeda(
    value
) {

    if (!value) {
        return "";
    }

    let texto =
        String(value)
            .replace(/[^\d.,]/g, "");

    if (texto.includes(",")) {

        const partes =
            texto.split(",");

        let inteiro =
            partes.shift()
                .replace(/\./g, "");

        const decimal =
            partes.join("")
                .replace(/\./g, "")
                .substring(0, 2);

        if (inteiro === "") {
            inteiro = "0";
        }

        return inteiro
            + ","
            + decimal;
    }

    const ultimoPonto =
        texto.lastIndexOf(".");

    if (ultimoPonto >= 0) {

        const casasDepoisPonto =
            texto.length
            - ultimoPonto
            - 1;

        if (casasDepoisPonto <= 2) {

            const inteiro =
                texto
                    .substring(
                        0,
                        ultimoPonto
                    )
                    .replace(/\./g, "");

            const decimal =
                texto
                    .substring(
                        ultimoPonto + 1
                    )
                    .replace(/\./g, "")
                    .substring(0, 2);

            return (
                inteiro || "0"
            )
                + ","
                + decimal;
        }

        return texto.replace(
            /\./g,
            ""
        );
    }

    return texto;
}

function configurarPeriodo() {

    const inicio =
        document.getElementById(
            "inicio"
        );

    const fim =
        document.getElementById(
            "fim"
        );

    if (!inicio
            || !fim) {

        return;
    }

    function validar() {

        if (!inicio.value
                || !fim.value) {

            return;
        }

        if (fim.value
                < inicio.value) {

            fim.value =
                inicio.value;
        }

        fim.min =
            inicio.value;
    }

    inicio.addEventListener(
        "change",
        validar
    );

    validar();
}

function configurarPreviewMovimentacao() {

    const form =
        document.getElementById(
            "movement-form"
        );

    if (!form) {
        return;
    }

    const descricao =
        document.getElementById(
            "descricao"
        );

    const valor =
        document.getElementById(
            "valor"
        );

    const preview =
        document.getElementById(
            "movement-preview-text"
        );

    const tipos =
        form.querySelectorAll(
            'input[name="tipo"]'
        );

    if (!preview) {
        return;
    }

    function atualizar() {

        const descricaoValor =
            descricao
            && descricao.value.trim()
                ? descricao.value.trim()
                : "Movimentação sem descrição";

        const tipoSelecionado =
            obterTipoSelecionado(
                tipos
            );

        const valorFormatado =
            formatarMoeda(
                valor
                    ? valor.value
                    : null
            );

        preview.textContent =
            (
                tipoSelecionado === "SAIDA"
                    ? "Saída"
                    : "Entrada"
            )
            + " • "
            + valorFormatado
            + " • "
            + descricaoValor;
    }

    if (descricao) {

        descricao.addEventListener(
            "input",
            atualizar
        );
    }

    if (valor) {

        valor.addEventListener(
            "input",
            atualizar
        );

        valor.addEventListener(
            "blur",
            atualizar
        );
    }

    tipos.forEach(
        function (tipo) {

            tipo.addEventListener(
                "change",
                atualizar
            );
        }
    );

    atualizar();
}

function configurarConta() {

    const form =
        document.getElementById(
            "conta-form"
        );

    if (!form) {
        return;
    }

    const tipos =
        form.querySelectorAll(
            'input[name="tipo"]'
        );

    const recorrente =
        document.getElementById(
            "recorrente"
        );

    const recurringSection =
        document.getElementById(
            "recurring-section"
        );

    const descricao =
        document.getElementById(
            "descricaoConta"
        );

    const valor =
        document.getElementById(
            "valorConta"
        );

    const vencimento =
        document.getElementById(
            "dataVencimento"
        );

    const previewIcon =
        document.getElementById(
            "account-preview-icon"
        );

    const previewDescricao =
        document.getElementById(
            "account-preview-description"
        );

    const previewDetails =
        document.getElementById(
            "account-preview-details"
        );

    function atualizar() {

        const tipo =
            obterTipoSelecionado(
                tipos
            );

        const ehReceber =
            tipo === "RECEBER";

        if (recurringSection) {

            recurringSection.classList.toggle(
                "recurring-disabled",
                ehReceber
            );
        }

        if (ehReceber
                && recorrente) {

            recorrente.checked =
                false;
        }

        if (previewIcon) {

            previewIcon.textContent =
                ehReceber
                    ? "↑"
                    : "↓";

            previewIcon.classList.toggle(
                "preview-pay",
                !ehReceber
            );

            previewIcon.classList.toggle(
                "preview-receive",
                ehReceber
            );
        }

        if (previewDescricao) {

            previewDescricao.textContent =
                descricao
                && descricao.value.trim()
                    ? descricao.value.trim()
                    : (
                        ehReceber
                            ? "Nova conta a receber"
                            : "Nova conta a pagar"
                    );
        }

        if (previewDetails) {

            const valorFormatado =
                formatarMoeda(
                    valor
                        ? valor.value
                        : null
                );

            const data =
                formatarData(
                    vencimento
                        ? vencimento.value
                        : null
                );

            const recorrencia =
                recorrente
                && recorrente.checked
                    ? " • Recorrente"
                    : "";

            previewDetails.textContent =
                valorFormatado
                + " • Vencimento "
                + data
                + recorrencia;
        }
    }

    tipos.forEach(
        function (tipo) {

            tipo.addEventListener(
                "change",
                atualizar
            );
        }
    );

    if (descricao) {

        descricao.addEventListener(
            "input",
            atualizar
        );
    }

    if (valor) {

        valor.addEventListener(
            "input",
            atualizar
        );

        valor.addEventListener(
            "blur",
            atualizar
        );
    }

    if (vencimento) {

        vencimento.addEventListener(
            "change",
            atualizar
        );
    }

    if (recorrente) {

        recorrente.addEventListener(
            "change",
            atualizar
        );
    }

    atualizar();
}

function obterTipoSelecionado(
    radios
) {

    for (const radio of radios) {

        if (radio.checked) {
            return radio.value;
        }
    }

    return "";
}

function converterMoedaParaNumero(
    value
) {

    if (value === null
            || value === undefined) {

        return null;
    }

    let texto =
        String(value)
            .trim()
            .replace("R$", "")
            .replace(/\s/g, "");

    if (texto === "") {
        return null;
    }

    if (texto.includes(",")) {

        texto =
            texto
                .replace(/\./g, "")
                .replace(",", ".");

    } else {

        const ultimoPonto =
            texto.lastIndexOf(".");

        if (ultimoPonto >= 0) {

            const casasDepoisPonto =
                texto.length
                - ultimoPonto
                - 1;

            if (casasDepoisPonto > 2) {

                texto =
                    texto.replace(
                        /\./g,
                        ""
                    );
            }
        }
    }

    const numero =
        Number(
            texto
        );

    if (Number.isNaN(numero)) {
        return null;
    }

    return numero;
}

function formatarMoeda(
    value
) {

    const numero =
        converterMoedaParaNumero(
            value
        );

    if (numero === null) {
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

function formatarData(
    value
) {

    if (!value) {
        return "--/--/----";
    }

    const partes =
        value.split(
            "-"
        );

    if (partes.length !== 3) {
        return value;
    }

    return partes[2]
        + "/"
        + partes[1]
        + "/"
        + partes[0];
}