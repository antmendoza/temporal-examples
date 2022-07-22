package examples.querysignalparentfromactivity;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface SignalQueryActivity {


    String doSomething1(String something);

    String doSomething2(String something);
}
