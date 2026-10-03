# frontend-web

Frontend server-side com Spring Boot 4.1.1, Java 21, Thymeleaf e Spring Security.

## Rodar

```bash
mvn spring-boot:run
```

Acesse: http://localhost:8080

Usuários locais iniciais:

- USER: `user` / `user123`
- ADMIN: `admin` / `admin123`

As credenciais podem ser substituídas por variáveis de ambiente:

- APP_USER_NAME
- APP_USER_PASSWORD
- APP_ADMIN_NAME
- APP_ADMIN_PASSWORD

Essas contas são apenas bootstrap local. A persistência/autenticação definitiva será implementada nas próximas etapas.
