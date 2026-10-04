package br.com.migracao.frontend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @Primary
    public RestClient coreApiRestClient(
            @Value("${app.backend.base-url}") String baseUrl,
            @Value("${app.backend.username}") String username,
            @Value("${app.backend.password}") String password
    ) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(
                        headers ->
                                headers.setBasicAuth(
                                        username,
                                        password
                                )
                )
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        "application/json"
                )
                .build();
    }

    @Bean("coreApiAuthRestClient")
    public RestClient coreApiAuthRestClient(
            @Value("${app.backend.base-url}") String baseUrl
    ) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        "application/json"
                )
                .build();
    }
}