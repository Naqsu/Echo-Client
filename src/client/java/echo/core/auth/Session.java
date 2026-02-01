package echo.core.auth;

/**
 * Przechowuje dane o aktualnie zalogowanym użytkowniku klienta (nie MC).
 */
public class Session {

    private final String username;
    private final String uid;
    private final String token;
    private final Role role;

    public Session(String username, String uid, String token, Role role) {
        this.username = username;
        this.uid = uid;
        this.token = token;
        this.role = role;
    }

    public boolean isValid() {
        return token != null && !token.isEmpty();
    }

    public enum Role {
        USER, BETA, DEVELOPER
    }
}