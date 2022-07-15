package examples.dynamicworflow;

import java.util.Objects;

public class DynamicWorkflowResponse {
    private String response;

    public DynamicWorkflowResponse() {

    }



    public DynamicWorkflowResponse(final String response) {

        this.response = response;
    }


    public String getResponse() {
        return response;
    }

    public void setResponse(final String response) {
        this.response = response;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DynamicWorkflowResponse that = (DynamicWorkflowResponse) o;
        return Objects.equals(response, that.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(response);
    }


    @Override
    public String toString() {
        return "DynamicWorkflowResponse{" + "response='" + response + '\'' + '}';
    }

}
