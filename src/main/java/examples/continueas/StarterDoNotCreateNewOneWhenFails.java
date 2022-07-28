package examples.continueas;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.common.RetryOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class StarterDoNotCreateNewOneWhenFails {

    public static final String TASK_QUEUE = "TASK_QUEUE";
    private static final String WORKFLOW_ID = "WORKFLOW_ID";
    private static final WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();

    private static final WorkflowClient client = WorkflowClient.newInstance(service);

    public static void main(String[] args) {


        final WorkflowOptions workflowOptions = WorkflowOptions.newBuilder()
                .setWorkflowId(WORKFLOW_ID)
                .setTaskQueue(TASK_QUEUE)
                .setRetryOptions(RetryOptions.newBuilder()
                        //.setDoNotRetry(RuntimeException.class.getName())
                        .build())
                .build();


        WorkflowStub workflow = client.newUntypedWorkflowStub(ContinueAsWf.class.getSimpleName(), workflowOptions);

        workflow.start(new ContinueAsStatus(), new ContinueAsConfiguration(1, 7));


    }




}
