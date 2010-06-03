package org.jbehave.contrib.finegrained;

import org.jbehave.scenario.*;
import org.jbehave.scenario.parser.*;
import org.junit.runner.*;


@RunWith(JUnitReportingRunner.class)
public class ExampleScenario extends JUnitScenario {

    public ExampleScenario() {
        super(new MyJBehaveConfiguration(), new ExampleSteps());
    }


    public static class MyJBehaveConfiguration extends PropertyBasedConfiguration {
        @Override
        public ClasspathScenarioDefiner forDefiningScenarios() {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

            return new ClasspathScenarioDefiner(new UnderscoredCamelCaseResolver(), new PatternScenarioParser(this),
                    contextClassLoader);
        }
    }

}
