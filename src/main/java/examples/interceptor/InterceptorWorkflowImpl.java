package examples.interceptor;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class InterceptorWorkflowImpl implements InterceptorWorkflow {
    @Override
    public String doSomething(String todo) {
        final InterceptorActivity activity = Workflow.newActivityStub(InterceptorActivity.class, ActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofSeconds(2)).build());

        return "wfResponse " + activity.doActivity();

    }
}
