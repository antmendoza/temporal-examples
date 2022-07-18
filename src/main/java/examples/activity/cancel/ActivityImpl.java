package examples.activity.cancel;

import io.temporal.activity.ActivityExecutionContext;
import io.temporal.client.ActivityCompletionException;

public class ActivityImpl implements Activity {
    @Override
    public String longRunningMethod(long secondsToWait) {


        ActivityExecutionContext executionContext = io.temporal.activity.Activity.getExecutionContext();

        System.out.println("secondsToWait" + secondsToWait);

        for (int i = 0; i < secondsToWait; i++) {
            sleep(1);
            try {
                executionContext.heartbeat(i);

//                final int numAttempts = executionContext.getInfo()
//                        .getAttempt();
//                if(numAttempts < 2){
//                    throw new IllegalStateException("test " +secondsToWait);
//                }else{
//                    System.out.println("executionContext.getInfo().getAttempt() " + numAttempts);
//                }

            } catch (ActivityCompletionException e) {
                System.out.println("ActivityId  " + secondsToWait + " shutdown.. ");
                throw e;

            }

        }


        final String s = "result " + secondsToWait;

        System.out.println(s);
        return s;
    }


    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
