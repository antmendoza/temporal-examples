package examples.interceptor;

import io.temporal.api.common.v1.Payload;
import io.temporal.api.common.v1.Payloads;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.api.history.v1.History;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.converter.DataConverter;
import io.temporal.testing.TestWorkflowRule;
import io.temporal.worker.WorkerFactoryOptions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.stream.Collectors;

public class InterceptorTest {


        private InterceptorSpy interceptorSpy = new InterceptorSpy();
    @Rule
   public TestWorkflowRule testWorkflowRule = TestWorkflowRule.newBuilder()
                .setWorkflowTypes(InterceptorWorkflowImpl.class)
                .setActivityImplementations(new InterceptorActivityImpl(), new DBActivityImpl())
                .setWorkerFactoryOptions(WorkerFactoryOptions.newBuilder()
                        .setWorkerInterceptors(new CustomInterceptor(interceptorSpy)).build())
                .setDoNotStart(true)
                .build();


    @After
    public void cleanUp() {
        testWorkflowRule.getTestEnvironment()
                .shutdown();
    }

    @Test
    public void test() {


        testWorkflowRule.getTestEnvironment().start();


        String workflowId = "anyE";
        final InterceptorWorkflow workflow = testWorkflowRule.getWorkflowClient().newWorkflowStub(InterceptorWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workflowId)
                        .setTaskQueue(testWorkflowRule.getTaskQueue())
                        .build());

        final String response = workflow.doSomething("input ignored");

        final WorkflowExecution workflowExecution = WorkflowExecution.newBuilder().setWorkflowId(workflowId).build();
        final History history = testWorkflowRule.getHistory(workflowExecution);

        // we are executing three activities, one from inside the workflow and two from an interceptor.
        Assert.assertEquals(3, interceptorSpy.getActivityInvocation());

        Assert.assertEquals(23, history.getEventsCount());

        Assert.assertEquals(1, countEventsWithInputValue(history, "executed inside interceptor, before workflow execution"));
        Assert.assertEquals(1, countEventsWithInputValue(history, "executed inside interceptor, after workflow execution"));

        Assert.assertEquals("result modified from inside interceptor", response);

    }

    private int countEventsWithInputValue(History history, String anObject) {
        return history.getEventsList().stream().filter(event -> {

            final Payloads activityInput = event.getActivityTaskScheduledEventAttributes().getInput();

            if (activityInput.getPayloadsCount() > 0) {
                Payload payload = activityInput.getPayloads(0);
                String input = DataConverter.getDefaultInstance().fromPayload(payload, String.class, String.class);

                boolean equals = input.equals(anObject);


                return equals;

            }
            return false;
        }).collect(Collectors.toList()).size();
    }

}
