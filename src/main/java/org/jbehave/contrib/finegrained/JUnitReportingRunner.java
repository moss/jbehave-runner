package org.jbehave.contrib.finegrained;

import org.jbehave.scenario.JUnitScenario;
import org.jbehave.scenario.definition.StoryDefinition;
import org.jbehave.scenario.steps.CandidateSteps;
import org.junit.runner.*;
import org.junit.runner.notification.*;

import java.util.List;

public class JUnitReportingRunner extends Runner {
    private Description storyDescription;
    private ReflectionHelper reflectionHelper;

    public JUnitReportingRunner(Class<? extends JUnitScenario> testClass) {
        reflectionHelper = new ReflectionHelper(this.getClass(), testClass);
        StoryDefinition story = reflectionHelper.reflectMeAConfiguration().forDefiningScenarios().loadScenarioDefinitionsFor(testClass);
        List<CandidateSteps> candidateSteps = reflectionHelper.reflectMeCandidateSteps();
        JUnitDescriptionGenerator descriptionGenerator = new JUnitDescriptionGenerator();
        storyDescription = descriptionGenerator.createDescriptionFrom(story, candidateSteps, testClass);
    }

    @Override
    public Description getDescription() {
        return storyDescription;
    }

    @Override
    public void run(RunNotifier notifier) {
        JUnitScenarioReporter reporter = new JUnitScenarioReporter(notifier, storyDescription);
        JUnitScenario testInstance = reflectionHelper.reflectMeATestInstance(reporter);

        try {
            testInstance.runScenario();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}