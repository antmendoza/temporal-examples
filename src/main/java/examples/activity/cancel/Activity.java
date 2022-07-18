package examples.activity.cancel;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface Activity {


    @ActivityMethod
    String longRunningMethod(long timeToWait);

}
