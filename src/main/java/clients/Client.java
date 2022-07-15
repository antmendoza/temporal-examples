package clients;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowStub;
import io.temporal.failure.ActivityFailure;
import io.temporal.serviceclient.WorkflowServiceStubs;

import java.util.Optional;

public class Client {


    public static final String TASK_QUEUE = "TASK_QUEUE";
    private static final WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();

    private static final WorkflowClient client = WorkflowClient.newInstance(service);

    public static void main(String[] args) {


        WorkflowStub workflow = client.newUntypedWorkflowStub("PerioperativePatient-patientId0.8756531434633196",
                Optional.of("27419b3a-9870-43da-a8b7-a37f815572f2"), Optional.of("PerioperativePatient"));


        try {
            //workflow.getResult(PerioperativeResponse.class);
        } catch (Exception e) {

            final Throwable cause = e.getCause();

            if (cause instanceof ActivityFailure) {

                System.out.println("here we are!!!");
                System.out.println(cause);
            }

        }


    }


}
