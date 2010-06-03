package org.jbehave.contrib.finegrained;

import org.jbehave.scenario.*;
import org.jbehave.scenario.reporters.ScenarioReporter;
import org.jbehave.scenario.steps.Steps;

import java.lang.annotation.Annotation;

public class ReflectionHelper {

    private final Class<?> invokingClass;
    private final Class<? extends JUnitScenario> testClass;

    public ReflectionHelper(Class<?> invokingClass, Class<? extends JUnitScenario> testClass) {
        this.invokingClass = invokingClass;
        this.testClass = testClass;

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

    public Steps reflectMeCandidateSteps() {
        checkForAnnotation(UseSteps.class);
        Class<? extends Steps> StepsClass = testClass.getAnnotation(
                UseSteps.class).value();
        return getInstance(StepsClass);
    }

    public JUnitScenario reflectMeATestInstance(final JUnitScenarioReporter reporter) {
        try {
            final JUnitScenario testInstance = testClass.newInstance();
            Configuration configuration = testInstance.getConfiguration();
            testInstance.useConfiguration(new DelegatingConfiguration(configuration) {
                @Override public ScenarioReporter forReportingScenarios() {
                    return reporter;
                }
            });
            return testInstance;
        } catch (Exception e) {
            throw new IllegalArgumentException(testClass.toString()
                    + "must have constructor with the following args: ("
                    + ClassLoader.class.toString() + ", "
                    + ScenarioReporter.class.toString()
                    + ") in order to use custom runner "
                    + this.getClass().toString(), e);
        }
    }
}
