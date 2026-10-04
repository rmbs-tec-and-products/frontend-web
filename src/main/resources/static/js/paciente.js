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
        configurarMascarasPaciente();
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

function configurarMascarasPaciente() {

    const telefone =
        document.getElementById(
            "telefone"
        );

    const celular =
        document.getElementById(
            "celular"
        );

    const documento =
        document.getElementById(
            "cpf"
        );

    configurarTelefoneFixo(
        telefone
    );

    configurarCelular(
        celular
    );

    configurarDocumento(
        documento
    );

    configurarValidacaoPaciente(
        telefone,
        celular,
        documento
    );
}

function configurarTelefoneFixo(
    campo
) {

    if (!campo) {
        return;
    }

    campo.required =
        false;

    campo.removeAttribute(
        "required"
    );

    campo.setAttribute(
        "inputmode",
        "numeric"
    );

    campo.setAttribute(
        "maxlength",
        "14"
    );

    campo.placeholder =
        "(18) 3222-1234";

    const label =
        document.querySelector(
            'label[for="telefone"]'
        );

    if (label) {

        const obrigatorio =
            label.querySelector(
                ".required"
            );

        if (obrigatorio) {

            obrigatorio.remove();
        }
    }

    campo.value =
        mascaraTelefoneFixo(
            campo.value
        );

    campo.addEventListener(
        "input",
        function () {

            campo.setCustomValidity(
                ""
            );

            campo.value =
                mascaraTelefoneFixo(
                    campo.value
                );
        }
    );
}

function configurarCelular(
    campo
) {

    if (!campo) {
        return;
    }

    campo.required =
        true;

    campo.setAttribute(
        "required",
        ""
    );

    campo.setAttribute(
        "inputmode",
        "numeric"
    );

    campo.setAttribute(
        "maxlength",
        "15"
    );

    campo.placeholder =
        "(18) 99999-1234";

    campo.value =
        mascaraCelular(
            campo.value
        );

    campo.addEventListener(
        "input",
        function () {

            campo.setCustomValidity(
                ""
            );

            campo.value =
                mascaraCelular(
                    campo.value
                );
        }
    );
}

function configurarDocumento(
    campo
) {

    if (!campo) {
        return;
    }

    campo.setAttribute(
        "inputmode",
        "numeric"
    );

    campo.setAttribute(
        "maxlength",
        "18"
    );

    campo.placeholder =
        "CPF ou CNPJ";

    const label =
        document.querySelector(
            'label[for="cpf"]'
        );

    if (label) {

        label.textContent =
            "CPF / CNPJ";
    }

    campo.value =
        mascaraDocumento(
            campo.value
        );

    campo.addEventListener(
        "input",
        function () {

            campo.setCustomValidity(
                ""
            );

            campo.value =
                mascaraDocumento(
                    campo.value
                );
        }
    );
}

function configurarValidacaoPaciente(
    telefone,
    celular,
    documento
) {

    const form =
        document.querySelector(
            ".patient-form"
        );

    if (!form) {
        return;
    }

    form.addEventListener(
        "submit",
        function (event) {

            let valido =
                true;

            if (telefone) {

                const digitosTelefone =
                    somenteDigitos(
                        telefone.value
                    );

                telefone.setCustomValidity(
                    ""
                );

                if (digitosTelefone.length > 0
                        && digitosTelefone.length !== 10) {

                    telefone.setCustomValidity(
                        "Informe o telefone fixo com DDD e 10 dígitos."
                    );

                    valido =
                        false;
                }
            }

            if (celular) {

                const digitosCelular =
                    somenteDigitos(
                        celular.value
                    );

                celular.setCustomValidity(
                    ""
                );

                if (digitosCelular.length !== 11) {

                    celular.setCustomValidity(
                        "Informe o celular com DDD e 11 dígitos."
                    );

                    valido =
                        false;
                }
            }

            if (documento) {

                const digitosDocumento =
                    somenteDigitos(
                        documento.value
                    );

                documento.setCustomValidity(
                    ""
                );

                if (digitosDocumento.length > 0
                        && digitosDocumento.length !== 11
                        && digitosDocumento.length !== 14) {

                    documento.setCustomValidity(
                        "Informe 11 dígitos para CPF ou 14 dígitos para CNPJ."
                    );

                    valido =
                        false;
                }
            }

            if (!valido) {

                event.preventDefault();

                form.reportValidity();
            }
        }
    );
}

