document.addEventListener("DOMContentLoaded", function () {

    const teeth = document.querySelectorAll(".tooth");
    const toothInput = document.getElementById("dente");
    const selectedLabel = document.getElementById("selected-tooth-label");
    const actionTooth = document.getElementById("action-tooth");

    const excludeForm =
        document.getElementById("exclude-tooth-form");

    const restoreForm =
        document.getElementById("restore-tooth-form");

    const procedureSelect =
        document.getElementById("procedimentoCodigo");

    const valueInput =
        document.getElementById("valor");

    function selectTooth(toothNumber) {

        if (!toothNumber) {
            return;
        }

        teeth.forEach(function (tooth) {
            tooth.classList.remove("tooth-selected");
        });

        const selected =
            document.querySelector(
                '.tooth[data-tooth="' + toothNumber + '"]'
            );

        if (selected) {
            selected.classList.add("tooth-selected");
        }

        if (toothInput) {
            toothInput.value = toothNumber;
        }

        if (selectedLabel) {
            selectedLabel.textContent =
                "Dente selecionado: " + toothNumber;
        }

        if (actionTooth) {
            actionTooth.textContent =
                toothNumber;
        }

        updateToothActions(toothNumber);
    }

    function updateToothActions(toothNumber) {

        if (!excludeForm || !restoreForm) {
            return;
        }

        const odontograma =
            excludeForm.dataset.odontograma;

        excludeForm.action =
            "/odontogramas/"
            + odontograma
            + "/dentes/"
            + toothNumber
            + "/excluir";

        restoreForm.action =
            "/odontogramas/"
            + odontograma
            + "/dentes/"
            + toothNumber
            + "/restaurar";
    }

    teeth.forEach(function (tooth) {

        tooth.addEventListener(
            "click",
            function () {

                selectTooth(
                    tooth.dataset.tooth
                );
            }
        );
    });

    if (toothInput
        && toothInput.value) {

        selectTooth(
            toothInput.value
        );
    }

    if (procedureSelect
        && valueInput) {

        procedureSelect.addEventListener(
            "change",
            function () {

                const selectedOption =
                    procedureSelect.options[
                        procedureSelect.selectedIndex
                    ];

                if (!selectedOption) {
                    return;
                }

                const value =
                    selectedOption.dataset.value;

                if (value !== undefined
                    && value !== null
                    && value !== "") {

                    valueInput.value = value;
                }
            }
        );
    }

    if (excludeForm) {

        excludeForm.addEventListener(
            "submit",
            function (event) {

                if (!toothInput
                    || !toothInput.value) {

                    event.preventDefault();

                    alert(
                        "Selecione um dente."
                    );
                }
            }
        );
    }

    if (restoreForm) {

        restoreForm.addEventListener(
            "submit",
            function (event) {

                if (!toothInput
                    || !toothInput.value) {

                    event.preventDefault();

                    alert(
                        "Selecione um dente."
                    );
                }
            }
        );
    }
});