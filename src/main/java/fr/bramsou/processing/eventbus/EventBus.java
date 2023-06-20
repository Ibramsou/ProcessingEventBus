package fr.bramsou.processing.eventbus;

import fr.bramsou.processing.eventbus.processor.EventGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class EventBus {

    public static EventBus initialize() {
        return new EventBus();
    }

    public static EventBus initialize(Class<?> loadingClass) {
        if (loadingClass != null) {
            try (InputStream input = loadingClass.getClassLoader().getResourceAsStream("META-INF/processing-event-bus.txt")) {
                if (input == null) throw new IllegalArgumentException("Cannot find processing event file");
                String id;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                    id = reader.readLine();
                }

                Class<? extends EventRegistry> registryClass = Class.forName(EventGenerator.GENERATED_PACKAGE + "." + String.format(EventGenerator.GENERATED_CLASS, id))
                        .asSubclass(EventRegistry.class);
                EventRegistry registry = registryClass.newInstance();
                registry.initialize();
                EventRegistry.EVENT_DATA_LIST.addAll(registry.data());
            } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return new EventBus();
    }

    protected EventBus() {}


    private final Map<Class<?>, List<EventData>> executorMap = new HashMap<>();

    /**
     * Register a new event listener handler
     * @param handler the handler
     */
    public void register(Object handler) {
        Set<Class<?>> sortingPackets = new HashSet<>();
        EventRegistry.EVENT_DATA_LIST.stream().filter(eventData -> eventData.getHandlerClass().equals(handler.getClass())).forEach(eventData -> {
            List<EventData> dataList = this.executorMap.computeIfAbsent(eventData.getPacketClass(), k -> new ArrayList<>());
            dataList.add(eventData);
            sortingPackets.add(eventData.getPacketClass());
            eventData.setHandler(handler);
        });

        Comparator<EventData> comparator = Comparator.comparingInt(value -> value.getPriority().ordinal());
        comparator = comparator.reversed();

        for (Class<?> sortingPacket : sortingPackets) {
            this.executorMap.get(sortingPacket).sort(comparator);
        }
        sortingPackets.clear();
    }

    /**
     * Unregister an handler
     * @param handler the handler
     */
    public void unregister(Object handler) {
        for (Map.Entry<Class<?>, List<EventData>> entry : this.executorMap.entrySet()) {
            List<EventData> list = entry.getValue();
            Set<EventData> toRemove = new HashSet<>(list.size());

            for (EventData eventData : list) {
                if (eventData.getHandler() != handler) continue;
                toRemove.add(eventData);
            }

            list.removeAll(toRemove);
        }
    }

    /**
     * Post an event
     * @param event event object
     */
    public void post(Object event) {
        for (EventData eventData : this.executorMap.get(event.getClass())) {
            eventData.getEventExecutor().execute(eventData.getHandler(), event);
        }
    }
}
