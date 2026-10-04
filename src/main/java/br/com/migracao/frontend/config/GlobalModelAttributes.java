package br.com.migracao.frontend.config;

import br.com.migracao.frontend.security.UsuarioPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("username")
    public String username(
            Authentication authentication
    ) {
        UsuarioPrincipal principal =
                principal(authentication);

        if (principal == null) {
            return null;
        }

        return principal.getNome();
    }

    @ModelAttribute("userLogin")
    public String userLogin(
            Authentication authentication
    ) {
        UsuarioPrincipal principal =
                principal(authentication);

        return principal != null
                ? principal.getUsername()
                : null;
    }

    @ModelAttribute("userPerfil")
    public String userPerfil(
            Authentication authentication
    ) {
        UsuarioPrincipal principal =
                principal(authentication);

        return principal != null
                ? principal.getPerfil()
                : null;
    }

    @ModelAttribute("currentUserCodigo")
    public Integer currentUserCodigo(
            Authentication authentication
    ) {
        UsuarioPrincipal principal =
                principal(authentication);

        return principal != null
                ? principal.getCodigo()
                : null;
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(
            Authentication authentication
    ) {
        if (authentication == null) {
            return false;
        }

        return authentication
                .getAuthorities()
                .stream()
                .anyMatch(
                        authority ->
                                authority
                                        .getAuthority()
                                        .equals(
                                                "ROLE_ADMIN"
                                        )
                );
    }

    private UsuarioPrincipal principal(
            Authentication authentication
    ) {
        if (authentication == null
                || !authentication.isAuthenticated()) {

            return null;
        }

        if (authentication.getPrincipal()
                instanceof UsuarioPrincipal principal) {

            return principal;
        }

        return null;
    }
}