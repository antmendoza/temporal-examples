package examples.activity.failure;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface WfActivityFail {


    @WorkflowMethod
    String start();

}
