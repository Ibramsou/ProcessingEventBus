package fr.bramsou.processing.eventbus;

import com.google.auto.service.AutoService;
import fr.bramsou.processing.eventbus.annotation.Handler;
import fr.bramsou.processing.eventbus.processor.EventGenerator;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class EventProcessor extends AbstractProcessor {
    private EventGenerator generator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        this.generator = new EventGenerator(processingEnv.getFiler());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        generator.generate(annotations, roundEnv);
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Collections.singleton(Handler.class.getCanonicalName()));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
