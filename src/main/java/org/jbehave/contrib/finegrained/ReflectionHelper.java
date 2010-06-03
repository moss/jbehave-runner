package org.jbehave.contrib.finegrained;

import org.jbehave.scenario.*;
import org.jbehave.scenario.reporters.ScenarioReporter;
import org.jbehave.scenario.steps.CandidateSteps;

import java.util.List;

public class ReflectionHelper {

    private final Class<? extends JUnitScenario> testClass;
    private RunnableScenario testInstanceForReference;

    public ReflectionHelper(Class<? extends JUnitScenario> testClass) {
        this.testClass = testClass;
        this.testInstanceForReference = createTestInstance();
    }

    public Configuration reflectMeAConfiguration() {
        return testInstanceForReference.getConfiguration();
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
