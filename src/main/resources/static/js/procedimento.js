document.addEventListener("DOMContentLoaded", function () {

    const produtoSelect =
        document.getElementById("produtoSelecionado");

    const quantidadeInput =
        document.getElementById("quantidadeProduto");

    const botaoAdicionar =
        document.getElementById("btnAdicionarProduto");

    const tbody =
        document.getElementById("produtosSelecionados");

    const tabela =
        document.getElementById("tabelaProdutos");

    const emptyState =
        document.getElementById("productEmptyState");

    if (!tbody) {
        return;
    }

    function atualizarEstadoTabela() {
        const possuiProdutos =
            tbody.querySelectorAll(".produto-row").length > 0;

        tabela.style.display =
            possuiProdutos ? "table" : "none";

        emptyState.style.display =
            possuiProdutos ? "none" : "block";
    }

    function reindexarProdutos() {
        const linhas =
            tbody.querySelectorAll(".produto-row");

        linhas.forEach(function (linha, index) {

            const codigo =
                linha.querySelector(
                    'input[name$=".produtoCodigo"]'
                );

            const nome =
                linha.querySelector(
                    'input[name$=".produtoNome"]'
                );

            const quantidade =
                linha.querySelector(
                    'input[name$=".quantidade"]'
                );

            codigo.name =
                `produtos[${index}].produtoCodigo`;

            nome.name =
                `produtos[${index}].produtoNome`;

            quantidade.name =
                `produtos[${index}].quantidade`;
        });
    }

    function produtoJaAdicionado(codigo) {
        return tbody.querySelector(
            `.produto-row[data-produto-codigo="${codigo}"]`
        ) !== null;
    }

    function adicionarProduto() {

        const codigo =
            produtoSelect.value;

        const option =
            produtoSelect.options[
                produtoSelect.selectedIndex
            ];

        const nome =
            option ? option.text.trim() : "";

        const quantidade =
            parseInt(quantidadeInput.value, 10);

        if (!codigo) {
            alert("Selecione um produto.");
            return;
        }

        if (!quantidade || quantidade < 1) {
            alert("Informe uma quantidade válida.");
            return;
        }

        if (produtoJaAdicionado(codigo)) {
            alert(
                "Este produto já foi adicionado ao procedimento."
            );
            return;
        }

        const linha =
            document.createElement("tr");

        linha.classList.add("produto-row");

        linha.dataset.produtoCodigo =
            codigo;

        linha.innerHTML = `
            <td>
                <span>${escapeHtml(nome)}</span>

                <input
                    type="hidden"
                    value="${codigo}"
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

        tbody.appendChild(linha);

        prepararLinha(linha);

        reindexarNovosCampos();
        atualizarEstadoTabela();

        produtoSelect.value = "";
        quantidadeInput.value = "1";
    }

    function reindexarNovosCampos() {
        const linhas =
            tbody.querySelectorAll(".produto-row");

        linhas.forEach(function (linha, index) {

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

            codigo.name =
                `produtos[${index}].produtoCodigo`;

            nome.name =
                `produtos[${index}].produtoNome`;

            quantidade.name =
                `produtos[${index}].quantidade`;
        });
    }

    function prepararLinha(linha) {
        const botaoRemover =
            linha.querySelector(
                ".btn-remove-product"
            );

        if (!botaoRemover) {
            return;
        }

        botaoRemover.addEventListener(
            "click",
            function () {

                linha.remove();

                reindexarProdutos();
                atualizarEstadoTabela();
            }
        );
    }

    function escapeHtml(value) {
        const div =
            document.createElement("div");

        div.textContent = value;

        return div.innerHTML;
    }

    function escapeAttribute(value) {
        return value
            .replaceAll("&", "&amp;")
            .replaceAll('"', "&quot;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;");
    }

    tbody
        .querySelectorAll(".produto-row")
        .forEach(prepararLinha);

    if (botaoAdicionar) {
        botaoAdicionar.addEventListener(
            "click",
            adicionarProduto
        );
    }

    reindexarProdutos();
    atualizarEstadoTabela();
});