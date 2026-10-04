package br.com.migracao.frontend.controller;

import br.com.migracao.frontend.client.FornecedorClient;
import br.com.migracao.frontend.client.ProdutoClient;
import br.com.migracao.frontend.dto.produto.ProdutoRequest;
import br.com.migracao.frontend.dto.produto.ProdutoResponse;
import br.com.migracao.frontend.form.ProdutoForm;
import br.com.migracao.frontend.mapper.ProdutoFormMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoClient produtoClient;
    private final FornecedorClient fornecedorClient;
    private final ProdutoFormMapper produtoFormMapper;

    @GetMapping
    public String consultar(
            @RequestParam(required = false) String pesquisa,
            Model model
    ) {
        model.addAttribute(
                "produtos",
                produtoClient.listar(pesquisa)
        );

        model.addAttribute(
                "pesquisa",
                pesquisa
        );

        model.addAttribute(
                "modo",
                "consultar"
        );

        return "produto/index";
    }

    @GetMapping("/novo")
    public String novo(
            Model model
    ) {
        ProdutoForm form = new ProdutoForm();

        form.setQuantidade(0);
        form.setEmbalagem(0);
        form.setQtdEmbalagem(0);
        form.setQuantidadeMinima(0);

        prepararFormulario(
                model,
                form
        );

        return "produto/index";
    }

    @GetMapping("/{codigo}/editar")
    public String editar(
            @PathVariable Integer codigo,
            Model model
    ) {
        ProdutoResponse response =
                produtoClient.buscarPorCodigo(codigo);

        ProdutoForm form =
                produtoFormMapper.toForm(response);

        prepararFormulario(
                model,
                form
        );

        return "produto/index";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute ProdutoForm produtoForm,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ProdutoRequest request =
                    produtoFormMapper.toRequest(produtoForm);

            if (produtoForm.getCodigo() == null) {

                produtoClient.cadastrar(request);

                redirectAttributes.addFlashAttribute(
                        "mensagemSucesso",
                        "Produto cadastrado com sucesso."
                );

            } else {

                produtoClient.atualizar(
                        produtoForm.getCodigo(),
                        request
                );

                redirectAttributes.addFlashAttribute(
                        "mensagemSucesso",
                        "Produto atualizado com sucesso."
                );
            }

            return "redirect:/produtos";

        } catch (RestClientResponseException exception) {

            prepararFormulario(
                    model,
                    produtoForm
            );

            model.addAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );

            return "produto/index";
        }
    }

    @PostMapping("/{codigo}/excluir")
    public String excluir(
            @PathVariable Integer codigo,
            RedirectAttributes redirectAttributes
    ) {
        try {

            produtoClient.excluir(codigo);

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Produto excluído com sucesso."
            );

        } catch (RestClientResponseException exception) {

            if (exception.getStatusCode().value() == 409) {

                redirectAttributes.addFlashAttribute(
                        "mensagemErro",
                        "O produto não pode ser excluído porque está associado a um procedimento."
                );

            } else {

                redirectAttributes.addFlashAttribute(
                        "mensagemErro",
                        "Não foi possível excluir o produto."
                );
            }
        }

        return "redirect:/produtos";
    }

    private void prepararFormulario(
            Model model,
            ProdutoForm form
    ) {
        model.addAttribute(
                "produtoForm",
                form
        );

        model.addAttribute(
                "fornecedores",
                fornecedorClient.listar(null)
        );

        model.addAttribute(
                "modo",
                "novo"
        );
    }

    private String extrairMensagemErro(
            RestClientResponseException exception
    ) {
        if (exception.getStatusCode().value() == 400) {
            return "Verifique os dados informados e tente novamente.";
        }

        if (exception.getStatusCode().value() == 404) {
            return "O produto ou fornecedor informado não foi encontrado.";
        }

        if (exception.getStatusCode().value() == 409) {
            return "A operação não pôde ser concluída porque o produto está sendo utilizado.";
        }

        return "Não foi possível concluir a operação.";
    }
}