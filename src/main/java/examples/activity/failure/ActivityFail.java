package examples.activity.failure;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface ActivityFail {

    String longRunningMethod(String doSomething);

    void compensateLongRunningMethod(String compensate);

}
