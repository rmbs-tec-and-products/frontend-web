package br.com.migracao.frontend.controller;

import br.com.migracao.frontend.client.CaixaClient;
import br.com.migracao.frontend.client.ContaClient;
import br.com.migracao.frontend.dto.caixa.CaixaMovimentacaoRequest;
import br.com.migracao.frontend.dto.caixa.CaixaMovimentacaoResponse;
import br.com.migracao.frontend.dto.caixa.CaixaResponse;
import br.com.migracao.frontend.dto.conta.ContaResponse;
import br.com.migracao.frontend.form.CaixaMovimentacaoForm;
import br.com.migracao.frontend.form.ContaForm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/financeiro")
@RequiredArgsConstructor
public class FinanceiroController {

    private static final String FINALIZACAO_PROCEDIMENTO =
            "FINALIZAÇÃO DE PROCEDIMENTO";

    private static final String PAGAMENTO_CONTA =
            "PAGAMENTO DE CONTA";

    private static final String RECEBIMENTO_CONTA =
            "RECEBIMENTO DE CONTA";

    private final CaixaClient caixaClient;
    private final ContaClient contaClient;
    private final ObjectMapper objectMapper;

    @InitBinder
    public void configurarBinder(
            WebDataBinder binder
    ) {
        DecimalFormatSymbols symbols =
                DecimalFormatSymbols.getInstance(
                        Locale.forLanguageTag(
                                "pt-BR"
                        )
                );

        DecimalFormat decimalFormat =
                new DecimalFormat(
                        "#,##0.00",
                        symbols
                );

        decimalFormat.setParseBigDecimal(
                true
        );

        binder.registerCustomEditor(
                BigDecimal.class,
                new CustomNumberEditor(
                        BigDecimal.class,
                        decimalFormat,
                        true
                )
        );
    }

    /* ==========================================================
       CONTAS
    ========================================================== */

    @GetMapping
    public String contas(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fim,

            @RequestParam(required = false)
            String tipo,

            @RequestParam(required = false)
            String status,

            @RequestParam(required = false)
            String pesquisa,

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
                        : hoje.withDayOfMonth(
                        hoje.lengthOfMonth()
                );

        try {
            model.addAttribute(
                    "contas",
                    contaClient.listar(
                            dataInicio,
                            dataFim,
                            tipo,
                            status,
                            pesquisa
                    )
            );

            model.addAttribute(
                    "resumoContas",
                    contaClient.resumo(
                            dataInicio,
                            dataFim
                    )
            );

        } catch (RestClientResponseException exception) {

            model.addAttribute(
                    "contas",
                    List.of()
            );

            model.addAttribute(
                    "resumoContas",
                    null
            );

            model.addAttribute(
                    "mensagemErro",
                    extrairMensagemErro(
                            exception
                    )
            );
        }

        model.addAttribute(
                "caixaAbertoAtual",
                buscarCaixaAbertoSilencioso()
        );

        model.addAttribute(
                "inicio",
                dataInicio
        );

        model.addAttribute(
                "fim",
                dataFim
        );

        model.addAttribute(
                "tipoFiltro",
                tipo
        );

        model.addAttribute(
                "statusFiltro",
                status
        );

        model.addAttribute(
                "pesquisa",
                pesquisa
        );

        model.addAttribute(
                "modo",
                "contas"
        );

        return "financeiro/index";
    }

    @GetMapping("/contas/novo")
    public String novaConta(
            @RequestParam(required = false)
            String tipo,
            Model model
    ) {
        ContaForm form =
                new ContaForm();

        if ("RECEBER".equalsIgnoreCase(tipo)) {

            form.setTipo(
                    "RECEBER"
            );

        } else {

            form.setTipo(
                    "PAGAR"
            );
        }

        form.setDataVencimento(
                LocalDate.now()
        );

        form.setRecorrente(
                false
        );

        model.addAttribute(
                "contaForm",
                form
        );

        model.addAttribute(
                "modo",
                "contaForm"
        );

        return "financeiro/index";
    }

