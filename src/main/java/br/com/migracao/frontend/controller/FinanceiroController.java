package br.com.migracao.frontend.controller;

import br.com.migracao.frontend.client.CaixaClient;
import br.com.migracao.frontend.dto.caixa.CaixaMovimentacaoRequest;
import br.com.migracao.frontend.dto.caixa.CaixaMovimentacaoResponse;
import br.com.migracao.frontend.dto.caixa.CaixaResponse;
import br.com.migracao.frontend.form.CaixaMovimentacaoForm;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

@Controller
@RequestMapping("/financeiro")
@RequiredArgsConstructor
public class FinanceiroController {

    private final CaixaClient caixaClient;
    private final ObjectMapper objectMapper;

    @GetMapping
    public String consultar(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fim,

            Model model
    ) {
        LocalDate hoje =
                LocalDate.now();

        LocalDate dataInicio =
                inicio != null
                        ? inicio
                        : hoje.withDayOfMonth(1);

        LocalDate dataFim =
                fim != null
                        ? fim
                        : hoje;

        try {
            model.addAttribute(
                    "caixas",
                    caixaClient.listar(
                            dataInicio,
                            dataFim
                    )
            );

        } catch (RestClientResponseException exception) {

            model.addAttribute(
                    "caixas",
                    java.util.List.of()
            );

            model.addAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );
        }

        model.addAttribute(
                "inicio",
                dataInicio
        );

        model.addAttribute(
                "fim",
                dataFim
        );

        model.addAttribute(
                "modo",
                "consultar"
        );

        return "financeiro/index";
    }

    @GetMapping("/caixa-aberto")
    public String caixaAberto(
            @RequestParam(required = false)
            Integer editar,
            Model model
    ) {
        CaixaMovimentacaoForm form =
                new CaixaMovimentacaoForm();

        try {
            CaixaResponse caixa =
                    caixaClient.buscarAberto();

            if (editar != null) {

                CaixaMovimentacaoResponse movimentacao =
                        caixa.movimentacoes()
                                .stream()
                                .filter(item ->
                                        editar.equals(
                                                item.codigo()
                                        )
                                )
                                .findFirst()
                                .orElse(null);

                if (movimentacao != null) {
                    form.setCodigo(
                            movimentacao.codigo()
                    );

                    form.setDescricao(
                            movimentacao.descricao()
                    );

                    form.setTipo(
                            movimentacao.tipo()
                    );

                    form.setValor(
                            movimentacao.valor()
                    );

                    form.setObservacao(
                            movimentacao.observacao()
                    );
                }
            }

            model.addAttribute(
                    "caixaAberto",
                    caixa
            );

        } catch (RestClientResponseException exception) {

            if (exception.getStatusCode().value() == 404) {
                model.addAttribute(
                        "caixaAberto",
                        null
                );

            } else {
                model.addAttribute(
                        "caixaAberto",
                        null
                );

                model.addAttribute(
                        "mensagemErro",
                        extrairMensagemErro(exception)
                );
            }
        }

        model.addAttribute(
                "movimentacaoForm",
                form
        );

        model.addAttribute(
                "modo",
                "caixaAberto"
        );

        return "financeiro/index";
    }

    @GetMapping("/caixas/{codigo}")
    public String detalhe(
            @PathVariable Integer codigo,
            Model model
    ) {
        try {
            model.addAttribute(
                    "caixaDetalhe",
                    caixaClient.buscarPorCodigo(codigo)
            );

        } catch (RestClientResponseException exception) {

            model.addAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );
        }

        model.addAttribute(
                "modo",
                "detalhe"
        );

        return "financeiro/index";
    }

    @PostMapping("/abrir")
    public String abrir(
            RedirectAttributes redirectAttributes
    ) {
        try {
            caixaClient.abrir();

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Caixa aberto com sucesso."
            );

        } catch (RestClientResponseException exception) {

            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );
        }

        return "redirect:/financeiro/caixa-aberto";
    }

    @PostMapping("/caixas/{codigo}/fechar")
    public String fechar(
            @PathVariable Integer codigo,
            RedirectAttributes redirectAttributes
    ) {
        try {
            caixaClient.fechar(codigo);

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Caixa fechado com sucesso."
            );

            return "redirect:/financeiro";

        } catch (RestClientResponseException exception) {

            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );

            return "redirect:/financeiro/caixa-aberto";
        }
    }

    @PostMapping("/caixas/{codigo}/movimentacoes/salvar")
    public String salvarMovimentacao(
            @PathVariable Integer codigo,
            @ModelAttribute CaixaMovimentacaoForm movimentacaoForm,
            RedirectAttributes redirectAttributes
    ) {
        try {
            CaixaMovimentacaoRequest request =
                    new CaixaMovimentacaoRequest(
                            movimentacaoForm.getDescricao(),
                            movimentacaoForm.getTipo(),
                            movimentacaoForm.getValor(),
                            movimentacaoForm.getObservacao()
                    );

            if (movimentacaoForm.getCodigo() == null) {

                caixaClient.adicionarMovimentacao(
                        codigo,
                        request
                );

                redirectAttributes.addFlashAttribute(
                        "mensagemSucesso",
                        "Movimentação adicionada com sucesso."
                );

            } else {

                caixaClient.atualizarMovimentacao(
                        codigo,
                        movimentacaoForm.getCodigo(),
                        request
                );

                redirectAttributes.addFlashAttribute(
                        "mensagemSucesso",
                        "Movimentação atualizada com sucesso."
                );
            }

        } catch (RestClientResponseException exception) {

            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );
        }

        return "redirect:/financeiro/caixa-aberto";
    }

    @PostMapping("/caixas/{codigo}/movimentacoes/{movimentacaoCodigo}/excluir")
    public String excluirMovimentacao(
            @PathVariable Integer codigo,
            @PathVariable Integer movimentacaoCodigo,
            RedirectAttributes redirectAttributes
    ) {
        try {
            caixaClient.excluirMovimentacao(
                    codigo,
                    movimentacaoCodigo
            );

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Movimentação excluída com sucesso."
            );

        } catch (RestClientResponseException exception) {

            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );
        }

        return "redirect:/financeiro/caixa-aberto";
    }

    private String extrairMensagemErro(
            RestClientResponseException exception
    ) {
        try {
            JsonNode json =
                    objectMapper.readTree(
                            exception.getResponseBodyAsString()
                    );

            JsonNode mensagem =
                    json.get("message");

            if (mensagem != null
                    && !mensagem.asText().isBlank()) {

                return mensagem.asText();
            }

        } catch (Exception ignored) {
        }

        if (exception.getStatusCode().value() == 400) {
            return "Verifique os dados informados.";
        }

        if (exception.getStatusCode().value() == 404) {
            return "Registro não encontrado.";
        }

        return "Não foi possível concluir a operação.";
    }
}