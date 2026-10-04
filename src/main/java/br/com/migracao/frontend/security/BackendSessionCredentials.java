package br.com.migracao.frontend.security;

import jakarta.servlet.http.HttpSession;

public final class BackendSessionCredentials {

    public static final String USERNAME =
            "SYSODONTO_BACKEND_USERNAME";

    public static final String PASSWORD =
            "SYSODONTO_BACKEND_PASSWORD";

    private BackendSessionCredentials() {
    }

    public static void armazenar(
            HttpSession session,
            String username,
            String password
    ) {
        session.setAttribute(
                USERNAME,
                username
        );

        session.setAttribute(
                PASSWORD,
                password
        );
    }

    public static String username(
            HttpSession session
    ) {
        if (session == null) {
            return null;
        }

        return (String) session.getAttribute(
                USERNAME
        );
    }

    public static String password(
            HttpSession session
    ) {
        if (session == null) {
            return null;
        }

        return (String) session.getAttribute(
                PASSWORD
        );
    }

    public static void atualizarUsername(
            HttpSession session,
            String username
    ) {
        if (session != null) {
            session.setAttribute(
                    USERNAME,
                    username
            );
        }
    }

    public static void atualizarPassword(
            HttpSession session,
            String password
    ) {
        if (session != null) {
            session.setAttribute(
                    PASSWORD,
                    password
            );
        }
    }
}