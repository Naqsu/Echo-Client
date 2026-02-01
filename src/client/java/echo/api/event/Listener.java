package echo.api.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adnotacja oznaczająca metodę jako słuchacza zdarzeń.
 * Metoda musi przyjmować jeden argument (typ zdarzenia).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Listener {
    EventPriority priority() default EventPriority.MEDIUM;
}