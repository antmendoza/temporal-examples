package examples.signal;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface WorkflowWaitSignal {

    String WORKFLOW_NAME = "ABC";

    @WorkflowMethod(name=WORKFLOW_NAME)
    void start();

    @SignalMethod
    void notify(final NotifyWfRequest request);


    @QueryMethod
    NotifyWfRequest getData();


}
