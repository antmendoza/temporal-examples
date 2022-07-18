package examples.activity.cancel;

import io.temporal.activity.ActivityCancellationType;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.failure.CanceledFailure;
import io.temporal.workflow.Async;
import io.temporal.workflow.CancellationScope;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class CancelActivityWfImpl implements CancelActivityWf {


    private final Activity activity = Workflow.newActivityStub(Activity.class, ActivityOptions.newBuilder()
            // if heartbeat timeout is not set, activity heartbeats will be throttled to one
            // every 30 seconds
            // which is too rare for the cancellations to be delivered in this example.
            .setHeartbeatTimeout(Duration.ofSeconds(5))
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setInitialInterval(Duration.ofSeconds(1))
                            .build())
            .setStartToCloseTimeout(Duration.ofSeconds(100))
            //Wait to cancel the activity, for cleaning purpose
            .setCancellationType(ActivityCancellationType.WAIT_CANCELLATION_COMPLETED)
            .build());


    CancellationScope cancellationScope;
    private boolean completeWf;


    @Override
    public String start() {
        List<Promise<String>> activityResults = new ArrayList<>();

        cancellationScope = Workflow.newCancellationScope(() -> {
            IntStream.rangeClosed(1, 3)
                    .asLongStream()
                    .forEach((secondsToWait) -> {
                        activityResults.add(Async.function(activity::longRunningMethod, secondsToWait)

                        );
                    });
        });


        // start cancellation scope
        cancellationScope.run();

        final String firsResult = Promise.anyOf(activityResults)
                .get();

        System.out.println("firsResult " +firsResult);

        Workflow.await(() -> this.completeWf);

        return firsResult;

    }

    @Override
    public void cancelRemainingActivities() {
        this.cancellationScope.cancel();
    }

    @Override
    public void completeWf() {
        this.completeWf = true;
    }

}
