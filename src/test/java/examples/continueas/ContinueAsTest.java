package examples.continueas;

import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.common.RetryOptions;
import io.temporal.internal.common.WorkflowExecutionHistory;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.testing.TestWorkflowRule;
import io.temporal.worker.WorkerOptions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.time.Duration;

public class ContinueAsTest {


    @Rule
    public TestWorkflowRule testWorkflowRule = TestWorkflowRule.newBuilder()
            .setWorkflowTypes(ContinueAsWfImpl.class)
            .setWorkerOptions(WorkerOptions.newBuilder()
                    .build())
            .setActivityImplementations(new ActivityContinueAsImpl())
            .setDoNotStart(true)
            .build();

    @After
    public void cleanUp() {
        testWorkflowRule.getTestEnvironment()
                .shutdown();
    }

    @Test
    public void test() {

        final String WORKFLOW_ID = "myWfId";
        final TestWorkflowEnvironment testEnvironment = testWorkflowRule.getTestEnvironment();
        testEnvironment.start();

        final WorkflowClient workflowClient = testWorkflowRule.getWorkflowClient();

        final WorkflowStub continueAsWf = workflowClient.newUntypedWorkflowStub(ContinueAsWf.class.getSimpleName(),
                WorkflowOptions.newBuilder()
                        .setWorkflowId(WORKFLOW_ID)
                        .setRetryOptions(RetryOptions
                                .newBuilder()
                                //.setDoNotRetry(RuntimeException.class.getName())
                                .build())
                        .setTaskQueue(testWorkflowRule.getTaskQueue())
                        .build());

        continueAsWf.start(new ContinueAsStatus(), new ContinueAsConfiguration(1, 7));







        System.out.println(new WorkflowExecutionHistory(
               testWorkflowRule.getHistory(WorkflowExecution.newBuilder().setWorkflowId(WORKFLOW_ID).build())).toJson(true));


        //System.out.println(new WorkflowExecutionHistory(
        //        testWorkflowRule.getHistory(WorkflowExecution.newBuilder().setWorkflowId(WORKFLOW_ID).build())).toJson(true));

        testEnvironment.sleep(Duration.ofSeconds(500));
        //workflowClient.newUntypedWorkflowStub(WORKFLOW_ID).signal("onResponseReceived", "anyData");
        Assert.assertEquals(new ContinueAsStatus(1, 0, 0), queryWorkflowStatus(WORKFLOW_ID, workflowClient));

        testEnvironment.sleep(Duration.ofSeconds(21));
        Assert.assertEquals(new ContinueAsStatus(1, 4, 1), queryWorkflowStatus(WORKFLOW_ID, workflowClient));

        workflowClient.newUntypedWorkflowStub(WORKFLOW_ID).signal("onResponseReceived", "anyData");
        Assert.assertEquals(new ContinueAsStatus(2, 4, 1), queryWorkflowStatus(WORKFLOW_ID, workflowClient));

        testEnvironment.sleep(Duration.ofSeconds(50));
        Assert.assertEquals(new ContinueAsStatus(2, 7, 1), queryWorkflowStatus(WORKFLOW_ID, workflowClient));

        //Assert.assertEquals(new ContinueAsWfStatus(2, 4, 1), queryWorkflowStatus(WORKFLOW_ID, workflowClient));

        //System.out.println(new WorkflowExecutionHistory(
        //        testWorkflowRule.getHistory(WorkflowExecution.newBuilder().setWorkflowId(WORKFLOW_ID).build())).toJson(true));


    }

    private ContinueAsStatus queryWorkflowStatus(String WORKFLOW_ID, WorkflowClient workflowClient) {
        return workflowClient.newUntypedWorkflowStub(WORKFLOW_ID).query("queryStatus", ContinueAsStatus.class);
    }


}
