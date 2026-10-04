document.addEventListener("DOMContentLoaded", function () {

    const teeth = document.querySelectorAll(".tooth");
    const procedureRows = document.querySelectorAll(".procedure-row");

    const toothInput = document.getElementById("dente");
    const actionTooth = document.getElementById("action-tooth");
    const modalToothNumber = document.getElementById("modal-tooth-number");

    const selectedTitle = document.getElementById("selected-tooth-title");
    const selectedDescription = document.getElementById("selected-tooth-description");
    const selectedLabel = document.getElementById("selected-tooth-label");

    const excludeInput = document.getElementById("exclude-tooth-input");
    const restoreInput = document.getElementById("restore-tooth-input");

    const excludeButton = document.getElementById("exclude-tooth-button");
    const restoreButton = document.getElementById("restore-tooth-button");
    const newProcedureButton = document.getElementById("new-procedure-button");

    const procedureSelect = document.getElementById("procedimentoCodigo");
    const valueInput = document.getElementById("valor");

    const procedureModal = document.getElementById("procedure-modal");
    const modalBackdrop = document.getElementById("procedure-modal-backdrop");
    const closeModalButton = document.getElementById("close-procedure-modal");
    const cancelModalButton = document.getElementById("cancel-procedure-modal");

    let selectedTooth = null;

    const statusPriority = {
        OBSERVACAO: 1,
        A_REALIZAR: 2,
        INICIADO: 3,
        CONCLUIDO: 4
    };

    function formatValue(value) {

        if (value === null
                || value === undefined
                || value === "") {
            return "";
        }

        const number = Number(
            String(value).replace(",", ".")
        );

        if (Number.isNaN(number)) {
            return value;
        }

        return number.toFixed(2);
    }

    function getToothProcedures(toothNumber) {

        return Array.from(procedureRows)
            .filter(function (row) {
                return row.dataset.tooth === String(toothNumber);
            });
    }

    function applyToothStatuses() {

        teeth.forEach(function (tooth) {

            if (tooth.dataset.excluded === "true") {
                return;
            }

            const toothNumber = tooth.dataset.tooth;
            const rows = getToothProcedures(toothNumber);

            if (rows.length === 0) {
                return;
            }

            let highestStatus = null;
            let highestPriority = 0;

            rows.forEach(function (row) {

                const status = row.dataset.status;
                const priority = statusPriority[status] || 0;

                if (priority > highestPriority) {
                    highestPriority = priority;
                    highestStatus = status;
                }
            });

            if (highestStatus) {
                tooth.classList.add(
                    "tooth-state-" + highestStatus.toLowerCase()
                );
            }
        });
    }

    function updateSelectedToothContent(toothNumber, excluded) {

        const procedureCount =
            getToothProcedures(toothNumber).length;

        if (actionTooth) {
            actionTooth.textContent = toothNumber;
        }

        if (modalToothNumber) {
            modalToothNumber.textContent = toothNumber;
        }

        if (selectedTitle) {

            selectedTitle.textContent =
                excluded
                    ? "Dente " + toothNumber + " está excluído"
                    : "Dente " + toothNumber + " selecionado";
        }

        if (selectedDescription) {

            if (excluded) {
                selectedDescription.textContent =
                    "Restaure este dente para adicionar novos procedimentos.";
            } else if (procedureCount === 0) {
                selectedDescription.textContent =
                    "Nenhum procedimento registrado neste dente.";
            } else if (procedureCount === 1) {
                selectedDescription.textContent =
                    "1 procedimento registrado neste dente.";
            } else {
                selectedDescription.textContent =
                    procedureCount + " procedimentos registrados neste dente.";
            }
        }

        if (selectedLabel) {

            selectedLabel.textContent =
                excluded
                    ? "Dente " + toothNumber + " — excluído"
                    : "Dente " + toothNumber;
        }
    }

    function selectTooth(toothNumber) {

        if (!toothNumber) {
            return;
        }

        const selected =
            document.querySelector(
                '.tooth[data-tooth="' + toothNumber + '"]'
            );

        if (!selected) {
            return;
        }

        teeth.forEach(function (tooth) {
            tooth.classList.remove("tooth-selected");
        });

        selected.classList.add("tooth-selected");

        selectedTooth = toothNumber;

        const excluded =
            selected.dataset.excluded === "true";

        if (toothInput) {
            toothInput.value = toothNumber;
        }

        if (excludeInput) {
            excludeInput.value = toothNumber;
        }

        if (restoreInput) {
            restoreInput.value = toothNumber;
        }

        if (excludeButton) {
            excludeButton.disabled = excluded;
        }

        if (restoreButton) {
            restoreButton.disabled = !excluded;
        }

        if (newProcedureButton) {
            newProcedureButton.disabled = excluded;
        }

        updateSelectedToothContent(
            toothNumber,
            excluded
        );
    }

    function openProcedureModal() {

        if (!procedureModal) {
            return;
        }

        if (!selectedTooth && toothInput && toothInput.value) {
            selectedTooth = toothInput.value;
        }

        if (!selectedTooth) {
            return;
        }

        procedureModal.classList.add("is-open");
        document.body.classList.add("modal-lock");
    }

    function closeProcedureModal() {

        if (!procedureModal) {
            return;
        }

        const editing =
            procedureModal.dataset.editing === "true";

        if (editing) {
            window.location.href = window.location.pathname;
            return;
        }

        procedureModal.classList.remove("is-open");
        document.body.classList.remove("modal-lock");
    }

    function setupToasts() {

        document.querySelectorAll(".odontograma-toast")
            .forEach(function (toast) {

                const close =
                    toast.querySelector(
                        ".odontograma-toast-close"
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
            });
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

    if (newProcedureButton) {

        newProcedureButton.addEventListener(
            "click",
            function () {

                if (!selectedTooth) {
                    return;
                }

                openProcedureModal();
            }
        );
    }

    if (closeModalButton) {
        closeModalButton.addEventListener(
            "click",
            closeProcedureModal
        );
    }

    if (cancelModalButton) {
        cancelModalButton.addEventListener(
            "click",
            closeProcedureModal
        );
    }

    if (modalBackdrop) {
        modalBackdrop.addEventListener(
            "click",
            closeProcedureModal
        );
    }

    document.addEventListener(
        "keydown",
        function (event) {

            if (event.key === "Escape"
                    && procedureModal
                    && procedureModal.classList.contains("is-open")) {

                closeProcedureModal();
            }
        }
    );

    if (valueInput && valueInput.value) {

        valueInput.value =
            formatValue(
                valueInput.value
            );
    }

    if (procedureSelect && valueInput) {

        procedureSelect.addEventListener(
            "change",
            function () {

                const option =
                    procedureSelect.options[
                        procedureSelect.selectedIndex
                    ];

                if (!option) {
                    return;
                }

                const value =
                    option.dataset.value;

                if (value !== undefined
                        && value !== null
                        && value !== "") {

                    valueInput.value =
                        formatValue(value);
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
                }
            }
        );
    }

    applyToothStatuses();
    setupToasts();

    if (toothInput && toothInput.value) {

        selectTooth(
            toothInput.value
        );
    }

    if (procedureModal
            && procedureModal.dataset.editing === "true") {

        openProcedureModal();
    }
});