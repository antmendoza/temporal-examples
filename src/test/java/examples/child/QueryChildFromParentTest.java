package examples.child;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import io.temporal.activity.ActivityOptions;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.api.workflowservice.v1.DescribeWorkflowExecutionRequest;
import io.temporal.api.workflowservice.v1.DescribeWorkflowExecutionResponse;
import io.temporal.api.workflowservice.v1.ListClosedWorkflowExecutionsResponse;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.testing.TestWorkflowRule;
import io.temporal.workflow.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import query.QueryWorkflow;

import java.time.Duration;

public class QueryChildFromParentTest {


    final static ActivityOptions options = ActivityOptions.newBuilder()
            .setScheduleToCloseTimeout(Duration.ofSeconds(2))
            .build();
    private final static String PARENT_WF_ID = "workflowId";
    private final static String CHILD_WF_ID = "ChildWf";
    @Rule
    public TestWorkflowRule testWorkflowRule = TestWorkflowRule.newBuilder()
            .setWorkflowTypes(WorkflowImpl.class, ChildWfImpl.class)
            .setDoNotStart(true)
            .build();

    @After
    public void cleanUp() {
        testWorkflowRule.getTestEnvironment()
                .shutdown();
    }

    @Test
    public void test() {


        final WorkflowClient workflowClient = testWorkflowRule.getWorkflowClient();


        testWorkflowRule.getWorker()
                .registerActivitiesImplementations(new ActivityQueryChildFromParentImpl(workflowClient,
                        testWorkflowRule.getWorkflowServiceStubs()));

        final TestWorkflowEnvironment testEnvironment = testWorkflowRule.getTestEnvironment();
        testEnvironment.start();

        final WorkflowOptions workflowOptions = WorkflowOptions.newBuilder()
                .setTaskQueue(testWorkflowRule.getTaskQueue())
                .setWorkflowId(PARENT_WF_ID)
                .build();


        final WorkflowStub iWorkflow = workflowClient.newUntypedWorkflowStub("IWorkflow", workflowOptions);
        iWorkflow.start();

        sleep(400);

        DescribeWorkflowExecutionResponse childStatus = iWorkflow.query("queryChildStatus",
                DescribeWorkflowExecutionResponse.class);


        Assert.assertNotNull(childStatus);


        ListClosedWorkflowExecutionsResponse listResponse = new QueryWorkflow(testEnvironment.getWorkflowServiceStubs(),
                testEnvironment.getNamespace()).closed();


        Assert.assertEquals(2, listResponse.getExecutionsCount());

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


        @QueryMethod
        DescribeWorkflowExecutionResponse queryChildStatus();

    }

    @WorkflowInterface
    public interface ChildWf {


        @WorkflowMethod
        void start();


    }

    @ActivityInterface
    public interface ActivityQueryChildFromParent {

        @ActivityMethod
        DescribeWorkflowExecutionResponse query(final String childWorkflowId);

    }

    public static class WorkflowImpl implements IWorkflow {

        private ChildWf childWf;
        private DescribeWorkflowExecutionResponse childStatus;


        @Override
        public void start(String name) {

            childWf = Workflow.newChildWorkflowStub(ChildWf.class, ChildWorkflowOptions.newBuilder()
                    .setWorkflowId(CHILD_WF_ID)
                    .build());


            Async.procedure(childWf::start);


            ActivityQueryChildFromParent activity = Workflow.newActivityStub(ActivityQueryChildFromParent.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofSeconds(2))
                            .build());


            Promise<WorkflowExecution> childExecution = Workflow.getWorkflowExecution(childWf);

            // Call .get on the promise. This will block until the child workflow starts execution (or start
            // fails)
            final WorkflowExecution childWorkflowExecution = childExecution.get();

            this.childStatus = activity.query(childWorkflowExecution.getWorkflowId());


        }


        @Override
        public DescribeWorkflowExecutionResponse queryChildStatus() {
            return this.childStatus;
        }


    }

    public static class ChildWfImpl implements ChildWf {


        @Override
        public void start() {

        }


    }

    public static class ActivityQueryChildFromParentImpl implements ActivityQueryChildFromParent {


        private final WorkflowClient workflowClient;
        private final WorkflowServiceStubs service;

        public ActivityQueryChildFromParentImpl(
                final WorkflowClient workflowClient, final WorkflowServiceStubs service
        ) {
            this.workflowClient = workflowClient;
            this.service = service;

        }

        @Override
        public DescribeWorkflowExecutionResponse query(final String childWorkflowId) {


            DescribeWorkflowExecutionRequest describeWorkflowExecutionRequest = DescribeWorkflowExecutionRequest.newBuilder()
                    .setNamespace(workflowClient.getOptions()
                            .getNamespace())
                    .setExecution(WorkflowExecution.newBuilder()
                            .setWorkflowId(childWorkflowId)
                            .build())
                    .build();

            return service.blockingStub()
                    .describeWorkflowExecution(describeWorkflowExecutionRequest);


        }

    }

}
