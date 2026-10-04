package br.com.migracao.frontend.controller;

import br.com.migracao.frontend.client.AgendaClient;
import br.com.migracao.frontend.client.CaixaClient;
import br.com.migracao.frontend.client.ProdutoClient;
import br.com.migracao.frontend.dto.agenda.AgendaResponse;
import br.com.migracao.frontend.dto.caixa.CaixaResponse;
import br.com.migracao.frontend.dto.produto.ProdutoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final AgendaClient agendaClient;
    private final ProdutoClient produtoClient;
    private final CaixaClient caixaClient;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home(
            Model model,
            Authentication authentication
    ) {
        LocalDate hoje =
                LocalDate.now();

        List<AgendaResponse> agendaHoje =
                carregarAgendaHoje(hoje);

        long totalPacientesHoje =
                agendaHoje.stream()
                        .map(AgendaResponse::pacienteCodigo)
                        .filter(Objects::nonNull)
                        .distinct()
                        .count();

        long totalDentistasHoje =
                agendaHoje.stream()
                        .map(AgendaResponse::dentistaCodigo)
                        .filter(Objects::nonNull)
                        .distinct()
                        .count();

        List<ProdutoResponse> produtos =
                carregarProdutos();

        List<ProdutoResponse> produtosEstoqueBaixo =
                produtos.stream()
                        .filter(this::estoqueBaixo)
                        .sorted(
                                Comparator.comparingInt(
                                        produto ->
                                                produto.quantidade() == null
                                                        ? Integer.MAX_VALUE
                                                        : produto.quantidade()
                                )
                        )
                        .toList();

        CaixaResponse caixaAberto =
                carregarCaixaAberto();

        model.addAttribute(
                "username",
                authentication != null
                        ? authentication.getName()
                        : "Usuário"
        );

        model.addAttribute(
                "hoje",
                hoje
        );

        model.addAttribute(
                "agendaHoje",
                agendaHoje
        );

        model.addAttribute(
                "agendaHojeResumo",
                agendaHoje.stream()
                        .limit(6)
                        .toList()
        );

        model.addAttribute(
                "totalAgendamentosHoje",
                agendaHoje.size()
        );

        model.addAttribute(
                "totalPacientesHoje",
                totalPacientesHoje
        );

        model.addAttribute(
                "totalDentistasHoje",
                totalDentistasHoje
        );

        model.addAttribute(
                "totalProdutosEstoqueBaixo",
                produtosEstoqueBaixo.size()
        );

        model.addAttribute(
                "produtosEstoqueBaixo",
                produtosEstoqueBaixo.stream()
                        .limit(5)
                        .toList()
        );

        model.addAttribute(
                "caixaAberto",
                caixaAberto
        );

        return "home";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin/index";
    }

    private List<AgendaResponse> carregarAgendaHoje(
            LocalDate hoje
    ) {
        try {
            return agendaClient
                    .listar(
                            hoje,
                            null
                    )
                    .stream()
                    .sorted(
                            Comparator.comparing(
                                    AgendaResponse::data
                            )
                    )
                    .toList();

        } catch (RestClientResponseException exception) {
            return List.of();
        }
    }

    private List<ProdutoResponse> carregarProdutos() {
        try {
            return produtoClient.listar(null);

        } catch (RestClientResponseException exception) {
            return List.of();
        }
    }

    private CaixaResponse carregarCaixaAberto() {
        try {
            return caixaClient.buscarAberto();

        } catch (RestClientResponseException exception) {
            return null;
        }
    }

    private boolean estoqueBaixo(
            ProdutoResponse produto
    ) {
        if (produto.quantidade() == null
                || produto.quantidadeMinima() == null) {
            return false;
        }

        if (produto.quantidadeMinima() <= 0) {
            return false;
        }

        return produto.quantidade()
                <= produto.quantidadeMinima();
    }
}