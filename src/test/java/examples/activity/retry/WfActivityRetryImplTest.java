package examples.activity.retry;

import io.temporal.client.WorkflowOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.testing.TestWorkflowRule;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class WfActivityRetryImplTest {


    @Rule
    public TestWorkflowRule testWorkflowRule = TestWorkflowRule.newBuilder()
            .setWorkflowTypes(WfActivityRetryImpl.class)
            .setDoNotStart(true)
            .build();

    @After
    public void cleanUp() {
        testWorkflowRule.getTestEnvironment()
                .shutdown();
    }

    @Test
    public void test() {


        ActivityRetry activityRetryMocked = Mockito.mock(ActivityRetry.class);

        when(activityRetryMocked.longRunningMethod(""))
                .thenThrow(
                        ApplicationFailure.newFailure("not yet1","com.my.CustomException"),
                        new IllegalStateException("not yet2")
                        )
                .thenReturn("");


        testWorkflowRule.getWorker()
                .registerActivitiesImplementations(activityRetryMocked);
        testWorkflowRule.getTestEnvironment()
                .start();


        WfActivityRetry workflow = testWorkflowRule.getWorkflowClient()
                .newWorkflowStub(WfActivityRetry.class, WorkflowOptions.newBuilder()
                        .setTaskQueue(testWorkflowRule.getTaskQueue())
                        .build());


        workflow.start();

        Mockito.verify(activityRetryMocked, Mockito.times(2))
                .longRunningMethod(any());
        Mockito.verify(activityRetryMocked, Mockito.times(1))
                .compensateLongRunningMethod(any());

    }


}
