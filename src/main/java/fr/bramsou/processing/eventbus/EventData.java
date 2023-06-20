package fr.bramsou.processing.eventbus;

public class EventData {

    private final Class<?> handlerClass;
    private final Class<?> packetClass;
    private final EventPriority priority;
    private final EventExecutor eventExecutor;
    private Object handler;

    public EventData(Class<?> handlerClass, Class<?> packetClass, EventPriority priority, EventExecutor eventExecutor) {
        this.handlerClass = handlerClass;
        this.packetClass = packetClass;
        this.priority = priority;
        this.eventExecutor = eventExecutor;
    }

    public Class<?> getHandlerClass() {
        return handlerClass;
    }

    public Class<?> getPacketClass() {
        return packetClass;
    }

    public EventPriority getPriority() {
        return priority;
    }

    public EventExecutor getEventExecutor() {
        return eventExecutor;
    }

    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }
}
