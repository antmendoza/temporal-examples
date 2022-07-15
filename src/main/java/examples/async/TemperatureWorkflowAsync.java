package examples.async;


import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

import java.util.List;

@WorkflowInterface
public interface TemperatureWorkflowAsync {



    @WorkflowMethod
    double start(List<String> cities);
}
