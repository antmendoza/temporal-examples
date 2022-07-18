package examples.activity.failure;

public class ActivityFailImpl implements ActivityFail {
    @Override
    public String longRunningMethod(String secondsToWait) {

        System.out.println("secondsToWait: " + secondsToWait);

        return "anything";
    }

    @Override
    public void compensateLongRunningMethod(final String compensate) {
        System.out.println("calling compensate");
    }

}
