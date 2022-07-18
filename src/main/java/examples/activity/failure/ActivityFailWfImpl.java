package examples.activity.failure;

import io.temporal.activity.ActivityCancellationType;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.workflow.Async;
import io.temporal.workflow.CancellationScope;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ActivityFailWfImpl implements ActivityFailWf {


    private final ActivityFail activity = Workflow.newActivityStub(ActivityFail.class, ActivityOptions.newBuilder()
            // if heartbeat timeout is not set, activity heartbeats will be throttled to one
            // every 30 seconds
            // which is too rare for the cancellations to be delivered in this example.
            .setHeartbeatTimeout(Duration.ofSeconds(5))
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setInitialInterval(Duration.ofSeconds(1))
                            .setMaximumAttempts(2)
                            .build())
            .setStartToCloseTimeout(Duration.ofSeconds(100))
            //Wait to cancel the activity, for cleaning purpose
            .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
            .build());



    @Override
    public String start() {

        try{
            activity.longRunningMethod("");

        }catch (ActivityFailure activityFailure){
            activity.compensateLongRunningMethod("");

        }

        return "done";

    }

}
