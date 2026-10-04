package br.com.migracao.frontend.client;

import br.com.migracao.frontend.dto.prontuario.ProntuarioArquivoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProntuarioClient {

    private final RestClient coreApiRestClient;

    public List<ProntuarioArquivoResponse> listar(
            Integer pacienteCodigo
    ) {

        List<ProntuarioArquivoResponse> arquivos =
                coreApiRestClient
                        .get()
                        .uri(
                                "/api/v1/pacientes/{pacienteCodigo}/prontuario",
                                pacienteCodigo
                        )
                        .retrieve()
                        .body(
                                new ParameterizedTypeReference<>() {
                                }
                        );

        return arquivos == null
                ? List.of()
                : arquivos;
    }

    public ProntuarioArquivoResponse enviar(
            Integer pacienteCodigo,
            String titulo,
            String descricao,
            MultipartFile arquivo
    ) {

        try {

            MultiValueMap<String, Object> body =
                    new LinkedMultiValueMap<>();

            body.add(
                    "titulo",
                    titulo
            );

            body.add(
                    "descricao",
                    descricao == null
                            ? ""
                            : descricao
            );

            String nomeArquivo =
                    arquivo.getOriginalFilename();

            if (nomeArquivo == null
                    || nomeArquivo.isBlank()) {

                nomeArquivo =
                        "arquivo";
            }

            byte[] conteudo =
                    arquivo.getBytes();

            String nomeFinal =
                    nomeArquivo;

            ByteArrayResource resource =
                    new ByteArrayResource(
                            conteudo
                    ) {

                        @Override
                        public String getFilename() {

                            return nomeFinal;
                        }
                    };

            HttpHeaders headersArquivo =
                    new HttpHeaders();

            if (arquivo.getContentType() != null
                    && !arquivo.getContentType().isBlank()) {

                try {

                    headersArquivo.setContentType(
                            MediaType.parseMediaType(
                                    arquivo.getContentType()
                            )
                    );

                } catch (Exception exception) {

                    headersArquivo.setContentType(
                            MediaType.APPLICATION_OCTET_STREAM
                    );
                }

            } else {

                headersArquivo.setContentType(
                        MediaType.APPLICATION_OCTET_STREAM
                );
            }

            ResponseEntity<byte[]> arquivoPart =
                    new ResponseEntity<>(
                            conteudo,
                            headersArquivo,
                            HttpStatusCode.valueOf(200)
                    );

            body.add(
                    "arquivo",
                    resource
            );

            return coreApiRestClient
                    .post()
                    .uri(
                            "/api/v1/pacientes/{pacienteCodigo}/prontuario",
                            pacienteCodigo
                    )
                    .contentType(
                            MediaType.MULTIPART_FORM_DATA
                    )
                    .body(
                            body
                    )
                    .retrieve()
                    .body(
                            ProntuarioArquivoResponse.class
                    );

        } catch (IOException exception) {

            throw new IllegalStateException(
                    "Não foi possível ler o arquivo selecionado.",
                    exception
            );
        }
    }

    public ResponseEntity<byte[]> conteudo(
            Integer pacienteCodigo,
            Integer arquivoCodigo
    ) {

        return coreApiRestClient
                .get()
                .uri(
                        "/api/v1/pacientes/{pacienteCodigo}/prontuario/{arquivoCodigo}/conteudo",
                        pacienteCodigo,
                        arquivoCodigo
                )
                .retrieve()
                .toEntity(
                        byte[].class
                );
    }

    public void excluir(
            Integer pacienteCodigo,
            Integer arquivoCodigo
    ) {

        coreApiRestClient
                .delete()
                .uri(
                        "/api/v1/pacientes/{pacienteCodigo}/prontuario/{arquivoCodigo}",
                        pacienteCodigo,
                        arquivoCodigo
                )
                .retrieve()
                .toBodilessEntity();
    }
}