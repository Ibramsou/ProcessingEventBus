package fr.bramsou.processing.eventbus;

/**
 * event priority
 * LOWEST = method will be called in last
 * HIGH = method is called in first
 */
public enum EventPriority {
    HIGHEST,
    HIGH,
    MEDIUM,
    LOW,
    LOWEST
}
