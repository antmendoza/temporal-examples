package examples.activity.failure;

import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowRule;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ActivityFailureTest {


    @Rule
    public TestWorkflowRule testWorkflowRule = TestWorkflowRule.newBuilder()
            .setWorkflowTypes(WfActivityFailImpl.class)
            .setDoNotStart(true)
            .build();

    @After
    public void cleanUp() {
        testWorkflowRule.getTestEnvironment()
                .shutdown();
    }

    @Test
    public void test() {


        ActivityFail activityFailMocked = Mockito.mock(ActivityFail.class);

        when(activityFailMocked.longRunningMethod(""))
                .thenThrow(
                        new IllegalStateException("not yet1"),
                        new IllegalStateException("not yet2"))
                .thenReturn("");


        testWorkflowRule.getWorker()
                .registerActivitiesImplementations(activityFailMocked);
        testWorkflowRule.getTestEnvironment()
                .start();


        WfActivityFail workflow = testWorkflowRule.getWorkflowClient()
                .newWorkflowStub(WfActivityFail.class, WorkflowOptions.newBuilder()
                        .setTaskQueue(testWorkflowRule.getTaskQueue())
                        .build());


        workflow.start();

        Mockito.verify(activityFailMocked, Mockito.times(2))
                .longRunningMethod(any());
        Mockito.verify(activityFailMocked, Mockito.times(1))
                .compensateLongRunningMethod(any());

    }


}
