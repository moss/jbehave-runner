package org.jbehave.contrib.finegrained;

import org.jbehave.scenario.JUnitScenario;
import org.jbehave.scenario.definition.*;
import org.jbehave.scenario.steps.*;
import org.junit.runner.*;

import java.util.List;

public class JUnitDescriptionGenerator {

    public Description createDescriptionFrom(ScenarioDefinition scenario,
                                             List<CandidateSteps> allCandidateSteps) {
        Class clazz = ClassUtils.getOrCreateClass(scenario.getTitle());
        Description scenarioDescription = Description.createTestDescription(clazz, scenario.getTitle());

        DescriptionTextUniquefier uniquefier = new DescriptionTextUniquefier();
        for (String stringStep : scenario.getSteps()) {
            for (CandidateSteps candidates : allCandidateSteps) {
                for (CandidateStep candidate : candidates.getSteps()) {
                    if (candidate.matches(stringStep)) {
                        String uniqueString = uniquefier.getUniqueDescription(getJunitSafeString(stringStep));
                        scenarioDescription.addChild(Description.createTestDescription(candidates.getClass(), uniqueString + " - Scenario: " + scenario.getTitle() + ""));
                    }
                }
            }
        }
        return scenarioDescription;
    }

    public Description createDescriptionFrom(StoryDefinition story,
                                             List<CandidateSteps> candidateSteps,
                                             Class<? extends JUnitScenario> testClass) {
        Description storyDescription = Description.createSuiteDescription(testClass);
        for (ScenarioDefinition definition : story.getScenarios()) {
            storyDescription.addChild(createDescriptionFrom(definition, candidateSteps));
        }
        return storyDescription;
    }

    public static String getJunitSafeString(String string) {
        return string.replaceAll("\n", ", ").replaceAll("[\\(\\)]", "|");
    }
}
