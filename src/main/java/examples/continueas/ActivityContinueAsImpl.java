package examples.continueas;

import io.temporal.failure.ApplicationFailure;

public class ActivityContinueAsImpl implements ActivityContinueAs {
    @Override
    public void sendRequest(ContinueAsStatus status) {


    }

    @Override
    public void throwApplicationFailure(boolean throwApplicationFailure) {
        if(throwApplicationFailure){
            throw ApplicationFailure.newFailure("Response not received in time", NullPointerException.class.getTypeName());
        }
    }

    @Override
    public void onResponseReceived(String response) {

    }
}
