package standalone;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import io.temporal.activity.ActivityOptions;
import io.temporal.api.workflowservice.v1.ListClosedWorkflowExecutionsRequest;
import io.temporal.api.workflowservice.v1.ListClosedWorkflowExecutionsResponse;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.testing.TestWorkflowRule;
import io.temporal.workflow.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.time.Duration;

public class NotifyParentFromChildTest {



    @Rule
    public TestWorkflowRule testWorkflowRule = TestWorkflowRule.newBuilder()
            .setWorkflowTypes(WorkflowImpl.class, ChildWfImpl.class)
            .setDoNotStart(true)
            .build();

    private final static String PARENT_WF_ID = "workflowId";


    private final static String    CHILD_WF_ID = "ChildWf";


    final static ActivityOptions options = ActivityOptions.newBuilder()
            .setScheduleToCloseTimeout(Duration.ofSeconds(2))
            .build();


    @After
    public void cleanUp() {
        testWorkflowRule.getTestEnvironment()
                .shutdown();
    }

    @Test
    public void test() {


        final WorkflowClient workflowClient = testWorkflowRule.getWorkflowClient();


        testWorkflowRule.getWorker().registerActivitiesImplementations(new ActivitySignalParentFromChildImpl(workflowClient));


        testWorkflowRule.getTestEnvironment()
                .start();

        final WorkflowOptions workflowOptions = WorkflowOptions.newBuilder()
                .setTaskQueue(testWorkflowRule.getTaskQueue())
                .setWorkflowId(PARENT_WF_ID)
                .build();


        final WorkflowStub iWorkflow = workflowClient.newUntypedWorkflowStub("IWorkflow", workflowOptions);
        iWorkflow.start();

        sleep(200);

        iWorkflow.signal("signal");


        sleep(200);


        final WorkflowStub childWf = workflowClient.newUntypedWorkflowStub(CHILD_WF_ID);
        childWf.signal("signal_1");

        sleep(2000);


        Assert.assertEquals("childStatus", iWorkflow.query("queryChildStatus", String.class));

        ListClosedWorkflowExecutionsRequest listRequest = ListClosedWorkflowExecutionsRequest.newBuilder()
                .setNamespace(testWorkflowRule.getTestEnvironment()
                        .getNamespace())
                .build();
        ListClosedWorkflowExecutionsResponse listResponse = testWorkflowRule.getTestEnvironment()
                .getWorkflowServiceStubs()
                .blockingStub()
                .listClosedWorkflowExecutions(listRequest);


        Assert.assertEquals(1, listResponse.getExecutionsCount());

    }


    private void sleep(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @WorkflowInterface
    public interface IWorkflow {


        @WorkflowMethod
        void start(String name);

        @SignalMethod
        void signal();

        @SignalMethod
        void signalFromChild(String childStatus);

        @QueryMethod
        String queryChildStatus();

    }

    @WorkflowInterface
    public interface ChildWf {


        @WorkflowMethod
        void start();


        @SignalMethod
        void signal_1();

        @SignalMethod
        void signal_2();

    }

    public static class WorkflowImpl implements IWorkflow {

        private boolean signalArrived;
        private ChildWf childWf;
        private String childStatus;


        @Override
        public void start(String name) {

            childWf = Workflow.newChildWorkflowStub(ChildWf.class, ChildWorkflowOptions.newBuilder()
                    .setWorkflowId(CHILD_WF_ID)
                    .build());

            Promise<Void> childWfResult = Async.procedure(childWf::start);

            childWfResult.get();


            Workflow.await(() -> this.signalArrived);


        }

        @Override
        public void signal() {
            this.signalArrived = true;
        }

        @Override
        public void signalFromChild(String childStatus) {
            this.childStatus = childStatus;
        }

        @Override
        public String queryChildStatus() {
            return this.childStatus;
        }


    }

    public static class ChildWfImpl implements ChildWf {

        private boolean signaled_1;
        private boolean signaled_2;

        @Override
        public void start() {



            Workflow.await(() -> this.signaled_1);

            ActivitySignalParentFromChild queryChild = Workflow.newActivityStub(ActivitySignalParentFromChild.class, options);

            queryChild.signalParent("signaled_1");


            Workflow.await(() -> this.signaled_2);

            queryChild.signalParent("signaled_2");


        }


        @Override
        public void signal_1() {
            this.signaled_1 = true;
        }

        @Override
        public void signal_2() {

            this.signaled_2 = true;
        }

    }



    @ActivityInterface
    public interface ActivitySignalParentFromChild {

        @ActivityMethod
        void signalParent(final String childStatus);

    }


    public static class ActivitySignalParentFromChildImpl implements ActivitySignalParentFromChild {


        private final WorkflowClient workflowClient;

        public ActivitySignalParentFromChildImpl(final WorkflowClient workflowClient) {
            this.workflowClient = workflowClient;

        }

        @Override
        public void signalParent(final String childStatus) {


            workflowClient.newWorkflowStub(IWorkflow.class, WorkflowOptions.newBuilder()
                    .setWorkflowId(PARENT_WF_ID)
                    .build()).signalFromChild(childStatus);

        }

    }

}
