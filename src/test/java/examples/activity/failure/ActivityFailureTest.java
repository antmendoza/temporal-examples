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
            .setWorkflowTypes(ActivityFailWfImpl.class)
            .setDoNotStart(true)
            .build();

    @After
    public void cleanUp() {
        testWorkflowRule.getTestEnvironment()
                .shutdown();
    }

    @Test
    public void test() {


        ActivityFail activityFail = Mockito.mock(ActivityFail.class);

        when(activityFail.longRunningMethod(""))
                .thenThrow(
                        new IllegalStateException("not yet1"),
                        new IllegalStateException("not yet2"))
                .thenReturn("");


        testWorkflowRule.getWorker()
                .registerActivitiesImplementations(activityFail);
        testWorkflowRule.getTestEnvironment()
                .start();


        ActivityFailWf workflow = testWorkflowRule.getWorkflowClient()
                .newWorkflowStub(ActivityFailWf.class, WorkflowOptions.newBuilder()
                        .setTaskQueue(testWorkflowRule.getTaskQueue())
                        .build());


        workflow.start();

        Mockito.verify(activityFail, Mockito.times(2))
                .longRunningMethod(any());
        Mockito.verify(activityFail, Mockito.times(1))
                .compensateLongRunningMethod(any());

    }


}
