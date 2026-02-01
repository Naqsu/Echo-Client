package echo.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtil {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double getRandomInRange(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static int getRandomInRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    // Interpolacja liniowa (Lerp) dla animacji
    public static double lerp(double start, double end, double delta) {
        return start + delta * (end - start);
    }
}