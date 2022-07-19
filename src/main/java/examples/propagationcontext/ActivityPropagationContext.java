package examples.propagationcontext;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface ActivityPropagationContext {

    @ActivityMethod
    public String doSomething();

}
