package br.com.migracao.frontend.controller;

import br.com.migracao.frontend.client.CaixaClient;
import br.com.migracao.frontend.client.OdontogramaClient;
import br.com.migracao.frontend.client.PacienteClient;
import br.com.migracao.frontend.client.ProcedimentoClient;
import br.com.migracao.frontend.dto.odontograma.OdontogramaProcedimentoRequest;
import br.com.migracao.frontend.dto.odontograma.OdontogramaProcedimentoResponse;
import br.com.migracao.frontend.dto.odontograma.OdontogramaRequest;
import br.com.migracao.frontend.dto.odontograma.OdontogramaResponse;
import br.com.migracao.frontend.dto.odontograma.OdontogramaStatusRequest;
import br.com.migracao.frontend.form.OdontogramaProcedimentoForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Controller
@RequestMapping("/odontogramas")
@RequiredArgsConstructor
public class OdontogramaController {

    private final OdontogramaClient odontogramaClient;
    private final CaixaClient caixaClient;
    private final PacienteClient pacienteClient;
    private final ProcedimentoClient procedimentoClient;
    private final ObjectMapper objectMapper;

    @GetMapping
    public String pacientes(
            @RequestParam(required = false) String pesquisa,
            Model model
    ) {
        try {
            model.addAttribute(
                    "pacientes",
                    pacienteClient.pesquisar(pesquisa)
            );

        } catch (RestClientResponseException exception) {
            model.addAttribute(
                    "pacientes",
                    List.of()
            );

            model.addAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );
        }

        model.addAttribute(
                "pesquisa",
                pesquisa
        );

