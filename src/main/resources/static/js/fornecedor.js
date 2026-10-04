document.addEventListener(
    "DOMContentLoaded",
    function () {

        configurarToasts();
        configurarResumoFornecedor();
    }
);

function configurarToasts() {

    document.querySelectorAll(
        ".fornecedor-toast"
    ).forEach(
        function (toast) {

            const close =
                toast.querySelector(
                    ".fornecedor-toast-close"
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

function configurarResumoFornecedor() {

    const nome =
        document.getElementById(
            "nome"
        );

    const telefone =
        document.getElementById(
            "telefone"
        );

    const preview =
        document.getElementById(
            "supplier-preview-text"
        );

    const avatar =
        document.getElementById(
            "supplier-preview-avatar"
        );

    if (!preview) {
        return;
    }

    function atualizar() {

        const nomeValor =
            nome && nome.value.trim()
                ? nome.value.trim()
                : "Fornecedor sem nome";

        const telefoneValor =
            telefone && telefone.value.trim()
                ? telefone.value.trim()
                : "Telefone não informado";

        preview.textContent =
            nomeValor
            + " • "
            + telefoneValor;

        if (avatar) {

            avatar.textContent =
                nome && nome.value.trim()
                    ? nome.value
                        .trim()
                        .charAt(0)
                        .toUpperCase()
                    : "F";
        }
    }

    if (nome) {

        nome.addEventListener(
            "input",
            atualizar
        );
    }

    if (telefone) {

        telefone.addEventListener(
            "input",
            atualizar
        );
    }

    atualizar();
}