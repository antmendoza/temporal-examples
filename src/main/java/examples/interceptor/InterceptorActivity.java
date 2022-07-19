package examples.interceptor;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface InterceptorActivity {

    @ActivityMethod
    String doActivity();

}
