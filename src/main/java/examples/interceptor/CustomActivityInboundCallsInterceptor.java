package examples.interceptor;

import io.temporal.activity.ActivityExecutionContext;
import io.temporal.common.interceptors.ActivityInboundCallsInterceptor;
import io.temporal.common.interceptors.ActivityInboundCallsInterceptorBase;

public class CustomActivityInboundCallsInterceptor extends ActivityInboundCallsInterceptorBase {
    private InterceptorSpy interceptorSpy;

    public CustomActivityInboundCallsInterceptor(ActivityInboundCallsInterceptor next, InterceptorSpy interceptorSpy) {
        super(next);
        this.interceptorSpy = interceptorSpy;
    }

    @Override
    public void init(ActivityExecutionContext context) {
        super.init(context);
    }

    @Override
    public ActivityOutput execute(ActivityInput input) {
        this.interceptorSpy.incrementActivityInvocation();
        return super.execute(input);
    }
}
