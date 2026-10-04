document.addEventListener(
    "DOMContentLoaded",
    function () {

        configurarToasts();
        configurarResumoAgendamento();
    }
);

function configurarToasts() {

    document.querySelectorAll(
        ".agenda-toast"
    ).forEach(
        function (toast) {

            const close =
                toast.querySelector(
                    ".agenda-toast-close"
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

function configurarResumoAgendamento() {

    const data =
        document.getElementById(
            "agendaData"
        );

    const hora =
        document.getElementById(
            "agendaHora"
        );

    const paciente =
        document.getElementById(
            "pacienteCodigo"
        );

    const dentista =
        document.getElementById(
            "dentistaFormCodigo"
        );

    const resumo =
        document.getElementById(
            "appointment-preview-text"
        );

    if (!resumo) {
        return;
    }

    function atualizarResumo() {

        const dataValor =
            data && data.value
                ? formatarData(data.value)
                : "data não informada";

        const horaValor =
            hora && hora.value
                ? hora.value
                : "horário não informado";

        const pacienteNome =
            obterTextoSelecionado(
                paciente,
                "paciente não selecionado"
            );

        const dentistaNome =
            obterTextoSelecionado(
                dentista,
                "dentista não selecionado"
            );

        resumo.textContent =
            dataValor
            + " às "
            + horaValor
            + " • "
            + pacienteNome
            + " • "
            + dentistaNome;
    }

    [data, hora, paciente, dentista]
        .forEach(
            function (campo) {

                if (!campo) {
                    return;
                }

                campo.addEventListener(
                    "change",
                    atualizarResumo
                );
            }
        );

    atualizarResumo();
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

function formatarData(
    valor
) {

    const partes =
        valor.split("-");

    if (partes.length !== 3) {
        return valor;
    }

    return partes[2]
        + "/"
        + partes[1]
        + "/"
        + partes[0];
}