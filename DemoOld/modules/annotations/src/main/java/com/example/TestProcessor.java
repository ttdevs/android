package com.example;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

/**
 * Created by ttdevs
 * 2017-08-06 (android)
 * https://github.com/ttdevs
 */
public class TestProcessor extends AbstractProcessor {
    private ProcessingEnvironment envir;

    public TestProcessor(ProcessingEnvironment env) {
        this.envir = env;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return super.getSupportedAnnotationTypes();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement typeEle : annotations) {
//            WondertwoInterface wondertwoInterface = typeEle.getAnnotation(WondertwoInterface.class);
//            if (wondertwoInterface == null) break;
//
//            Class clazz = typeEle.getClass();
//            if (clazz.getDeclaredMethods().length > 0) {
//                try {
//                    if (typeEle.getModifiers().contains(Modifier.PUBLIC)
//                            && !typeEle.getModifiers().contains(Modifier.STATIC)) {
//                        PrintWriter writer = (PrintWriter) envir.getFiler()
//                                .createSourceFile(wondertwoInterface.value());
//                        writer.println("package " + clazz.getPackage().getName() + ";");
//                        writer.println("public interface " + wondertwoInterface.value() + " {");
//                        for (Method method : clazz.getDeclaredMethods()) {
//                            writer.print("    public ");
//                            writer.print(method.getReturnType() + " ");
//                            writer.print(method.getName() + " (");
//                            int i = 0;
//                            for (TypeParameterElement parameter : typeEle.getTypeParameters()) {
//                                writer.print(parameter.asType() + " " + parameter.getSimpleName());
//                                if (++i < typeEle.getTypeParameters().size())
//                                    writer.print(", ");
//                            }
//                            writer.println(");");
//                        }
//                        writer.println("}");
//                        writer.close();
//                    }
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
        }
        return true;
    }
}
