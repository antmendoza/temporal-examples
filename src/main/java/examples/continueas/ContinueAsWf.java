package examples.continueas;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ContinueAsWf {



    @WorkflowMethod
    void workflowMethod(ContinueAsStatus status, ContinueAsConfiguration config);

    @SignalMethod
    void onResponseReceived(String response);

    @QueryMethod
    ContinueAsStatus queryStatus();
}
