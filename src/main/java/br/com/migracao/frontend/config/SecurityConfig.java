package br.com.migracao.frontend.config;

import br.com.migracao.frontend.security.CoreApiAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CoreApiAuthenticationProvider coreApiAuthenticationProvider;

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .authenticationProvider(
                        coreApiAuthenticationProvider
                )
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(
                                        "/css/**",
                                        "/js/**",
                                        "/images/**",
                                        "/actuator/health",
                                        "/error"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/admin/**"
                                )
                                .hasRole("ADMIN")

                                .anyRequest()
                                .authenticated()
                )
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .failureUrl("/login?error")
                                .defaultSuccessUrl("/", true)
                                .permitAll()
                )
                .logout(
                        logout -> logout
                                .logoutSuccessUrl("/login?logout")
                                .invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .permitAll()
                );

        return http.build();
    }
}