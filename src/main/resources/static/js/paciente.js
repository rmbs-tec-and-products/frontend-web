document.addEventListener("DOMContentLoaded", () => {

    console.log("paciente.js carregado");

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
});


function configurarCampoDependente(
    nomeCampo,
    idContainer
) {

    const radios = document.getElementsByName(nomeCampo);

    const container =
        document.getElementById(idContainer);

    if (!container) {
        console.warn(
            `Container não encontrado: ${idContainer}`
        );

        return;
    }

    if (!radios || radios.length === 0) {
        console.warn(
            `Radio não encontrado: ${nomeCampo}`
        );

        return;
    }

    function atualizar(limpar) {

        let valorSelecionado = null;

        for (const radio of radios) {

            if (radio.checked) {
                valorSelecionado = radio.value;
                break;
            }
        }

        const mostrar =
            valorSelecionado === "true";

        if (mostrar) {

            container.style.display = "block";

        } else {

            container.style.display = "none";

        }

        const campos =
            container.querySelectorAll(
                "input, select, textarea"
            );

        campos.forEach(campo => {

            campo.disabled = !mostrar;

            if (!mostrar && limpar) {

                if (
                    campo.type === "checkbox" ||
                    campo.type === "radio"
                ) {

                    campo.checked = false;

                } else if (
                    campo.type === "number"
                ) {

                    campo.value = "0";

                } else {

                    campo.value = "";
                }
            }
        });
    }


    for (const radio of radios) {

        radio.addEventListener(
            "change",
            () => atualizar(true)
        );
    }


    /*
     * Ao carregar a página:
     *
     * NOVO:
     * false -> campo permanece escondido.
     *
     * EDIÇÃO:
     * true -> campo aparece com o valor existente.
     */
    atualizar(false);
}