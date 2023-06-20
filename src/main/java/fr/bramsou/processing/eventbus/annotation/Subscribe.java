package fr.bramsou.processing.eventbus.annotation;

import fr.bramsou.processing.eventbus.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Subscribe {

    EventPriority priority() default EventPriority.MEDIUM;
}
