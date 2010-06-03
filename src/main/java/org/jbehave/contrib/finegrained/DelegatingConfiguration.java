package org.jbehave.contrib.finegrained;

import org.jbehave.scenario.Configuration;
import org.jbehave.scenario.definition.KeyWords;
import org.jbehave.scenario.errors.*;
import org.jbehave.scenario.parser.ScenarioDefiner;
import org.jbehave.scenario.reporters.*;
import org.jbehave.scenario.steps.*;

class DelegatingConfiguration implements Configuration {
    private final Configuration delegate;

    public DelegatingConfiguration(Configuration delegate) {
        this.delegate = delegate;
    }

    public ScenarioDefiner forDefiningScenarios() {
        return delegate.forDefiningScenarios();
    }

    public ScenarioReporter forReportingScenarios() {
        return delegate.forReportingScenarios();
    }

    public PendingErrorStrategy forPendingSteps() {
        return delegate.forPendingSteps();
    }

    public StepCreator forCreatingSteps() {
        return delegate.forCreatingSteps();
    }

    public ErrorStrategy forHandlingErrors() {
        return delegate.forHandlingErrors();
    }

    public KeyWords keywords() {
        return delegate.keywords();
    }

    public StepdocGenerator forGeneratingStepdoc() {
        return delegate.forGeneratingStepdoc();
    }

    public StepdocReporter forReportingStepdoc() {
        return delegate.forReportingStepdoc();
    }
}
