package fr.bramsou.processing.eventbus;

@FunctionalInterface
public interface EventExecutor {

    void execute(Object handler, Object event);
}