function mascaraTelefoneFixo(
    valor
) {

    let numero =
        somenteDigitos(
            valor
        ).slice(
            0,
            10
        );

    if (numero.length === 0) {

        return "";
    }

    if (numero.length <= 2) {

        return "("
            + numero;
    }

    if (numero.length <= 6) {

        return "("
            + numero.slice(
                0,
                2
            )
            + ") "
            + numero.slice(
                2
            );
    }

    return "("
        + numero.slice(
            0,
            2
        )
        + ") "
        + numero.slice(
            2,
            6
        )
        + "-"
        + numero.slice(
            6
        );
}

function mascaraCelular(
    valor
) {

    let numero =
        somenteDigitos(
            valor
        ).slice(
            0,
            11
        );

    if (numero.length === 0) {

        return "";
    }

    if (numero.length <= 2) {

        return "("
            + numero;
    }

    if (numero.length <= 7) {

        return "("
            + numero.slice(
                0,
                2
            )
            + ") "
            + numero.slice(
                2
            );
    }

    return "("
        + numero.slice(
            0,
            2
        )
        + ") "
        + numero.slice(
            2,
            7
        )
        + "-"
        + numero.slice(
            7
        );
}

function mascaraDocumento(
    valor
) {

    const numero =
        somenteDigitos(
            valor
        ).slice(
            0,
            14
        );

    if (numero.length <= 11) {

        return mascaraCpf(
            numero
        );
    }

    return mascaraCnpj(
        numero
    );
}

function mascaraCpf(
    numero
) {

    if (numero.length <= 3) {

        return numero;
    }

    if (numero.length <= 6) {

        return numero.slice(
            0,
            3
        )
        + "."
        + numero.slice(
            3
        );
    }

    if (numero.length <= 9) {

        return numero.slice(
            0,
            3
        )
        + "."
        + numero.slice(
            3,
            6
        )
        + "."
        + numero.slice(
            6
        );
    }

    return numero.slice(
        0,
        3
    )
    + "."
    + numero.slice(
        3,
        6
    )
    + "."
    + numero.slice(
        6,
        9
    )
    + "-"
    + numero.slice(
        9,
        11
    );
}

function mascaraCnpj(
    numero
) {

    if (numero.length <= 2) {

        return numero;
    }

    if (numero.length <= 5) {

        return numero.slice(
            0,
            2
        )
        + "."
        + numero.slice(
            2
        );
    }

    if (numero.length <= 8) {

        return numero.slice(
            0,
            2
        )
        + "."
        + numero.slice(
            2,
            5
        )
        + "."
        + numero.slice(
            5
        );
    }

    if (numero.length <= 12) {

        return numero.slice(
            0,
            2
        )
        + "."
        + numero.slice(
            2,
            5
        )
        + "."
        + numero.slice(
            5,
            8
        )
        + "/"
        + numero.slice(
            8
        );
    }

    return numero.slice(
        0,
        2
    )
    + "."
    + numero.slice(
        2,
        5
    )
    + "."
    + numero.slice(
        5,
        8
    )
    + "/"
    + numero.slice(
        8,
        12
    )
    + "-"
    + numero.slice(
        12,
        14
    );
}

function somenteDigitos(
    valor
) {

    if (!valor) {

        return "";
    }

    return String(
        valor
    ).replace(
        /\D/g,
        ""
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