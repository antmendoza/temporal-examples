package examples.cancel.activity;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.testing.TestActivityEnvironment;
import io.temporal.testing.TestWorkflowRule;
import io.temporal.worker.WorkerOptions;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

public class CancelActivitiesTest {


    @Rule
    public TestWorkflowRule testWorkflowRule = TestWorkflowRule.newBuilder()
            .setWorkflowTypes(CancelActivityWfImpl.class)
            .setWorkerOptions(WorkerOptions.newBuilder()
                    .setMaxConcurrentActivityTaskPollers(1)
                    .setMaxConcurrentActivityExecutionSize(100)
                    .build())
            .setActivityImplementations(new ActivityImpl())
            .setDoNotStart(true)
            .build();

    @After
    public void cleanUp() {
        testWorkflowRule.getTestEnvironment()
                .shutdown();
    }

    @Test
    public void test() {


        testWorkflowRule.getTestEnvironment()
                .start();


        final WorkflowClient workflowClient = testWorkflowRule.getWorkflowClient();


        WorkflowStub cancelActivityWf = workflowClient.newUntypedWorkflowStub(CancelActivityWf.class.getSimpleName(),
                WorkflowOptions.newBuilder()
                        //                .setWorkflowId("this.work")
                        .setTaskQueue(testWorkflowRule.getTaskQueue())
                        .build());


        cancelActivityWf.start();

        sleep(2);

        cancelActivityWf.signal("cancelRemainingActivities");

        sleep(5);

        cancelActivityWf.signal("completeWf");

    }


    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
