package br.com.migracao.frontend.controller;

import br.com.migracao.frontend.client.PacienteClient;
import br.com.migracao.frontend.client.ProntuarioClient;
import br.com.migracao.frontend.dto.paciente.PacienteResponse;
import br.com.migracao.frontend.dto.prontuario.ProntuarioArquivoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Controller
@RequestMapping("/pacientes/{pacienteCodigo}/prontuario")
@RequiredArgsConstructor
public class ProntuarioController {

    private final PacienteClient pacienteClient;
    private final ProntuarioClient prontuarioClient;
    private final ObjectMapper objectMapper;

    @GetMapping
    public String prontuario(
            @PathVariable
            Integer pacienteCodigo,
            Model model
    ) {

        try {

            PacienteResponse paciente =
                    pacienteClient.buscarPorCodigo(
                            pacienteCodigo
                    );

            List<ProntuarioArquivoResponse> arquivos =
                    prontuarioClient.listar(
                            pacienteCodigo
                    );

            model.addAttribute(
                    "paciente",
                    paciente
            );

            model.addAttribute(
                    "arquivos",
                    arquivos
            );

        } catch (RestClientResponseException exception) {

            model.addAttribute(
                    "mensagemErro",
                    extrairMensagemErro(
                            exception
                    )
            );
        }

        return "paciente/prontuario";
    }

    @PostMapping("/enviar")
    public String enviar(
            @PathVariable
            Integer pacienteCodigo,

            @RequestParam
            String titulo,

            @RequestParam(required = false)
            String descricao,

            @RequestParam("arquivo")
            MultipartFile arquivo,

            RedirectAttributes redirectAttributes
    ) {

        try {

            prontuarioClient.enviar(
                    pacienteCodigo,
                    titulo,
                    descricao,
                    arquivo
            );

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Arquivo adicionado ao prontuário com sucesso."
            );

        } catch (RestClientResponseException exception) {

            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    extrairMensagemErro(
                            exception
                    )
            );

        } catch (Exception exception) {

            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    "Não foi possível enviar o arquivo."
            );
        }

        return "redirect:/pacientes/"
                + pacienteCodigo
                + "/prontuario";
    }

    @GetMapping("/{arquivoCodigo}/conteudo")
    @ResponseBody
    public ResponseEntity<byte[]> conteudo(
            @PathVariable
            Integer pacienteCodigo,

            @PathVariable
            Integer arquivoCodigo
    ) {

        try {

            return prontuarioClient.conteudo(
                    pacienteCodigo,
                    arquivoCodigo
            );

        } catch (RestClientResponseException exception) {

            return ResponseEntity
                    .status(
                            exception.getStatusCode()
                    )
                    .build();
        }
    }

    @PostMapping("/{arquivoCodigo}/excluir")
    public String excluir(
            @PathVariable
            Integer pacienteCodigo,

            @PathVariable
            Integer arquivoCodigo,

            RedirectAttributes redirectAttributes
    ) {

        try {

            prontuarioClient.excluir(
                    pacienteCodigo,
                    arquivoCodigo
            );

            redirectAttributes.addFlashAttribute(
                    "mensagemSucesso",
                    "Arquivo excluído do prontuário."
            );

        } catch (RestClientResponseException exception) {

            redirectAttributes.addFlashAttribute(
                    "mensagemErro",
                    extrairMensagemErro(
                            exception
                    )
            );
        }

        return "redirect:/pacientes/"
                + pacienteCodigo
                + "/prontuario";
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
                    json.get(
                            "message"
                    );

            if (mensagem != null
                    && !mensagem.asText().isBlank()) {

                return mensagem.asText();
            }

        } catch (Exception ignored) {
        }

        int status =
                exception
                        .getStatusCode()
                        .value();

        if (status == 404) {

            return "Paciente ou arquivo não encontrado.";
        }

        if (status == 413) {

            return "O arquivo deve possuir no máximo 20 MB.";
        }

        if (status == 400) {

            return "Verifique os dados e o arquivo informado.";
        }

        return "Não foi possível concluir a operação.";
    }
}