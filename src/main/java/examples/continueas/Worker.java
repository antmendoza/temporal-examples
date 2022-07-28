package examples.continueas;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.WorkerFactory;

public class Worker {

    private static final WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();

    private static final WorkflowClient client = WorkflowClient.newInstance(service);

    private static final WorkerFactory factory = WorkerFactory.newInstance(client);

    public static void main(String[] args) {

        io.temporal.worker.Worker worker = factory.newWorker(StarterDoNotCreateNewOneWhenFails.TASK_QUEUE);

        worker.registerWorkflowImplementationTypes(ContinueAsWfImpl.class);
        worker.registerActivitiesImplementations(new ActivityContinueAsImpl());

        factory.start();


    }


}
