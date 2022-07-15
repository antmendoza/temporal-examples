package query;

import io.temporal.api.workflowservice.v1.ListClosedWorkflowExecutionsRequest;
import io.temporal.api.workflowservice.v1.ListClosedWorkflowExecutionsResponse;
import io.temporal.api.workflowservice.v1.ListOpenWorkflowExecutionsRequest;
import io.temporal.api.workflowservice.v1.ListOpenWorkflowExecutionsResponse;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class QueryWorkflow {
    private final String namespace;
    private final WorkflowServiceStubs workflowServiceStubs;

    public QueryWorkflow(
            final WorkflowServiceStubs workflowServiceStubs, final String namespace
    ) {
        this.workflowServiceStubs = workflowServiceStubs;
        this.namespace = namespace;
    }

    public ListClosedWorkflowExecutionsResponse closed() {


        ListClosedWorkflowExecutionsRequest listRequest = ListClosedWorkflowExecutionsRequest.newBuilder()
                .setNamespace(this.namespace)
                .build();
        return this.workflowServiceStubs.blockingStub()
                .listClosedWorkflowExecutions(listRequest);


    }

    public ListOpenWorkflowExecutionsResponse open() {

        ListOpenWorkflowExecutionsRequest listRequest = ListOpenWorkflowExecutionsRequest.newBuilder()
                .setNamespace(this.namespace)
                .build();
        return workflowServiceStubs.blockingStub()
                .listOpenWorkflowExecutions(listRequest);


    }

}
