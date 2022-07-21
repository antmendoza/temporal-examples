package examples.activity.retry;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface ActivityRetry {

    String longRunningMethod(String doSomething);

    void compensateLongRunningMethod(String compensate);

}
