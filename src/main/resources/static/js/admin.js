document.addEventListener(
    "DOMContentLoaded",
    function () {

        configurarToasts();
        configurarSenha();
        configurarPreviewUsuario();
    }
);

function configurarToasts() {

    document.querySelectorAll(
        ".admin-toast"
    ).forEach(
        function (toast) {

            const fechar =
                toast.querySelector(
                    ".admin-toast-close"
                );

            if (fechar) {

                fechar.addEventListener(
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

function configurarSenha() {

    const button =
        document.querySelector(
            "[data-password-toggle]"
        );

    const senha =
        document.getElementById(
            "senha"
        );

    if (!button
            || !senha) {
        return;
    }

    button.addEventListener(
        "click",
        function () {

            const mostrar =
                senha.type === "password";

            senha.type =
                mostrar
                    ? "text"
                    : "password";

            button.textContent =
                mostrar
                    ? "Ocultar"
                    : "Mostrar";
        }
    );
}

function configurarPreviewUsuario() {

    const nome =
        document.getElementById(
            "nome"
        );

    const perfil =
        document.getElementById(
            "perfil"
        );

    const avatar =
        document.getElementById(
            "user-preview-avatar"
        );

    const previewNome =
        document.getElementById(
            "user-preview-name"
        );

    const previewAcesso =
        document.getElementById(
            "user-preview-access"
        );

    if (!previewNome
            || !previewAcesso) {
        return;
    }

    function atualizar() {

        const nomeValor =
            nome && nome.value.trim()
                ? nome.value.trim()
                : "Novo usuário";

        const perfilValor =
            perfil
            && perfil.value === "ADMIN"
                ? "Administrador"
                : "Usuário";

        previewNome.textContent =
            nomeValor;

        previewAcesso.textContent =
            "Perfil " + perfilValor;

        if (avatar) {

            avatar.textContent =
                nome && nome.value.trim()
                    ? nome.value
                        .trim()
                        .charAt(0)
                        .toUpperCase()
                    : "U";
        }
    }

    if (nome) {

        nome.addEventListener(
            "input",
            atualizar
        );
    }

    if (perfil) {

        perfil.addEventListener(
            "change",
            atualizar
        );
    }

    atualizar();
}