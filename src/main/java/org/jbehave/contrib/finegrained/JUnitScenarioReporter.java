package org.jbehave.contrib.finegrained;

import org.jbehave.scenario.definition.*;
import org.jbehave.scenario.reporters.ScenarioReporter;
import org.junit.runner.*;
import org.junit.runner.notification.*;

import java.util.*;

public class JUnitScenarioReporter implements ScenarioReporter {

    private RunNotifier notifier;
    private Description currentScenario;
    private Description currentStep;
    private final Iterator<Description> scenarioDescriptions;
    private final Description storyDescription;
    List<Description> finishedDescriptions = new ArrayList<Description>();

    public JUnitScenarioReporter(RunNotifier notifier, Description storyDescription) {
        this.notifier = notifier;
        this.storyDescription = storyDescription;
        this.scenarioDescriptions = storyDescription.getChildren().iterator();
        this.currentScenario = this.scenarioDescriptions.next();
    }

    public void afterScenario() {
        notifier.fireTestFinished(currentScenario);
        if (scenarioDescriptions.hasNext()) {
            currentScenario = scenarioDescriptions.next();
            finishedDescriptions.clear();
        }
    }

    public void givenScenarios(List<String> givenScenarios) {
    }

    public void examplesTable(ExamplesTable table) {
    }

    public void beforeExamples(List<String> steps, ExamplesTable table) {
    }

    public void examplesTableRow(Map<String, String> tableRow) {
    }

    public void example(Map<String, String> tableRow) {
    }

    public void afterExamples() {
    }

    public void afterStory(boolean embeddedStory) {
        notifier.fireTestFinished(storyDescription);
    }

    public void afterStory() {
        afterStory(false);
    }

    public void beforeScenario(String title) {
        notifier.fireTestStarted(currentScenario);
    }

    public void beforeStory(Blurb blurb) {
        beforeStory(new StoryDefinition(blurb), false);
    }

    public void beforeStory(StoryDefinition story, boolean embeddedStory) {
        notifier.fireTestStarted(storyDescription);
    }

    public void failed(String step, Throwable e) {
        currentStep = getStepDescription(step);
        notifier.fireTestStarted(currentStep);
        notifier.fireTestFailure(new Failure(currentStep, e));
        finishedDescriptions.add(currentStep);
    }

    public void dryRun() {
    }

    public void notPerformed(String step) {
    }

    public void pending(String step) {
    }

    public void successful(String step) {
        currentStep = getStepDescription(step);
        notifier.fireTestStarted(currentStep);
        notifier.fireTestFinished(currentStep);
        finishedDescriptions.add(currentStep);
    }

    public void ignorable(String step) {
    }

    private Description getStepDescription(String step) {
        for (Description description : currentScenario.getChildren()) {
            if (!finishedDescriptions.contains(description) && match(step, description)) {
                return description;
            }
        }
        throw new RuntimeException("Could not find description for: " + step);
    }

    private boolean match(String step, Description description) {
        return description.getDisplayName().startsWith(JUnitDescriptionGenerator.getJunitSafeString(step));
    }

}
