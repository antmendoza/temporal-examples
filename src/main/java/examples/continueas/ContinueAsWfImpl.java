package examples.continueas;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

import static java.time.Duration.ofSeconds;

public class ContinueAsWfImpl implements ContinueAsWf {
    private final int MAX_ITERATIONS = 5;
    private String response;

    private Logger log = Workflow.getLogger(ContinueAsWfImpl.class);

    private ContinueAsStatus continueAsStatus;
    private final ActivityContinueAs activity = Workflow.newActivityStub(ActivityContinueAs.class,
            ActivityOptions.newBuilder()
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setMaximumAttempts(3).build())
                    .setStartToCloseTimeout(ofSeconds(2)).build());

    @Override
    public void workflowMethod(ContinueAsStatus status, ContinueAsConfiguration config) {

        this.continueAsStatus = status;



        for (int i = 1; i <= MAX_ITERATIONS; i++) {

            activity.sendRequest(this.continueAsStatus);

            Workflow.await(ofSeconds(config.secondsToWait()), () -> response != null);

            if (response == null) {
                this.continueAsStatus.recordExpectedResponseNotReceived();
                int iteration = i + (status.getInvocationsToContinueAs()*MAX_ITERATIONS);
                boolean throwApplicationFailure = config.throwApplicationFailure(iteration);

                activity.throwApplicationFailure(throwApplicationFailure);

                continue;
            }
            this.continueAsStatus.recordResponseReceived();
            activity.onResponseReceived(response);
            resetResponse();
            //Workflow.sleep(ofSeconds(waitBeforeSendNewSignal));
        }

        Workflow.continueAsNew(this.continueAsStatus.continueAs(), config);

    }

    private void resetResponse() {
        this.response = null;
    }


    @Override
    public void onResponseReceived(String response) {
        log.info("response received.");
        this.response = response;
    }


    @Override
    public ContinueAsStatus queryStatus() {
        return this.continueAsStatus;
    }

}
