package examples.signal;

import io.temporal.workflow.Workflow;

public class WorkflowSendSignalImpl implements WorkflowSendSignal {

    @Override
    public void start(WorkflowSendSignalRequest workflowSendSignalRequest) {

        final String clientId = workflowSendSignalRequest.getClientId();
        WorkflowWaitSignal waitSignal = Workflow.newExternalWorkflowStub(WorkflowWaitSignal.class, clientId);
        final NotifyWfRequest data = new NotifyWfRequest(clientId);
        waitSignal.notify(data);

    }


}
