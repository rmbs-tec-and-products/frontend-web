document.addEventListener(
    "DOMContentLoaded",
    function () {

        const produtoSelect =
            document.getElementById(
                "produtoSelecionado"
            );

        const quantidadeInput =
            document.getElementById(
                "quantidadeProduto"
            );

        const botaoAdicionar =
            document.getElementById(
                "btnAdicionarProduto"
            );

        const tbody =
            document.getElementById(
                "produtosSelecionados"
            );

        const tabela =
            document.getElementById(
                "tabelaProdutos"
            );

        const emptyState =
            document.getElementById(
                "productEmptyState"
            );

        const countSummary =
            document.getElementById(
                "product-count-summary"
            );

        const nomeInput =
            document.getElementById(
                "nome"
            );

        const valorInput =
            document.getElementById(
                "valor"
            );

        const preview =
            document.getElementById(
                "procedure-preview-text"
            );

        configurarToasts();

        if (!tbody) {
            return;
        }

        function atualizarEstadoTabela() {

            const quantidade =
                tbody.querySelectorAll(
                    ".produto-row"
                ).length;

            const possuiProdutos =
                quantidade > 0;

            if (tabela) {
                tabela.style.display =
                    possuiProdutos
                        ? "table"
                        : "none";
            }

            if (emptyState) {
                emptyState.style.display =
                    possuiProdutos
                        ? "none"
                        : "flex";
            }

            if (countSummary) {
                countSummary.textContent =
                    quantidade;
            }

            atualizarPreview();
        }

        function reindexarProdutos() {

            const linhas =
                tbody.querySelectorAll(
                    ".produto-row"
                );

            linhas.forEach(
                function (linha, index) {

                    const codigo =
                        linha.querySelector(
                            '[data-field="produtoCodigo"], input[name$=".produtoCodigo"]'
                        );

                    const nome =
                        linha.querySelector(
                            '[data-field="produtoNome"], input[name$=".produtoNome"]'
                        );

                    const quantidade =
                        linha.querySelector(
                            '[data-field="quantidade"], input[name$=".quantidade"]'
                        );

                    if (codigo) {
                        codigo.name =
                            `produtos[${index}].produtoCodigo`;
                    }

                    if (nome) {
                        nome.name =
                            `produtos[${index}].produtoNome`;
                    }

                    if (quantidade) {
                        quantidade.name =
                            `produtos[${index}].quantidade`;
                    }
                }
            );
        }

        function produtoJaAdicionado(
            codigo
        ) {

            return tbody.querySelector(
                `.produto-row[data-produto-codigo="${codigo}"]`
            ) !== null;
        }

        function adicionarProduto() {

            if (!produtoSelect
                    || !quantidadeInput) {
                return;
            }

            const codigo =
                produtoSelect.value;

            const option =
                produtoSelect.options[
                    produtoSelect.selectedIndex
                ];

            const nome =
                option
                    ? option.text.trim()
                    : "";

            const quantidade =
                parseInt(
                    quantidadeInput.value,
                    10
                );

            if (!codigo) {

                alert(
                    "Selecione um produto."
                );

                return;
            }

            if (!quantidade
                    || quantidade < 1) {

                alert(
                    "Informe uma quantidade válida."
                );

                return;
            }

            if (produtoJaAdicionado(codigo)) {

                alert(
                    "Este produto já foi adicionado ao procedimento."
                );

                return;
            }

            const linha =
                document.createElement(
                    "tr"
                );

            linha.classList.add(
                "produto-row"
            );

            linha.dataset.produtoCodigo =
                codigo;

            linha.innerHTML = `
                <td>

                    <div class="selected-product-name">

                        <span class="product-icon">
                            •
                        </span>

                        <strong>
                            ${escapeHtml(nome)}
                        </strong>

                    </div>

                    <input
                        type="hidden"
                        value="${escapeAttribute(codigo)}"
                        data-field="produtoCodigo">

                    <input
                        type="hidden"
                        value="${escapeAttribute(nome)}"
                        data-field="produtoNome">

                </td>

                <td>

                    <input
                        type="number"
                        min="1"
                        value="${quantidade}"
                        class="table-quantity"
                        data-field="quantidade">

                </td>

                <td>

                    <button
                        type="button"
                        class="btn-remove-product">
                        Remover
                    </button>

                </td>
            `;

            tbody.appendChild(
                linha
            );

            prepararLinha(
                linha
            );

            reindexarProdutos();
            atualizarEstadoTabela();

            produtoSelect.value =
                "";

            quantidadeInput.value =
                "1";
        }

        function prepararLinha(
            linha
        ) {

            const botaoRemover =
                linha.querySelector(
                    ".btn-remove-product"
                );

            const quantidade =
                linha.querySelector(
                    '.table-quantity'
                );

            if (botaoRemover) {

                botaoRemover.addEventListener(
                    "click",
                    function () {

                        linha.remove();

                        reindexarProdutos();
                        atualizarEstadoTabela();
                    }
                );
            }

            if (quantidade) {

                quantidade.addEventListener(
                    "change",
                    function () {

                        if (!quantidade.value
                                || Number(quantidade.value) < 1) {

                            quantidade.value =
                                "1";
                        }

                        atualizarPreview();
                    }
                );
            }
        }

        function atualizarPreview() {

            if (!preview) {
                return;
            }

            const nome =
                nomeInput
                && nomeInput.value.trim()
                    ? nomeInput.value.trim()
                    : "Procedimento sem nome";

            const valor =
                formatarMoeda(
                    valorInput
                        ? valorInput.value
                        : null
                );

            const quantidadeProdutos =
                tbody.querySelectorAll(
                    ".produto-row"
                ).length;

            const produtosTexto =
                quantidadeProdutos === 0
                    ? "sem produtos vinculados"
                    : quantidadeProdutos === 1
                        ? "1 produto vinculado"
                        : quantidadeProdutos
                            + " produtos vinculados";

            preview.textContent =
                nome
                + " • "
                + valor
                + " • "
                + produtosTexto;
        }

        function formatarMoeda(
            value
        ) {

            if (value === null
                    || value === undefined
                    || value === "") {

                return "R$ 0,00";
            }

            const numero =
                Number(
                    String(value)
                        .replace(",", ".")
                );

            if (Number.isNaN(numero)) {
                return "R$ 0,00";
            }

            return numero.toLocaleString(
                "pt-BR",
                {
                    style: "currency",
                    currency: "BRL"
                }
            );
        }

        function escapeHtml(
            value
        ) {

            const div =
                document.createElement(
                    "div"
                );

            div.textContent =
                value;

            return div.innerHTML;
        }

        function escapeAttribute(
            value
        ) {

            return String(value)
                .replaceAll(
                    "&",
                    "&amp;"
                )
                .replaceAll(
                    '"',
                    "&quot;"
                )
                .replaceAll(
                    "<",
                    "&lt;"
                )
                .replaceAll(
                    ">",
                    "&gt;"
                );
        }

        tbody
            .querySelectorAll(
                ".produto-row"
            )
            .forEach(
                prepararLinha
            );

        if (botaoAdicionar) {

            botaoAdicionar.addEventListener(
                "click",
                adicionarProduto
            );
        }

        if (nomeInput) {

            nomeInput.addEventListener(
                "input",
                atualizarPreview
            );
        }

        if (valorInput) {

            valorInput.addEventListener(
                "input",
                atualizarPreview
            );
        }

        reindexarProdutos();
        atualizarEstadoTabela();
        atualizarPreview();
    }
);

function configurarToasts() {

    document.querySelectorAll(
        ".procedimento-toast"
    ).forEach(
        function (toast) {

            const close =
                toast.querySelector(
                    ".procedimento-toast-close"
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