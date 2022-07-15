package clients;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class Starter {

    public static final String TASK_QUEUE = "TASK_QUEUE";
    private static final String patientId = "patientId" + Math.random();
    private static final String WORKFLOW_ID = "PerioperativePatient" + "-" + patientId;
    private static final WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();

    private static final WorkflowClient client = WorkflowClient.newInstance(service);

    public static void main(String[] args) {


        WorkflowOptions workflowOptions = WorkflowOptions.newBuilder()
                .setWorkflowId(WORKFLOW_ID)
                .setTaskQueue(TASK_QUEUE)
                //                .setWorkflowRunTimeout(Duration.ofSeconds(2))
                .build();


        startWorkflow(workflowOptions);


        sendCompleteSurgeonConsultationSignal(workflowOptions);


    }

    private static void sendCompleteSurgeonConsultationSignal(final WorkflowOptions workflowOptions) {
        //        WorkflowStub workflow = client.newUntypedWorkflowStub("PerioperativePatient", workflowOptions);
        //  WorkflowStub untyped = WorkflowStub.fromTyped(workflow);
        //WorkflowStub workflow = client.newUntypedWorkflowStub(WORKFLOW_ID);
        // workflow.signal("completeSurgeonConsultation", new CompleteSurgeonConsultationRequest());


    }

    private static void startWorkflow(final WorkflowOptions workflowOptions) {
        WorkflowStub workflow1 = client.newUntypedWorkflowStub("PerioperativePatient", workflowOptions);
        //workflow1.start(new Patient(patientId));
        //WorkflowStub workflow2 = client.newUntypedWorkflowStub("PerioperativePatient-patientId");
        //WorkflowStub workflow = client.newUntypedWorkflowStub("PerioperativePatient-patientId");

        //workflow1.signal("completeSurgeonConsultation", new CompleteSurgeonConsultationRequest());

    }


}
