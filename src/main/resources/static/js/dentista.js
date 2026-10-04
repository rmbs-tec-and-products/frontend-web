document.addEventListener(
    "DOMContentLoaded",
    function () {

        configurarToasts();
        configurarResumoDentista();
    }
);

function configurarToasts() {

    document.querySelectorAll(
        ".dentista-toast"
    ).forEach(
        function (toast) {

            const close =
                toast.querySelector(
                    ".dentista-toast-close"
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

function configurarResumoDentista() {

    const nome =
        document.getElementById(
            "nome"
        );

    const preview =
        document.getElementById(
            "dentist-preview-text"
        );

    const avatar =
        document.getElementById(
            "dentist-preview-avatar"
        );

    if (!preview) {
        return;
    }

    function atualizar() {

        const nomeValor =
            nome && nome.value.trim()
                ? nome.value.trim()
                : "Dentista sem nome";

        preview.textContent =
            nomeValor
            + " • Disponível para agendamentos";

        if (avatar) {

            avatar.textContent =
                nome && nome.value.trim()
                    ? nome.value
                        .trim()
                        .charAt(0)
                        .toUpperCase()
                    : "D";
        }
    }

    if (nome) {

        nome.addEventListener(
            "input",
            atualizar
        );
    }

    atualizar();
}