package examples.cancel.activity;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CancelActivityWf {


    @WorkflowMethod
    String start();

    @SignalMethod
    void cancelRemainingActivities();

    @SignalMethod
    void completeWf();

}
