package examples.async;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface HelloDoNotCompleteOnReturnWorkflow {

    @WorkflowMethod
    String execute(String name);


}
