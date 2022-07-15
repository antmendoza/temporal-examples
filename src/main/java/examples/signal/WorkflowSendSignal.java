package examples.signal;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface WorkflowSendSignal {


    @WorkflowMethod
    void start(WorkflowSendSignalRequest workflowSendSignalRequest);


}
