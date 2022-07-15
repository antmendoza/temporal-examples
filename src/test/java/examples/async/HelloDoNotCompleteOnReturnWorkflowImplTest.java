package examples.async;

import io.temporal.client.ActivityCompletionClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowRule;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class HelloDoNotCompleteOnReturnWorkflowImplTest {


    @Rule
    public TestWorkflowRule testWorkflowRule = TestWorkflowRule.newBuilder()
            .setWorkflowTypes(HelloDoNotCompleteOnReturnWorkflowImpl.class)
            .setDoNotStart(true)
            .build();

    @After
    public void cleanUp() {
        testWorkflowRule.getTestEnvironment()
                .shutdown();
    }



    @Test
    public void testHello(){



        ActivityCompletionClient completionClient =
                testWorkflowRule.getWorkflowClient().newActivityCompletionClient();

        testWorkflowRule.getWorker()
                .registerActivitiesImplementations(new HelloDoNotCompleteOnReturnWorkflowImpl.HelloServiceImpl(completionClient));

        testWorkflowRule.getTestEnvironment().start();

        final HelloDoNotCompleteOnReturnWorkflow helloDoNotCompleteOnReturnWorkflow = testWorkflowRule.getWorkflowClient()
                .newWorkflowStub(HelloDoNotCompleteOnReturnWorkflow.class, WorkflowOptions.newBuilder()
                        .setTaskQueue(testWorkflowRule.getTaskQueue())
                        .build());


        final String helloResponse = helloDoNotCompleteOnReturnWorkflow.execute("Antonio");


        Assert.assertEquals("Hello Antonio", helloResponse);



    }



}
