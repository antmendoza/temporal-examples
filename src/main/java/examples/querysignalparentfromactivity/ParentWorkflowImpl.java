package examples.querysignalparentfromactivity;

import io.temporal.activity.ActivityOptions;
import io.temporal.api.enums.v1.ParentClosePolicy;
import io.temporal.api.workflowservice.v1.DescribeWorkflowExecutionResponse;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ParentWorkflowImpl implements ParentWorkflow {
    private DescribeWorkflowExecutionResponse childStatus;
    private final List<String> status = new ArrayList<>();

    @Override
    public List<String> execute() {

        // We set the parentClosePolicy to "Abandon"
        // This will allow child workflow to continue execution after parent completes
        ChildWorkflowOptions childWorkflowOptions =
                ChildWorkflowOptions.newBuilder()
                        .setWorkflowId("childWorkflow")
                        .setParentClosePolicy(ParentClosePolicy.PARENT_CLOSE_POLICY_ABANDON)
                        .build();

        SignalQueryActivity signalQueryActivity =
                Workflow.newActivityStub(
                        SignalQueryActivity.class,
                        ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build());


        signalQueryActivity.doSomething1("");
        signalQueryActivity.doSomething2("");


        return status;
    }

    @Override
    public void updateFromActivity(String status) {
        this.status.add(status);
    }

    @Override
    public List<String> queryStatus() {
        return this.status;
    }

}
