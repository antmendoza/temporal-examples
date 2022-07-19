package examples.propagationcontext;


import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface WfPropagationContext {

    @WorkflowMethod
    String execute();

}
