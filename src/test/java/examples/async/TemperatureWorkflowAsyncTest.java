package examples.async;

import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowRule;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

public class TemperatureWorkflowAsyncTest {


    @Rule
    public TestWorkflowRule testWorkflowRule = TestWorkflowRule.newBuilder()
            .setWorkflowTypes(TemperatureWorkflowAsyncImpl.class)
            .setDoNotStart(true)
            .build();

    @After
    public void cleanUp() {
        testWorkflowRule.getTestEnvironment()
                .shutdown();
    }


    @Test
    public void testAsync() {


        final TemperatureService temperatureService = new TemperatureServiceImpl();
        testWorkflowRule.getWorker()
                .registerActivitiesImplementations(temperatureService);

        testWorkflowRule.getTestEnvironment()
                .start();

        final TemperatureWorkflowAsync workflowAsync = testWorkflowRule.getWorkflowClient()
                .newWorkflowStub(TemperatureWorkflowAsync.class, WorkflowOptions.newBuilder()
                        .setTaskQueue(testWorkflowRule.getTaskQueue())
                        .build());


        final Double tmpAverage = workflowAsync.start(List.of("BCN", "MUR"));


        Assert.assertEquals(25, tmpAverage.doubleValue(), 0.1);


    }

}
