package echo.core.auth.alt;

/**
 * Model danych reprezentujący pojedyncze konto Minecraft.
 */
public class Alt {

    private String email;    // Email lub nazwa użytkownika (dla Offline)
    private String password; // Hasło (puste dla Microsoft OAuth w przeglądarce)
    private String username; // Nazwa w grze (pobierana po zalogowaniu)
    private String uuid;     // UUID gracza
    private AccountType type;
    private Status status;

    public Alt(String email, String password, AccountType type) {
        this.email = email;
        this.password = password;
        this.type = type;
        this.username = email; // Tymczasowo, dopóki się nie zalogujemy
        this.status = Status.UNCHECKED;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public AccountType getType() { return type; }
    public void setType(AccountType type) { this.type = type; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    // Nazwa wyświetlana na liście (maskowanie maila)
    public String getMaskedEmail() {
        if (type == AccountType.CRACKED) return email;
        return email.replaceAll("(?<=.{2}).(?=.*@)", "*");
    }

    public enum AccountType {
        MICROSOFT,
        CRACKED
    }

    public enum Status {
        WORKING("§aWorking"),
        BANNED("§cBanned"),
        UNCHECKED("§7Unchecked");

        final String label;
        Status(String label) { this.label = label; }

        @Override
        public String toString() { return label; }
    }
}