        return "odontograma/index";
    }

    @GetMapping("/paciente/{pacienteCodigo}")
    public String odontogramasPaciente(
            @PathVariable Integer pacienteCodigo,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            model.addAttribute(
                    "paciente",
                    pacienteClient.buscarPorCodigo(
                            pacienteCodigo
                    )
            );

            model.addAttribute(
                    "odontogramas",
                    odontogramaClient.listarPorPaciente(
                            pacienteCodigo
                    )
            );

            return "odontograma/paciente";

        } catch (RestClientResponseException exception) {
            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );

            return "redirect:/odontogramas";
        }
    }

    @PostMapping("/paciente/{pacienteCodigo}/novo")
    public String novoOdontograma(
            @PathVariable Integer pacienteCodigo,
            RedirectAttributes redirectAttributes
    ) {
        try {
            OdontogramaResponse odontograma =
                    odontogramaClient.cadastrar(
                            new OdontogramaRequest(
                                    pacienteCodigo
                            )
                    );

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Odontograma criado com sucesso."
            );

            return "redirect:/odontogramas/"
                    + odontograma.codigo();

        } catch (RestClientResponseException exception) {
            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );

            return "redirect:/odontogramas/paciente/"
                    + pacienteCodigo;
        }
    }

    @GetMapping("/{codigo}")
    public String detalhe(
            @PathVariable Integer codigo,
            @RequestParam(required = false) Integer editar,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            OdontogramaResponse odontograma =
                    odontogramaClient.buscarPorCodigo(
                            codigo
                    );

            OdontogramaProcedimentoForm form =
                    new OdontogramaProcedimentoForm();

            if (editar != null) {
                odontograma.procedimentos()
                        .stream()
                        .filter(item ->
                                editar.equals(
                                        item.codigo()
                                )
                        )
                        .findFirst()
                        .ifPresent(item ->
                                preencherForm(
                                        form,
                                        item
                                )
                        );
            }

            model.addAttribute(
                    "odontograma",
                    odontograma
            );

            model.addAttribute(
                    "procedimentosCadastro",
                    procedimentoClient.listar(null)
            );

            model.addAttribute(
                    "procedimentoForm",
                    form
            );

            model.addAttribute(
                    "dentesSuperioresDireita",
                    List.of(
                            18, 17, 16, 15,
                            14, 13, 12, 11
                    )
            );

            model.addAttribute(
                    "dentesSuperioresEsquerda",
                    List.of(
                            21, 22, 23, 24,
                            25, 26, 27, 28
                    )
            );

            model.addAttribute(
                    "dentesInferioresDireita",
                    List.of(
                            48, 47, 46, 45,
                            44, 43, 42, 41
                    )
            );

            model.addAttribute(
                    "dentesInferioresEsquerda",
                    List.of(
                            31, 32, 33, 34,
                            35, 36, 37, 38
                    )
            );

            return "odontograma/detalhe";

        } catch (RestClientResponseException exception) {
            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );

            return "redirect:/odontogramas";
        }
    }

    @PostMapping("/{codigo}/status")
    public String alterarStatus(
            @PathVariable Integer codigo,
            @RequestParam String status,
            RedirectAttributes redirectAttributes
    ) {
        try {
            odontogramaClient.alterarStatus(
                    codigo,
                    new OdontogramaStatusRequest(
                            status
                    )
            );

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Status do odontograma atualizado."
            );

        } catch (RestClientResponseException exception) {
            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );
        }

        return "redirect:/odontogramas/"
                + codigo;
    }

    @PostMapping("/{codigo}/dentes/excluir")
    public String excluirDente(
            @PathVariable Integer codigo,
            @RequestParam Integer dente,
            RedirectAttributes redirectAttributes
    ) {
        try {
            odontogramaClient.excluirDente(
                    codigo,
                    dente
            );

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Dente "
                            + dente
                            + " excluído do odontograma."
            );

        } catch (RestClientResponseException exception) {
            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );
        }

        return "redirect:/odontogramas/"
                + codigo;
    }

    @PostMapping("/{codigo}/dentes/restaurar")
    public String restaurarDente(
            @PathVariable Integer codigo,
            @RequestParam Integer dente,
            RedirectAttributes redirectAttributes
    ) {
        try {
            odontogramaClient.restaurarDente(
                    codigo,
                    dente
            );

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Dente "
                            + dente
                            + " restaurado."
            );

        } catch (RestClientResponseException exception) {
            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );
        }

        return "redirect:/odontogramas/"
                + codigo;
    }

    @PostMapping("/{codigo}/procedimentos/salvar")
    public String salvarProcedimento(
            @PathVariable Integer codigo,
            @ModelAttribute OdontogramaProcedimentoForm procedimentoForm,
            RedirectAttributes redirectAttributes
    ) {
        try {
            OdontogramaProcedimentoRequest request =
                    new OdontogramaProcedimentoRequest(
                            procedimentoForm.getProcedimentoCodigo(),
                            procedimentoForm.getDente(),
                            procedimentoForm.getStatus(),
                            procedimentoForm.getValor(),
                            procedimentoForm.getFaces(),
                            procedimentoForm.getObservacao()
                    );

            if (procedimentoForm.getCodigo() == null) {
                odontogramaClient.adicionarProcedimento(
                        codigo,
                        request
                );

                redirectAttributes.addFlashAttribute(
                        "mensagemSucesso",
                        "Procedimento adicionado ao odontograma."
                );

            } else {
                odontogramaClient.atualizarProcedimento(
                        codigo,
                        procedimentoForm.getCodigo(),
                        request
                );

                redirectAttributes.addFlashAttribute(
                        "mensagemSucesso",
                        "Procedimento atualizado."
                );
            }

        } catch (RestClientResponseException exception) {
            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );
        }

        return "redirect:/odontogramas/"
                + codigo;
    }

    @PostMapping("/{codigo}/procedimentos/{itemCodigo}/excluir")
    public String excluirProcedimento(
            @PathVariable Integer codigo,
            @PathVariable Integer itemCodigo,
            RedirectAttributes redirectAttributes
    ) {
        try {
            odontogramaClient.excluirProcedimento(
                    codigo,
                    itemCodigo
            );

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Procedimento excluído."
            );

        } catch (RestClientResponseException exception) {
            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    extrairMensagemErro(exception)
            );
        }

        return "redirect:/odontogramas/"
                + codigo;
    }

    @PostMapping("/{codigo}/procedimentos/{itemCodigo}/concluir")
    public String concluirProcedimento(
            @PathVariable Integer codigo,
            @PathVariable Integer itemCodigo,
            RedirectAttributes redirectAttributes
    ) {
        try {
            caixaClient.buscarAberto();

            odontogramaClient.concluirProcedimento(
                    codigo,
                    itemCodigo
            );

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Procedimento concluído com sucesso."
            );

        } catch (RestClientResponseException exception) {
            String mensagem =
                    exception.getStatusCode().value() == 404
                            ? "Não existe caixa aberto. Abra o caixa antes de concluir o procedimento."
                            : extrairMensagemErro(exception);

            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    mensagem
            );
        }

        return "redirect:/odontogramas/"
                + codigo;
    }

    private void preencherForm(
            OdontogramaProcedimentoForm form,
            OdontogramaProcedimentoResponse item
    ) {
        form.setCodigo(
                item.codigo()
        );

        form.setProcedimentoCodigo(
                item.procedimentoCodigo()
        );

        form.setDente(
                item.dente()
        );

        form.setStatus(
                item.status()
        );

        form.setValor(
                item.valor()
        );

        form.setFaces(
                item.faces() == null
                        ? List.of()
                        : item.faces()
        );

        form.setObservacao(
                item.observacao()
        );
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

            JsonNode detalhe =
                    json.get("detail");

            if (detalhe != null
                    && !detalhe.asText().isBlank()) {

                return detalhe.asText();
            }

        } catch (Exception ignored) {
        }

        if (exception.getStatusCode().value() == 400) {
            return "Verifique os dados informados.";
        }

        if (exception.getStatusCode().value() == 404) {
            return "Registro não encontrado.";
        }

        if (exception.getStatusCode().value() == 409) {
            return "A operação não pôde ser realizada.";
        }

        return "Não foi possível concluir a operação.";
    }
}