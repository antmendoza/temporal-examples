package examples.dynamicworflow;

import io.temporal.client.WorkflowClientOptions;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.common.converter.DataConverter;
import io.temporal.testing.TestWorkflowRule;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class DynamicWorkflowTest {

    @Rule
    public TestWorkflowRule testWorkflowRule = TestWorkflowRule.newBuilder()
            .setWorkflowTypes(DynamicWorkflowImpl.class)
            .setWorkflowClientOptions(WorkflowClientOptions.newBuilder()
                    //With dataConverter
                    .setDataConverter(new MyDataConverter(DataConverter.getDefaultInstance()))
                    .build())
            .setDoNotStart(true)
            .build();

    @After
    public void cleanUp() {
        testWorkflowRule.getTestEnvironment()
                .shutdown();
    }

    @Test
    public void dataConverter() {

        testWorkflowRule.getTestEnvironment()
                .start();

        final WorkflowStub dynamicWorkflow = testWorkflowRule.getWorkflowClient()
                .newUntypedWorkflowStub("AnyDynamicWorkflow", WorkflowOptions.newBuilder()
                        .setTaskQueue(testWorkflowRule.getTaskQueue())
                        .build());

        final String encodedValue = "encodedValue";
        dynamicWorkflow.start(encodedValue);
        Assert.assertEquals(new DynamicWorkflowResponse(encodedValue),
                dynamicWorkflow.getResult(DynamicWorkflowResponse.class));

    }


    @Test
    public void activityCountWords() {


        testWorkflowRule.getWorker()
                .registerActivitiesImplementations(new DynamicWorkflowImpl.CountWordsActivityImpl());

        testWorkflowRule.getTestEnvironment()
                .start();

        final WorkflowStub dynamicWorkflow = testWorkflowRule.getWorkflowClient()
                .newUntypedWorkflowStub("AnyDynamicWorkflow", WorkflowOptions.newBuilder()
                        .setTaskQueue(testWorkflowRule.getTaskQueue())
                        .build());

        final DynamicWorkflowImpl.ACTION action = DynamicWorkflowImpl.ACTION.COUNT_WORDS;
        dynamicWorkflow.start(action, "world");
        Assert.assertEquals(new DynamicWorkflowResponse("5"), dynamicWorkflow.getResult(DynamicWorkflowResponse.class));

    }


}
