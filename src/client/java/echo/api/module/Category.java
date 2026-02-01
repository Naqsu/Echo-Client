package echo.api.module;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    VISUAL("Visual"),
    HUD("HUD"),
    PLAYER("Player"),
    SCRIPTS("Scripts");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}