package examples.propagationcontext;

import io.temporal.client.WorkflowClientOptions;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestEnvironmentOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import org.junit.*;

import java.util.Collections;

import static examples.propagationcontext.WfPropagationContextImpl.KEY_CONTEXT;

public class WfPropagationContextImplTest {

    private static final String TASK_QUEUE = "test-workflow";

    private TestWorkflowEnvironment testEnvironment;

    @Before
    public void setUp() {
        TestEnvironmentOptions options =
                TestEnvironmentOptions.newBuilder()
                        .setWorkflowClientOptions(
                                WorkflowClientOptions.newBuilder()
                                        .setContextPropagators(Collections.singletonList(new ContextPropagatorImpl(KEY_CONTEXT)))
                                        .build())
                        .build();
        testEnvironment = TestWorkflowEnvironment.newInstance(options);
    }

    @After
    public void tearDown() {
        testEnvironment.close();
    }

    @Test
    public void test(){

        Worker worker = testEnvironment.newWorker(TASK_QUEUE);
        worker.registerWorkflowImplementationTypes(WfPropagationContextImpl.class);
        worker.registerActivitiesImplementations(new ActivityPropagationContextImpl());


        testEnvironment.start();

        WfPropagationContext workflow = testEnvironment.getWorkflowClient().newWorkflowStub(WfPropagationContext.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue(TASK_QUEUE).build());

        Assert.assertEquals(WfPropagationContextImpl.VALUE_CONTEXT, workflow.execute());


    }


}