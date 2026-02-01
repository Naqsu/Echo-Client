package echo.api.event.impl;

/**
 * Zdarzenie aktualizacji gracza (UpdateWalkingPlayer).
 * Wywoływane przed i po wysłaniu pakietów ruchu.
 */
public class MotionEvent {

    private double x, y, z;
    private float yaw, pitch;
    private boolean onGround;
    private final Phase phase;

    public MotionEvent(double x, double y, double z, float yaw, float pitch, boolean onGround, Phase phase) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.phase = phase;
    }

    public boolean isPre() {
        return phase == Phase.PRE;
    }

    public boolean isPost() {
        return phase == Phase.POST;
    }

    // --- Gettery ---

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
    public boolean isOnGround() { return onGround; }
    public Phase getPhase() { return phase; }

    // --- Settery (Tylko dla fazy PRE, modyfikują pakiety) ---

    public void setX(double x) {
        if (isPre()) this.x = x;
    }

    public void setY(double y) {
        if (isPre()) this.y = y;
    }

    public void setZ(double z) {
        if (isPre()) this.z = z;
    }

    public void setYaw(float yaw) {
        if (isPre()) this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        if (isPre()) this.pitch = pitch;
    }

    public void setOnGround(boolean onGround) {
        if (isPre()) this.onGround = onGround;
    }

    // --- Helpery do rotacji ---

    public void setRotations(float yaw, float pitch) {
        setYaw(yaw);
        setPitch(pitch);
    }

    public enum Phase {
        PRE,  // Przed wysłaniem pakietu (można edytować)
        POST  // Po wysłaniu pakietu (tylko odczyt/cleanup)
    }
}