    @GetMapping("/contas/{codigo}/editar")
    public String editarConta(
            @PathVariable
            Integer codigo,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ContaResponse conta =
                    contaClient.buscarPorCodigo(
                            codigo
                    );

            if (!"PENDENTE".equals(
                    conta.status()
            )) {

                redirectAttributes
                        .addFlashAttribute(
                                "mensagemErro",
                                "Somente contas pendentes podem ser alteradas."
                        );

                return "redirect:/financeiro";
            }

            ContaForm form =
                    new ContaForm();

            form.setCodigo(
                    conta.codigo()
            );

            form.setTipo(
                    conta.tipo()
            );

            form.setDescricao(
                    conta.descricao()
            );

            form.setFormaPagamento(
                    conta.formaPagamento()
            );

            form.setValor(
                    conta.valor()
            );

            form.setDataVencimento(
                    conta.dataVencimento()
            );

            form.setObservacao(
                    conta.observacao()
            );

            form.setRecorrente(
                    conta.recorrente()
            );

            model.addAttribute(
                    "contaForm",
                    form
            );

            model.addAttribute(
                    "modo",
                    "contaForm"
            );

            return "financeiro/index";

        } catch (RestClientResponseException exception) {

            redirectAttributes
                    .addFlashAttribute(
                            "mensagemErro",
                            extrairMensagemErro(
                                    exception
                            )
                    );

            return "redirect:/financeiro";
        }
    }

    @PostMapping("/contas/salvar")
    public String salvarConta(
            @ModelAttribute
            ContaForm contaForm,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            if (contaForm.getCodigo() == null) {

                contaClient.cadastrar(
                        contaForm.getTipo(),
                        contaForm.getDescricao(),
                        contaForm.getFormaPagamento(),
                        contaForm.getValor(),
                        contaForm.getDataVencimento(),
                        contaForm.getObservacao(),
                        contaForm.getRecorrente()
                );

                redirectAttributes
                        .addFlashAttribute(
                                "mensagemSucesso",
                                "Conta cadastrada com sucesso."
                        );

            } else {

                contaClient.atualizar(
                        contaForm.getCodigo(),
                        contaForm.getTipo(),
                        contaForm.getDescricao(),
                        contaForm.getFormaPagamento(),
                        contaForm.getValor(),
                        contaForm.getDataVencimento(),
                        contaForm.getObservacao(),
                        contaForm.getRecorrente()
                );

                redirectAttributes
                        .addFlashAttribute(
                                "mensagemSucesso",
                                "Conta atualizada com sucesso."
                        );
            }

            return "redirect:/financeiro";

        } catch (RestClientResponseException exception) {

            model.addAttribute(
                    "contaForm",
                    contaForm
            );

            model.addAttribute(
                    "modo",
                    "contaForm"
            );

            model.addAttribute(
                    "mensagemErro",
                    extrairMensagemErro(
                            exception
                    )
            );

            return "financeiro/index";
        }
    }

    @PostMapping("/contas/{codigo}/baixar")
    public String baixarConta(
            @PathVariable
            Integer codigo,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ContaResponse conta =
                    contaClient.baixar(
                            codigo
                    );

            String mensagem =
                    "PAGAR".equals(
                            conta.tipo()
                    )
                            ? "Conta paga com sucesso."
                            : "Conta recebida com sucesso.";

            if (conta.recorrente()
                    && "PAGAR".equals(
                    conta.tipo()
            )) {

                mensagem +=
                        " A próxima recorrência foi criada automaticamente.";
            }

            redirectAttributes
                    .addFlashAttribute(
                            "mensagemSucesso",
                            mensagem
                    );

        } catch (RestClientResponseException exception) {

            redirectAttributes
                    .addFlashAttribute(
                            "mensagemErro",
                            extrairMensagemErro(
                                    exception
                            )
                    );
        }

        return "redirect:/financeiro";
    }

    @PostMapping("/contas/{codigo}/cancelar")
    public String cancelarConta(
            @PathVariable
            Integer codigo,
            RedirectAttributes redirectAttributes
    ) {
        try {
            contaClient.cancelar(
                    codigo
            );

            redirectAttributes
                    .addFlashAttribute(
                            "mensagemSucesso",
                            "Conta cancelada com sucesso."
                    );

        } catch (RestClientResponseException exception) {

            redirectAttributes
                    .addFlashAttribute(
                            "mensagemErro",
                            extrairMensagemErro(
                                    exception
                            )
                    );
        }

        return "redirect:/financeiro";
    }

    /* ==========================================================
       HISTÓRICO DE CAIXAS
    ========================================================== */

    @GetMapping("/caixas")
    public String consultarCaixas(
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
                    List.of()
            );

            model.addAttribute(
                    "mensagemErro",
                    extrairMensagemErro(
                            exception
                    )
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
                "caixas"
        );

        return "financeiro/index";
    }

    /* ==========================================================
       CAIXA ABERTO
    ========================================================== */

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

                if (movimentacao != null
                        && !movimentacaoAutomatica(
                        movimentacao
                )) {

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

            if (exception
                    .getStatusCode()
                    .value() == 404) {

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
                        extrairMensagemErro(
                                exception
                        )
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
            @PathVariable
            Integer codigo,
            Model model
    ) {
        try {
            model.addAttribute(
                    "caixaDetalhe",
                    caixaClient.buscarPorCodigo(
                            codigo
                    )
            );

        } catch (RestClientResponseException exception) {

            model.addAttribute(
                    "mensagemErro",
                    extrairMensagemErro(
                            exception
                    )
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

            redirectAttributes
                    .addFlashAttribute(
                            "mensagemSucesso",
                            "Caixa aberto com sucesso."
                    );

        } catch (RestClientResponseException exception) {

            redirectAttributes
                    .addFlashAttribute(
                            "mensagemErro",
                            extrairMensagemErro(
                                    exception
                            )
                    );
        }

        return "redirect:/financeiro/caixa-aberto";
    }

    @PostMapping("/caixas/{codigo}/fechar")
    public String fechar(
            @PathVariable
            Integer codigo,
            RedirectAttributes redirectAttributes
    ) {
        try {
            caixaClient.fechar(
                    codigo
            );

            redirectAttributes
                    .addFlashAttribute(
                            "mensagemSucesso",
                            "Caixa fechado com sucesso."
                    );

            return "redirect:/financeiro/caixas";

        } catch (RestClientResponseException exception) {

            redirectAttributes
                    .addFlashAttribute(
                            "mensagemErro",
                            extrairMensagemErro(
                                    exception
                            )
                    );

            return "redirect:/financeiro/caixa-aberto";
        }
    }

    @PostMapping("/caixas/{codigo}/movimentacoes/salvar")
    public String salvarMovimentacao(
            @PathVariable
            Integer codigo,
            @ModelAttribute
            CaixaMovimentacaoForm movimentacaoForm,
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

            if (movimentacaoForm.getCodigo()
                    == null) {

                caixaClient.adicionarMovimentacao(
                        codigo,
                        request
                );

                redirectAttributes
                        .addFlashAttribute(
                                "mensagemSucesso",
                                "Movimentação adicionada com sucesso."
                        );

            } else {

                caixaClient.atualizarMovimentacao(
                        codigo,
                        movimentacaoForm.getCodigo(),
                        request
                );

                redirectAttributes
                        .addFlashAttribute(
                                "mensagemSucesso",
                                "Movimentação atualizada com sucesso."
                        );
            }

        } catch (RestClientResponseException exception) {

            redirectAttributes
                    .addFlashAttribute(
                            "mensagemErro",
                            extrairMensagemErro(
                                    exception
                            )
                    );
        }

        return "redirect:/financeiro/caixa-aberto";
    }

    @PostMapping("/caixas/{codigo}/movimentacoes/{movimentacaoCodigo}/excluir")
    public String excluirMovimentacao(
            @PathVariable
            Integer codigo,
            @PathVariable
            Integer movimentacaoCodigo,
            RedirectAttributes redirectAttributes
    ) {
        try {
            caixaClient.excluirMovimentacao(
                    codigo,
                    movimentacaoCodigo
            );

            redirectAttributes
                    .addFlashAttribute(
                            "mensagemSucesso",
                            "Movimentação excluída com sucesso."
                    );

        } catch (RestClientResponseException exception) {

            redirectAttributes
                    .addFlashAttribute(
                            "mensagemErro",
                            extrairMensagemErro(
                                    exception
                            )
                    );
        }

        return "redirect:/financeiro/caixa-aberto";
    }

    /* ==========================================================
       AUXILIARES
    ========================================================== */

    private CaixaResponse buscarCaixaAbertoSilencioso() {
        try {
            return caixaClient.buscarAberto();

        } catch (RestClientResponseException exception) {
            return null;
        }
    }

    private boolean movimentacaoAutomatica(
            CaixaMovimentacaoResponse movimentacao
    ) {
        if (movimentacao.descricao() == null) {
            return false;
        }

        String descricao =
                movimentacao
                        .descricao()
                        .trim();

        return FINALIZACAO_PROCEDIMENTO
                .equalsIgnoreCase(
                        descricao
                )
                || PAGAMENTO_CONTA
                .equalsIgnoreCase(
                        descricao
                )
                || RECEBIMENTO_CONTA
                .equalsIgnoreCase(
                        descricao
                );
    }

    private String extrairMensagemErro(
            RestClientResponseException exception
    ) {
        try {
            JsonNode json =
                    objectMapper.readTree(
                            exception
                                    .getResponseBodyAsString()
                    );

            JsonNode mensagem =
                    json.get(
                            "message"
                    );

            if (mensagem != null
                    && !mensagem
                    .asText()
                    .isBlank()) {

                return mensagem.asText();
            }

        } catch (Exception ignored) {
        }

        int status =
                exception
                        .getStatusCode()
                        .value();

        if (status == 400) {
            return "Verifique os dados informados.";
        }

        if (status == 404) {
            return "Registro não encontrado.";
        }

        if (status == 409) {
            return "Não foi possível concluir a operação.";
        }

        return "Não foi possível concluir a operação.";
    }
}