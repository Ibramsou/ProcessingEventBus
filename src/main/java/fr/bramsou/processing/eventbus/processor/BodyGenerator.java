package fr.bramsou.processing.eventbus.processor;

import fr.bramsou.processing.eventbus.EventPriority;
import fr.bramsou.processing.eventbus.annotation.Subscribe;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import java.util.List;
import java.util.Set;

public abstract class BodyGenerator {

    protected final void generateBody(Set<? extends TypeElement> annotations, RoundEnvironment environment, List<String> lines) {
        annotations.forEach(annotation -> environment.getElementsAnnotatedWith(annotation).forEach(element -> {
            String handlerClassName = element.asType().toString();
            element.getEnclosedElements().stream().filter(internalElement -> internalElement.getKind() == ElementKind.METHOD).forEach(method -> {
                ExecutableElement executable = (ExecutableElement) method;
                Subscribe subscribe = executable.getAnnotation(Subscribe.class);
                if (subscribe == null) return;
                EventPriority priority = subscribe.priority();
                List<? extends VariableElement> parameters = executable.getParameters();
                if (parameters.size() != 1) {
                    throw new IllegalArgumentException(String.format("Cannot subscribe the method %s, excepted 1 parameter, found %s", method.getSimpleName(), parameters.size()));
                }
                String eventClass = parameters.get(0).asType().toString();
                String methodName = method.getSimpleName().toString();
                this.generateData(lines, handlerClassName, eventClass, methodName, priority);
            });
            for (Element internalElement : element.getEnclosedElements()) {
                if (internalElement.getKind() == ElementKind.METHOD) {
                    ExecutableElement executableElement = (ExecutableElement) internalElement;

                }
            }
        }));
    }

    private void generateData(List<String> lines, String handlerClass, String eventClass, String method, EventPriority priority) {
        lines.add(String.format(
                "this.data.add(new EventData(%s.class, %s.class, %s, (handler, event) -> ((%s) handler).%s((%s) event)));",
                handlerClass,
                eventClass,
                priority.getClass().getName() + "." + priority.name(),
                handlerClass,
                method,
                eventClass
        ));
    }
}
