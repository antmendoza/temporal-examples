package examples.querysignalparentfromactivity;

import io.temporal.activity.Activity;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;

import java.util.List;

public class SignalQueryActivityImpl implements SignalQueryActivity {

    private final WorkflowClient workflowClient;
    private final WorkflowServiceStubs service;

    public SignalQueryActivityImpl(final WorkflowClient workflowClient) {
        this.workflowClient = workflowClient;
        this.service = workflowClient.getWorkflowServiceStubs();
    }

    @Override
    public String doSomething1(final String something) {

        String workflowId = Activity.getExecutionContext().getInfo().getWorkflowId();

        ParentWorkflow parentWorkflow = this.workflowClient.newWorkflowStub(ParentWorkflow.class, workflowId);
        parentWorkflow.updateFromActivity( "status1");

        return null;
    }


    @Override
    public String doSomething2(final String something) {

        String workflowId = Activity.getExecutionContext().getInfo().getWorkflowId();

        ParentWorkflow parentWorkflow = this.workflowClient.newWorkflowStub(ParentWorkflow.class, workflowId);

        List<String> parentStatus = parentWorkflow.queryStatus();

        parentWorkflow.updateFromActivity( parentStatus.get(0) + "-status2");

        return null;
    }
}
