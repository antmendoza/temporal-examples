package examples.child;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import io.temporal.api.workflowservice.v1.ListOpenWorkflowExecutionsResponse;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.testing.TestWorkflowRule;
import io.temporal.workflow.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import query.QueryWorkflow;

public class NotifyParentFromChildTest {


    @Rule
    public TestWorkflowRule testWorkflowRule = TestWorkflowRule.newBuilder()
            .setWorkflowTypes(NotifyParentFromChildImpl.class, ChildWfImpl.class)
            .setDoNotStart(true)
            .build();

    private final static String workflowId = "workflowId";


    @After
    public void cleanUp() {
        testWorkflowRule.getTestEnvironment()
                .shutdown();
    }

    @Test
    public void test() {


        final WorkflowClient workflowClient = testWorkflowRule.getWorkflowClient();
        testWorkflowRule.getWorker().registerActivitiesImplementations(new ActivitySignalParentFromChildImpl(
                workflowClient));


        testWorkflowRule.getTestEnvironment()
                .start();

        final WorkflowOptions workflowOptions = WorkflowOptions.newBuilder()
                .setTaskQueue(testWorkflowRule.getTaskQueue())
                .setWorkflowId(workflowId)
                .build();



        final TestWorkflowEnvironment testEnvironment = testWorkflowRule.getTestEnvironment();


        final WorkflowStub iWorkflow = workflowClient.newUntypedWorkflowStub("NotifyParentFromChild", workflowOptions);
        iWorkflow.start();

        sleep(200);

        iWorkflow.signal("signal");


        sleep(200);


        final WorkflowStub childWf = workflowClient.newUntypedWorkflowStub("ChildWf");
        childWf.signal("signal_1");

        sleep(2000);

        Assert.assertEquals("signaled_1", iWorkflow.query("queryChildStatus", String.class));

        ListOpenWorkflowExecutionsResponse listResponse = new QueryWorkflow(testEnvironment.getWorkflowServiceStubs(),
                testEnvironment.getNamespace()).open();

        Assert.assertEquals(0, listResponse.getExecutionsCount());

    }


    private void sleep(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @WorkflowInterface
    public interface NotifyParentFromChild {


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

    }

    public static class NotifyParentFromChildImpl implements NotifyParentFromChild {

        private boolean signalArrived;
        private ChildWf childWf;
        private String childStatus;


        @Override
        public void start(String name) {

            childWf = Workflow.newChildWorkflowStub(ChildWf.class, ChildWorkflowOptions.newBuilder()
                    .setWorkflowId("ChildWf")
                    .build());

            Promise<Void> childWfResult = Async.procedure(childWf::start);

            childWfResult.get();


            io.temporal.workflow.Workflow.await(() -> this.signalArrived);


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

        @Override
        public void start() {

            io.temporal.workflow.Workflow.await(() -> this.signaled_1);

            NotifyParentFromChild parent =   Workflow.newExternalWorkflowStub(NotifyParentFromChild.class, Workflow.getInfo().getParentWorkflowId().get());

            parent.signalFromChild("signaled_1");

        }


        @Override
        public void signal_1() {
            this.signaled_1 = true;
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


            workflowClient.newWorkflowStub(NotifyParentFromChild.class, WorkflowOptions.newBuilder()
                    .setWorkflowId(workflowId)
                    .build()).signalFromChild(childStatus);

        }

    }

}
