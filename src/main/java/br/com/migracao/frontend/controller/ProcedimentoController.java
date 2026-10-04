package br.com.migracao.frontend.controller;

import br.com.migracao.frontend.client.ProcedimentoClient;
import br.com.migracao.frontend.client.ProdutoClient;
import br.com.migracao.frontend.dto.procedimento.ProcedimentoRequest;
import br.com.migracao.frontend.dto.procedimento.ProcedimentoResponse;
import br.com.migracao.frontend.form.ProcedimentoForm;
import br.com.migracao.frontend.mapper.ProcedimentoFormMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/procedimentos")
@RequiredArgsConstructor
public class ProcedimentoController {

    private final ProcedimentoClient procedimentoClient;
    private final ProdutoClient produtoClient;
    private final ProcedimentoFormMapper procedimentoFormMapper;

    @GetMapping
    public String consultar(
            @RequestParam(required = false) String pesquisa,
            Model model
    ) {
        model.addAttribute(
                "procedimentos",
                procedimentoClient.listar(pesquisa)
        );

        model.addAttribute("pesquisa", pesquisa);
        model.addAttribute("modo", "consultar");

        return "procedimento/index";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        prepararFormulario(
                model,
                new ProcedimentoForm()
        );

        return "procedimento/index";
    }

    @GetMapping("/{codigo}/editar")
    public String editar(
            @PathVariable Integer codigo,
            Model model
    ) {
        ProcedimentoResponse response =
                procedimentoClient.buscarPorCodigo(codigo);

        ProcedimentoForm form =
                procedimentoFormMapper.toForm(response);

        prepararFormulario(model, form);

        return "procedimento/index";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute ProcedimentoForm procedimentoForm,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ProcedimentoRequest request =
                    procedimentoFormMapper.toRequest(procedimentoForm);

            if (procedimentoForm.getCodigo() == null) {
                procedimentoClient.cadastrar(request);

                redirectAttributes.addFlashAttribute(
                        "mensagemSucesso",
                        "Procedimento cadastrado com sucesso."
                );
            } else {
                procedimentoClient.atualizar(
                        procedimentoForm.getCodigo(),
                        request
                );

                redirectAttributes.addFlashAttribute(
                        "mensagemSucesso",
                        "Procedimento atualizado com sucesso."
                );
            }

            return "redirect:/procedimentos";

        } catch (RestClientResponseException exception) {
            prepararFormulario(
                    model,
                    procedimentoForm
            );

            model.addAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );

            return "procedimento/index";
        }
    }

    @PostMapping("/{codigo}/excluir")
    public String excluir(
            @PathVariable Integer codigo,
            RedirectAttributes redirectAttributes
    ) {
        try {
            procedimentoClient.excluir(codigo);

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Procedimento excluído com sucesso."
            );

        } catch (RestClientResponseException exception) {
            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    "Não foi possível excluir o procedimento."
            );
        }

        return "redirect:/procedimentos";
    }

    private void prepararFormulario(
            Model model,
            ProcedimentoForm form
    ) {
        model.addAttribute(
                "procedimentoForm",
                form
        );

        model.addAttribute(
                "produtosDisponiveis",
                produtoClient.listar()
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
            return "Um dos produtos selecionados não foi encontrado.";
        }

        if (exception.getStatusCode().value() == 409) {
            return "A operação não pôde ser concluída por conflito de dados.";
        }

        return "Não foi possível salvar o procedimento.";
    }
}