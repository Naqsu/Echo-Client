package echo.ui.components;

public class Draggable {

    protected float x, y, width, height;
    private boolean dragging;
    private float dragX, dragY;

    public Draggable(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Aktualizuje pozycję, jeśli element jest przeciągany.
     * Wywołuj to w render().
     */
    public void updatePosition(double mouseX, double mouseY) {
        if (dragging) {
            this.x = (float) (mouseX - dragX);
            this.y = (float) (mouseY - dragY);
        }
    }

    /**
     * Rozpoczyna przeciąganie, jeśli kliknięto w obszar.
     * Wywołuj to w mouseClicked().
     */
    public boolean startDragging(double mouseX, double mouseY) {
        if (isHovered(mouseX, mouseY)) {
            this.dragging = true;
            this.dragX = (float) (mouseX - x);
            this.dragY = (float) (mouseY - y);
            return true;
        }
        return false;
    }

    /**
     * Kończy przeciąganie.
     * Wywołuj to w mouseReleased().
     */
    public void stopDragging() {
        this.dragging = false;
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    // Gettery i Settery
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public float getWidth() { return width; }
    public void setWidth(float width) { this.width = width; }
    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }
}