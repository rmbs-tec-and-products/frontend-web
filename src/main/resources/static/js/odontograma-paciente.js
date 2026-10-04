document.addEventListener(
    "DOMContentLoaded",
    function () {

        const selector =
            document.querySelector(
                ".dentition-selector"
            );

        const tabs =
            document.querySelectorAll(
                "[data-dentition-tab]"
            );

        const panels =
            document.querySelectorAll(
                "[data-dentition-panel]"
            );

        if (!selector
                || !tabs.length
                || !panels.length) {

            return;
        }

        const adultos =
            Number(
                selector.dataset.adultCount || 0
            );

        const pediatricos =
            Number(
                selector.dataset.childCount || 0
            );

        function abrir(
            tipo
        ) {

            tabs.forEach(
                function (tab) {

                    tab.classList.toggle(
                        "active",
                        tab.dataset.dentitionTab === tipo
                    );
                }
            );

            panels.forEach(
                function (panel) {

                    panel.classList.toggle(
                        "active",
                        panel.dataset.dentitionPanel === tipo
                    );
                }
            );

            sessionStorage.setItem(
                "sysodonto-odontograma-denticao",
                tipo
            );
        }

        tabs.forEach(
            function (tab) {

                tab.addEventListener(
                    "click",
                    function () {

                        abrir(
                            tab.dataset.dentitionTab
                        );
                    }
                );
            }
        );

        let inicial =
            "adulto";

        if (adultos === 0
                && pediatricos > 0) {

            inicial =
                "pediatrico";

        } else if (adultos > 0
                && pediatricos === 0) {

            inicial =
                "adulto";

        } else {

            const salvo =
                sessionStorage.getItem(
                    "sysodonto-odontograma-denticao"
                );

            if (salvo === "adulto"
                    || salvo === "pediatrico") {

                inicial =
                    salvo;
            }
        }

        abrir(
            inicial
        );
    }
);