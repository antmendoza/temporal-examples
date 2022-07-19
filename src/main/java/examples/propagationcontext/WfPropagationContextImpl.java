package examples.propagationcontext;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import org.slf4j.MDC;

import java.time.Duration;

public class WfPropagationContextImpl implements WfPropagationContext {

    public static String KEY_CONTEXT = "test";
    public static String VALUE_CONTEXT = "someValue";


    @Override
    public String execute() {


        ActivityPropagationContext activity = Workflow.newActivityStub(ActivityPropagationContext.class,
                ActivityOptions.newBuilder()
                        .setStartToCloseTimeout(Duration.ofSeconds(2))
                        .build());


        MDC.put(KEY_CONTEXT, VALUE_CONTEXT);


        return  activity.doSomething();


    }
}
