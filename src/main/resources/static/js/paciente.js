document.addEventListener(
    "DOMContentLoaded",
    function () {

        configurarCampoDependente(
            "anamnese.algumTratamento",
            "grupo-tratamento"
        );

        configurarCampoDependente(
            "anamnese.hospitalizado",
            "grupo-hospitalizado"
        );

        configurarCampoDependente(
            "anamnese.tomaMedicamento",
            "grupo-medicamento"
        );

        configurarCampoDependente(
            "anamnese.alergiaMedicamentoAnestesico",
            "grupo-alergia"
        );

        configurarCampoDependente(
            "anamnese.cirurgia",
            "grupo-cirurgia"
        );

        configurarCampoDependente(
            "anamnese.fumante",
            "grupo-fumante"
        );

        configurarToasts();
        configurarEstadoMaiusculo();
        configurarAcessoProntuario();
    }
);

function configurarCampoDependente(
    nomeCampo,
    idContainer
) {

    const radios =
        document.getElementsByName(
            nomeCampo
        );

    const container =
        document.getElementById(
            idContainer
        );

    if (!container
            || !radios
            || radios.length === 0) {

        return;
    }

    function atualizar(
        limpar
    ) {

        let valorSelecionado =
            null;

        for (const radio of radios) {

            if (radio.checked) {

                valorSelecionado =
                    radio.value;

                break;
            }
        }

        const mostrar =
            valorSelecionado === "true";

        container.classList.toggle(
            "visible",
            mostrar
        );

        const campos =
            container.querySelectorAll(
                "input, select, textarea"
            );

        campos.forEach(
            function (campo) {

                campo.disabled =
                    !mostrar;

                if (!mostrar
                        && limpar) {

                    if (campo.type === "checkbox"
                            || campo.type === "radio") {

                        campo.checked =
                            false;

                    } else if (campo.type === "number") {

                        campo.value =
                            "0";

                    } else {

                        campo.value =
                            "";
                    }
                }
            }
        );
    }

    for (const radio of radios) {

        radio.addEventListener(
            "change",
            function () {

                atualizar(
                    true
                );
            }
        );
    }

    atualizar(
        false
    );
}

function configurarToasts() {

    document.querySelectorAll(
        ".paciente-toast"
    ).forEach(
        function (toast) {

            const close =
                toast.querySelector(
                    ".paciente-toast-close"
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

function configurarEstadoMaiusculo() {

    const estado =
        document.getElementById(
            "estado"
        );

    if (!estado) {
        return;
    }

    estado.addEventListener(
        "input",
        function () {

            estado.value =
                estado.value
                    .toUpperCase()
                    .slice(
                        0,
                        2
                    );
        }
    );
}

function configurarAcessoProntuario() {

    configurarProntuarioNaLista();
    configurarProntuarioNaEdicao();
}

function configurarProntuarioNaLista() {

    document.querySelectorAll(
        ".patient-table tbody tr"
    ).forEach(
        function (linha) {

            const codigo =
                linha.querySelector(
                    ".patient-code"
                );

            const acoes =
                linha.querySelector(
                    ".table-actions"
                );

            if (!codigo
                    || !acoes) {

                return;
            }

            const pacienteCodigo =
                codigo.textContent.trim();

            if (!pacienteCodigo) {
                return;
            }

            if (acoes.querySelector(
                ".action-prontuario"
            )) {

                return;
            }

            const link =
                document.createElement(
                    "a"
                );

            link.href =
                "/pacientes/"
                + encodeURIComponent(
                    pacienteCodigo
                )
                + "/prontuario";

            link.className =
                "action-link action-edit action-prontuario";

            link.textContent =
                "Prontuário";

            acoes.insertBefore(
                link,
                acoes.firstChild
            );
        }
    );
}

function configurarProntuarioNaEdicao() {

    const codigo =
        document.querySelector(
            'input[name="codigo"]'
        );

    const header =
        document.querySelector(
            ".patient-editor-header"
        );

    const codigoCard =
        document.querySelector(
            ".patient-editor-code"
        );

    if (!codigo
            || !codigo.value
            || !header
            || !codigoCard) {

        return;
    }

    if (header.querySelector(
        ".patient-prontuario-button"
    )) {

        return;
    }

    const link =
        document.createElement(
            "a"
        );

    link.href =
        "/pacientes/"
        + encodeURIComponent(
            codigo.value
        )
        + "/prontuario";

    link.className =
        "btn-secondary patient-prontuario-button";

    link.textContent =
        "Prontuário";

    header.insertBefore(
        link,
        codigoCard
    );
}