package examples.activity.retry;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface WfActivityRetry {


    @WorkflowMethod
    String start();

}
