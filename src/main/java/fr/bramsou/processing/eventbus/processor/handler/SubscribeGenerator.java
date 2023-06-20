package fr.bramsou.processing.eventbus.processor.handler;

import fr.bramsou.processing.eventbus.EventPriority;
import fr.bramsou.processing.eventbus.processor.EventGenerator;
import fr.bramsou.processing.eventbus.processor.GeneratorInterface;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;

public class SubscribeGenerator implements GeneratorInterface {

    private final String handlerClass;
    private final String method;
    private final String packetClass;
    private final EventPriority priority;

    public SubscribeGenerator(String handlerClass, String method, String packetClass, EventPriority priority) {
        this.handlerClass = handlerClass;
        this.method = method;
        this.packetClass = packetClass;
        this.priority = priority;
    }

    @Override
    public void handle(EventGenerator generator, Set<? extends TypeElement> annotations, RoundEnvironment environment, List<String> lines) {
        lines.add(String.format(
                "this.data.add(new EventData(%s.class, %s.class, %s, (handler, event) -> ((%s) handler).%s((%s) event)));",
                this.handlerClass,
                this.packetClass,
                this.priority.getClass().getName() + "." + this.priority.name(),
                this.handlerClass,
                this.method,
                this.packetClass
        ));
    }
}
