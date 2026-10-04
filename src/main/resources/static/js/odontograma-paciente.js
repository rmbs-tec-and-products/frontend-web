document.addEventListener(
    "DOMContentLoaded",
    function () {

        const tabs =
            document.querySelectorAll(
                "[data-dentition-tab]"
            );

        const panels =
            document.querySelectorAll(
                "[data-dentition-panel]"
            );

        if (!tabs.length
                || !panels.length) {

            return;
        }

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

        const salva =
            sessionStorage.getItem(
                "sysodonto-odontograma-denticao"
            );

        if (salva === "adulto"
                || salva === "pediatrico") {

            abrir(
                salva
            );
        }
    }
);