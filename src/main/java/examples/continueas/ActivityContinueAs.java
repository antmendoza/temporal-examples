package examples.continueas;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface ActivityContinueAs {




    @ActivityMethod
    void sendRequest(ContinueAsStatus status);

    @ActivityMethod
    void throwApplicationFailure(boolean throwApplicationFailure);

    @ActivityMethod
    void onResponseReceived(String response);
}
