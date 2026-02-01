package echo.api.event;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Szyna zdarzeń zarządzająca rejestracją i wywoływaniem metod.
 */
public class EventBus {

    // Mapa: Klasa Zdarzenia -> Lista Subskrybentów (Metod)
    private final Map<Class<?>, List<EventData>> registry = new ConcurrentHashMap<>();

    /**
     * Rejestruje obiekt, skanując go w poszukiwaniu metod z @Listener.
     */
    public void register(Object subscriber) {
        if (subscriber == null) return;

        for (Method method : subscriber.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Listener.class)) {
                // Sprawdzenie poprawności sygnatury metody
                if (method.getParameterCount() != 1) {
                    System.err.println("Invalid listener method: " + method.getName() + " in " + subscriber.getClass().getName());
                    continue;
                }

                if (!method.trySetAccessible()) {
                    System.err.println("Cannot access method: " + method.getName());
                    continue;
                }

                Class<?> eventClass = method.getParameterTypes()[0];
                Listener annotation = method.getAnnotation(Listener.class);

                List<EventData> dataList = registry.computeIfAbsent(eventClass, k -> new CopyOnWriteArrayList<>());
                EventData data = new EventData(subscriber, method, annotation.priority().getValue());

                if (!dataList.contains(data)) {
                    dataList.add(data);
                    // Sortowanie malejące (im wyższy int priorytetu, tym wcześniej na liście)
                    dataList.sort(Comparator.comparingInt(EventData::getPriority).reversed());
                }
            }
        }
    }

    /**
     * Wyrejestrowuje obiekt (usuwa wszystkie jego metody z nasłuchiwania).
     */
    public void unregister(Object subscriber) {
        if (subscriber == null) return;

        registry.values().forEach(list ->
                list.removeIf(data -> data.source == subscriber)
        );
    }

    /**
     * Główna metoda wywołująca zdarzenie.
     * Przekazuje obiekt 'event' do wszystkich zarejestrowanych metod.
     */
    public void post(Object event) {
        List<EventData> dataList = registry.get(event.getClass());

        if (dataList != null && !dataList.isEmpty()) {
            for (EventData data : dataList) {
                try {
                    data.target.invoke(data.source, event);
                } catch (Exception e) {
                    System.err.println("Error dispatching event " + event.getClass().getSimpleName());
                    e.printStackTrace();
                }
            }
        }
    }

    // Klasa wewnętrzna przechowująca informację o konkretnym listenerze
    private static class EventData {
        final Object source;   // Instancja klasy (np. KillAura)
        final Method target;   // Metoda (np. onTick)
        final int priority;

        EventData(Object source, Method target, int priority) {
            this.source = source;
            this.target = target;
            this.priority = priority;
        }

        int getPriority() {
            return priority;
        }

        // Nadpisujemy equals, żeby nie dodawać duplikatów
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EventData eventData = (EventData) o;
            return source.equals(eventData.source) && target.equals(eventData.target);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, target);
        }
    }
}