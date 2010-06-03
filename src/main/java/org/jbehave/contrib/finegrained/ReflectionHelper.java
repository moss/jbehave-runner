package org.jbehave.contrib.finegrained;

import org.jbehave.scenario.*;
import org.jbehave.scenario.reporters.ScenarioReporter;
import org.jbehave.scenario.steps.CandidateSteps;

import java.lang.annotation.Annotation;
import java.util.List;

public class ReflectionHelper {

    private final Class<?> invokingClass;
    private final Class<? extends JUnitScenario> testClass;
    private RunnableScenario testInstanceForReference;

    public ReflectionHelper(Class<?> invokingClass, Class<? extends JUnitScenario> testClass) {
        this.invokingClass = invokingClass;
        this.testClass = testClass;
        this.testInstanceForReference = createTestInstance();
    }

    public <T> T getInstance(Class<T> targetClass) {
        try {
            return targetClass.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(targetClass.toString()
                    + " must have default constructor to use custom runner "
                    + invokingClass.toString());
        }
    }

    public void checkForAnnotation(Class<? extends Annotation> annotationClass) {
        Annotation annotation = testClass.getAnnotation(annotationClass);
        if (annotation == null) {
            throw new IllegalArgumentException(testClass.toString()
                    + " must declare annotation " + annotationClass.toString()
                    + " to use custom runner " + invokingClass.toString());
        }
    }

    public Configuration reflectMeAConfiguration() {
        checkForAnnotation(UseConfiguration.class);
        Class<? extends Configuration> configurationClass = testClass.getAnnotation(
                UseConfiguration.class).value();
        return getInstance(configurationClass);
    }

    public List<CandidateSteps> reflectMeCandidateSteps() {
        return testInstanceForReference.getSteps();
    }

    public JUnitScenario reflectMeATestInstance(final JUnitScenarioReporter reporter) {
        final JUnitScenario newTestInstance = createTestInstance();
        Configuration configuration = newTestInstance.getConfiguration();
        newTestInstance.useConfiguration(new DelegatingConfiguration(configuration) {
            @Override public ScenarioReporter forReportingScenarios() {
                return reporter;
            }
        });
        return newTestInstance;
    }

    private JUnitScenario createTestInstance() {
        try {
            return testClass.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(testClass.toString()
                    + " must have an empty constructor in order to use custom runner "
                    + this.getClass().toString(), e);
        }
    }
}
