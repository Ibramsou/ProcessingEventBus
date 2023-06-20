package fr.bramsou.processing.eventbus.processor;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;

public interface GeneratorInterface {

    void handle(EventGenerator generator, Set<? extends TypeElement> annotations, RoundEnvironment environment, List<String> lines);
}
