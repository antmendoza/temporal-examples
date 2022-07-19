package examples.interceptor;

import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowRule;
import io.temporal.worker.WorkerFactoryOptions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class InterceptorTest {


        private InterceptorSpy interceptorSpy = new InterceptorSpy();
    @Rule
   public TestWorkflowRule testWorkflowRule = TestWorkflowRule.newBuilder()
                .setWorkflowTypes(InterceptorWorkflowImpl.class)
                .setActivityImplementations(new InterceptorActivityImpl())
                .setWorkerFactoryOptions(WorkerFactoryOptions.newBuilder()
                        .setWorkerInterceptors(new CustomInterceptor(interceptorSpy)).build())
                .setDoNotStart(true)
                .build();



    @Test
    public void test() {


        testWorkflowRule.getTestEnvironment().start();


        final InterceptorWorkflow workflow = testWorkflowRule.getWorkflowClient().newWorkflowStub(InterceptorWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue(testWorkflowRule.getTaskQueue())
                        .build());


        final String response = workflow.doSomething("input ignored");




        Assert.assertEquals(1, interceptorSpy.getActivityInvocation());
        Assert.assertEquals("result modified from inside interceptor", response);

    }

}
