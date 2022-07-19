package examples.interceptor;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface InterceptorWorkflow {


    @WorkflowMethod
    String doSomething(String todo);
}
