package examples.signal;

import io.temporal.workflow.Workflow;

import java.util.Objects;

public class WorkflowWaitSignalImpl implements WorkflowWaitSignal{
    private NotifyWfRequest request;

    @Override
    public void start() {
        Workflow.await(() -> !Objects.isNull(this.request));

    }


    @Override
    public void notify(final NotifyWfRequest request) {
        this.request = request;

    }

    @Override
    public NotifyWfRequest getData() {
        return this.request;
    }

}
