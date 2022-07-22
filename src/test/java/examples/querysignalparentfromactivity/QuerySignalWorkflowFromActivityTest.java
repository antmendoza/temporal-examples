package examples.querysignalparentfromactivity;

import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowRule;
import org.junit.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

public class QuerySignalWorkflowFromActivityTest {

    @Rule
    public TestWorkflowRule testWorkflowRule = TestWorkflowRule.newBuilder()
            .setWorkflowTypes(ParentWorkflowImpl.class)
            .setDoNotStart(true)
            .build();

    @After
    public void cleanUp() {
        testWorkflowRule.getTestEnvironment()
                .shutdown();
    }

    @Test
    public void test() {



        testWorkflowRule.getWorker()
                .registerActivitiesImplementations(new SignalQueryActivityImpl(testWorkflowRule.getWorkflowClient()));
        testWorkflowRule.getTestEnvironment()
                .start();


        ParentWorkflow workflow = testWorkflowRule.getWorkflowClient()
                .newWorkflowStub(ParentWorkflow.class, WorkflowOptions.newBuilder()
                        .setTaskQueue(testWorkflowRule.getTaskQueue())
                        .build());



        List<String> result = workflow.execute();
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains("status1-status2"));

    }


}