package com.ttdevs.demo.lib.compiler;

import com.google.auto.service.AutoService;
import com.ttdevs.demo.lib.annotation.Student;
import com.ttdevs.demo.lib.compiler.utils.ClassUtils;
import com.ttdevs.demo.lib.compiler.utils.LogUtils;
import com.ttdevs.demo.lib.compiler.utils.StudentManagerBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions({StudentProcessor.OPTIONS_PARAM_DEBUG})
public class StudentProcessor extends AbstractProcessor {
    protected static final String OPTIONS_PARAM_DEBUG = "debug";

    private Filer mFiler;
    private Elements mElements; // source file
    private Map<String, Element> mClassMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        LogUtils.init(processingEnv.getMessager());
        mFiler = processingEnv.getFiler();
        mElements = processingEnv.getElementUtils();

        LogUtils.d("Init debug: " + processingEnv.getOptions().get(OPTIONS_PARAM_DEBUG));
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(ClassUtils.CLASS_STUDENT.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        LogUtils.d(String.format("=========Process %s============", !roundEnv.processingOver() ? "start" : "  end"));

        for (Element item : roundEnv.getRootElements()) {
            LogUtils.d("Process Class: " + item.getSimpleName());
        }

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ClassUtils.CLASS_STUDENT);
        if (null != elements && !elements.isEmpty()) {
            for (Element element : elements) {
                if (element.getKind() == ElementKind.CLASS) {
                    Student student = element.getAnnotation(ClassUtils.CLASS_STUDENT);
                    mClassMap.put(student.name(), element);
                }
            }
            // Create StudentManager.java
            StudentManagerBuilder.create()
                    .filer(mFiler)
                    .build(mClassMap);
        }
        return true;
    }
}
