package echo.util.render;

import java.awt.*;

public class ColorUtil {

    public static int getRainbow(int delay, float saturation, float brightness) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), saturation, brightness).getRGB();
    }

    public static int interpolateColor(int color1, int color2, float factor) {
        float inverse = 1.0f - factor;
        int r = (int) (((color1 >> 16) & 0xFF) * inverse + ((color2 >> 16) & 0xFF) * factor);
        int g = (int) (((color1 >> 8) & 0xFF) * inverse + ((color2 >> 8) & 0xFF) * factor);
        int b = (int) ((color1 & 0xFF) * inverse + (color2 & 0xFF) * factor);
        int a = (int) (((color1 >> 24) & 0xFF) * inverse + ((color2 >> 24) & 0xFF) * factor);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int withAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    public static int createColor(int red, int green, int blue, int alpha) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static float[] decomposeColor(int color) {
        float alpha = ((color >> 24) & 0xFF) / 255.0f;
        float red = ((color >> 16) & 0xFF) / 255.0f;
        float green = ((color >> 8) & 0xFF) / 255.0f;
        float blue = (color & 0xFF) / 255.0f;

        return new float[]{red, green, blue, alpha};
    }

    public static int multiplyAlpha(int color, float multiplier) {
        int alpha = (int) (((color >> 24) & 0xFF) * multiplier);
        alpha = Math.min(Math.max(alpha, 0), 255);
        return withAlpha(color, alpha);
    }

    public static int getRainbowWave(int position, float speed, float saturation, float brightness, float waveLength) {
        float hue = ((System.currentTimeMillis() % (int) (36000 / speed)) / (36000.0f / speed) + position / waveLength) % 1.0f;
        return Color.getHSBColor(hue, saturation, brightness).getRGB();
    }
}