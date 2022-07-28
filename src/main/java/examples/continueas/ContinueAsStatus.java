package examples.continueas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContinueAsStatus {

    private int invocationsToContinueAs;
    private int responseReceived;
    private int expectedResponseNotReceived;

    public ContinueAsStatus() {
        this(0, 0, 0);
    }

    public ContinueAsStatus(int responseReceived,
                            int expectedResponseNotReceived,
                            int invocationsToContinueAs) {
        this.invocationsToContinueAs = invocationsToContinueAs;
        this.responseReceived = responseReceived;
        this.expectedResponseNotReceived = expectedResponseNotReceived;
    }

    public void recordExpectedResponseNotReceived() {
        this.expectedResponseNotReceived++;
    }

    public void recordResponseReceived() {
        this.responseReceived++;
    }


    public int getInvocationsToContinueAs() {
        return invocationsToContinueAs;
    }

    public ContinueAsStatus continueAs() {

        final ContinueAsStatus response = new ContinueAsStatus(
                this.responseReceived,
                expectedResponseNotReceived,
                ++this.invocationsToContinueAs
        );

        return response;
    }


    @Override
    public String toString() {
        return "ContinueAsWfStatus{" +
                "responseReceived=" + responseReceived +
                ", expectedResponseNotReceived=" + expectedResponseNotReceived +
                ", invocationsToContinueAs=" + invocationsToContinueAs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContinueAsStatus that = (ContinueAsStatus) o;
        return invocationsToContinueAs == that.invocationsToContinueAs && responseReceived == that.responseReceived && expectedResponseNotReceived == that.expectedResponseNotReceived;
    }

    @Override
    public int hashCode() {
        return Objects.hash(invocationsToContinueAs, responseReceived, expectedResponseNotReceived);
    }
}
