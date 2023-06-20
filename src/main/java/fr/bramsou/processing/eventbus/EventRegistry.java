package fr.bramsou.processing.eventbus;

import java.util.ArrayList;
import java.util.List;

public interface EventRegistry {

    List<EventData> EVENT_DATA_LIST = new ArrayList<>();

    void initialize();

    List<EventData> data();
}
