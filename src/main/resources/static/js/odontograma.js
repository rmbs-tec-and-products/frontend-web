document.addEventListener("DOMContentLoaded", function () {

    const teeth =
        document.querySelectorAll(".tooth");

    const toothInput =
        document.getElementById("dente");

    const selectedLabel =
        document.getElementById(
            "selected-tooth-label"
        );

    const actionTooth =
        document.getElementById(
            "action-tooth"
        );

    const excludeInput =
        document.getElementById(
            "exclude-tooth-input"
        );

    const restoreInput =
        document.getElementById(
            "restore-tooth-input"
        );

    const excludeButton =
        document.getElementById(
            "exclude-tooth-button"
        );

    const restoreButton =
        document.getElementById(
            "restore-tooth-button"
        );

    const procedureSelect =
        document.getElementById(
            "procedimentoCodigo"
        );

    const valueInput =
        document.getElementById(
            "valor"
        );

    function formatValue(value) {

        if (value === null
                || value === undefined
                || value === "") {

            return "";
        }

        const number =
            Number(
                String(value)
                    .replace(",", ".")
            );

        if (Number.isNaN(number)) {
            return value;
        }

        return number.toFixed(2);
    }

    function selectTooth(
        toothNumber
    ) {

        if (!toothNumber) {
            return;
        }

        teeth.forEach(
            function (tooth) {

                tooth.classList.remove(
                    "tooth-selected"
                );
            }
        );

        const selected =
            document.querySelector(
                '.tooth[data-tooth="'
                + toothNumber
                + '"]'
            );

        if (!selected) {
            return;
        }

        selected.classList.add(
            "tooth-selected"
        );

        const excluded =
            selected.dataset.excluded
            === "true";

        if (toothInput) {

            toothInput.value =
                toothNumber;
        }

        if (excludeInput) {

            excludeInput.value =
                toothNumber;
        }

        if (restoreInput) {

            restoreInput.value =
                toothNumber;
        }

        if (selectedLabel) {

            selectedLabel.textContent =
                "Dente selecionado: "
                + toothNumber
                + (
                    excluded
                        ? " — excluído"
                        : ""
                );
        }

        if (actionTooth) {

            actionTooth.textContent =
                toothNumber;
        }

        if (excludeButton) {

            excludeButton.disabled =
                excluded;
        }

        if (restoreButton) {

            restoreButton.disabled =
                !excluded;
        }
    }

    teeth.forEach(
        function (tooth) {

            tooth.addEventListener(
                "click",
                function () {

                    selectTooth(
                        tooth.dataset.tooth
                    );
                }
            );
        }
    );

    if (toothInput
            && toothInput.value) {

        selectTooth(
            toothInput.value
        );
    }

    if (valueInput
            && valueInput.value) {

        valueInput.value =
            formatValue(
                valueInput.value
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

                    valueInput.value =
                        formatValue(
                            value
                        );
                }
            }
        );
    }

    const excludeForm =
        document.getElementById(
            "exclude-tooth-form"
        );

    if (excludeForm) {

        excludeForm.addEventListener(
            "submit",
            function (event) {

                if (!excludeInput
                        || !excludeInput.value) {

                    event.preventDefault();

                    alert(
                        "Selecione um dente."
                    );
                }
            }
        );
    }

    const restoreForm =
        document.getElementById(
            "restore-tooth-form"
        );

    if (restoreForm) {

        restoreForm.addEventListener(
            "submit",
            function (event) {

                if (!restoreInput
                        || !restoreInput.value) {

                    event.preventDefault();

                    alert(
                        "Selecione um dente."
                    );
                }
            }
        );
    }
});