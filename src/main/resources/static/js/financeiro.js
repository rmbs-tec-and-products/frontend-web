document.addEventListener(
    "DOMContentLoaded",
    function () {

        configurarToasts();
        configurarPreviewMovimentacao();
        configurarPeriodo();
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

function configurarPreviewMovimentacao() {

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
        document.querySelectorAll(
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

        const tipoTexto =
            tipoSelecionado === "SAIDA"
                ? "Saída"
                : "Entrada";

        preview.textContent =
            tipoTexto
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

function obterTipoSelecionado(
    radios
) {

    for (const radio of radios) {

        if (radio.checked) {
            return radio.value;
        }
    }

    return "ENTRADA";
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

        if (fim.value < inicio.value) {
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