package examples.signal;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.testing.TestWorkflowRule;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

public class SendSignalBetweenWorkflowsWorkflowTest {

    @Rule
    public TestWorkflowRule testWorkflowRule = TestWorkflowRule.newBuilder()
            .setWorkflowTypes(WorkflowWaitSignalImpl.class, WorkflowSendSignalImpl.class)
            .setDoNotStart(true)
            .build();

    @After
    public void cleanUp() {
        testWorkflowRule.getTestEnvironment()
                .shutdown();
    }

    @Test
    public void testSendSignal() {


        testWorkflowRule.getTestEnvironment()
                .start();


        final String clientId = UUID.randomUUID()
                .toString();


        final WorkflowStub workflowWaitSignal = testWorkflowRule.getWorkflowClient()
                //Get untyped workflow by workflowType
                .newUntypedWorkflowStub(WorkflowWaitSignal.WORKFLOW_NAME, WorkflowOptions.newBuilder()
                        .setWorkflowId(clientId)
                        .setTaskQueue(testWorkflowRule.getTaskQueue())
                        .build());


        workflowWaitSignal.start();
        Assert.assertNull(getDataFromWorkflowWaitSignal(workflowWaitSignal));


        final WorkflowSendSignal workflowSendSignal = testWorkflowRule.getWorkflowClient()
                .newWorkflowStub(WorkflowSendSignal.class, WorkflowOptions.newBuilder()
                        .setTaskQueue(testWorkflowRule.getTaskQueue())
                        .build());

        workflowSendSignal.start(new WorkflowSendSignalRequest(clientId));


        Assert.assertNotNull(getDataFromWorkflowWaitSignal(workflowWaitSignal));
        Assert.assertEquals(new NotifyWfRequest(clientId), getDataFromWorkflowWaitSignal(workflowWaitSignal));


    }

    private NotifyWfRequest getDataFromWorkflowWaitSignal(final WorkflowStub workflowWaitSignal) {
        return workflowWaitSignal.query("getData", NotifyWfRequest.class);
    }


}
