package examples.signal;

import java.util.Objects;

public class WorkflowSendSignalRequest {

    private String clientId;

    public WorkflowSendSignalRequest() {
    }

    public WorkflowSendSignalRequest(final String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WorkflowSendSignalRequest that = (WorkflowSendSignalRequest) o;
        return Objects.equals(clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId);
    }

}
