package fr.bramsou.processing.eventbus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface EventRegistry {

    Set<Class<?>> LOADED_CLASSES = new HashSet<>();
    List<EventData> EVENT_DATA_LIST = new ArrayList<>();

    void initialize();

    List<EventData> data();
}
