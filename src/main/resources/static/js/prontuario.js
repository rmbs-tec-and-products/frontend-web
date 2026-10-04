document.addEventListener(
    "DOMContentLoaded",
    function () {

        configurarToasts();
        configurarUpload();
    }
);

function configurarToasts() {

    document.querySelectorAll(
        ".prontuario-toast"
    ).forEach(
        function (toast) {

            const close =
                toast.querySelector(
                    ".prontuario-toast-close"
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

function configurarUpload() {

    const arquivo =
        document.getElementById(
            "arquivo"
        );

    const drop =
        document.querySelector(
            ".file-drop"
        );

    const selecionado =
        document.getElementById(
            "selected-file"
        );

    const nome =
        document.getElementById(
            "selected-file-name"
        );

    const tamanho =
        document.getElementById(
            "selected-file-size"
        );

    if (!arquivo
            || !drop) {

        return;
    }

    arquivo.addEventListener(
        "change",
        function () {

            atualizarArquivoSelecionado();
        }
    );

    drop.addEventListener(
        "dragover",
        function (event) {

            event.preventDefault();

            drop.classList.add(
                "dragging"
            );
        }
    );

    drop.addEventListener(
        "dragleave",
        function () {

            drop.classList.remove(
                "dragging"
            );
        }
    );

    drop.addEventListener(
        "drop",
        function (event) {

            event.preventDefault();

            drop.classList.remove(
                "dragging"
            );

            if (!event.dataTransfer
                    || !event.dataTransfer.files
                    || event.dataTransfer.files.length === 0) {

                return;
            }

            arquivo.files =
                event.dataTransfer.files;

            atualizarArquivoSelecionado();
        }
    );

    function atualizarArquivoSelecionado() {

        if (!arquivo.files
                || arquivo.files.length === 0) {

            if (selecionado) {

                selecionado.classList.remove(
                    "visible"
                );
            }

            return;
        }

        const file =
            arquivo.files[0];

        if (file.size > 20 * 1024 * 1024) {

            alert(
                "O arquivo deve possuir no máximo 20 MB."
            );

            arquivo.value =
                "";

            if (selecionado) {

                selecionado.classList.remove(
                    "visible"
                );
            }

            return;
        }

        if (nome) {

            nome.textContent =
                file.name;
        }

        if (tamanho) {

            tamanho.textContent =
                formatarTamanho(
                    file.size
                );
        }

        if (selecionado) {

            selecionado.classList.add(
                "visible"
            );
        }
    }
}

function formatarTamanho(
    bytes
) {

    if (bytes < 1024) {

        return bytes + " B";
    }

    if (bytes < 1024 * 1024) {

        return (
            bytes / 1024
        ).toLocaleString(
            "pt-BR",
            {
                maximumFractionDigits: 1
            }
        ) + " KB";
    }

    return (
        bytes / (
            1024 * 1024
        )
    ).toLocaleString(
        "pt-BR",
        {
            maximumFractionDigits: 1
        }
    ) + " MB";
}