package examples.interceptor;

import io.temporal.common.interceptors.ActivityInboundCallsInterceptor;
import io.temporal.common.interceptors.WorkerInterceptor;
import io.temporal.common.interceptors.WorkflowInboundCallsInterceptor;

public class CustomInterceptor implements WorkerInterceptor {

    private InterceptorSpy interceptorSpy;

    public CustomInterceptor(InterceptorSpy interceptorSpy) {
        this.interceptorSpy = interceptorSpy;
    }

    @Override
    public WorkflowInboundCallsInterceptor interceptWorkflow(WorkflowInboundCallsInterceptor next) {
        return new CustomWorkflowInboundCallsInterceptor(next, this.interceptorSpy);
    }

    @Override
    public ActivityInboundCallsInterceptor interceptActivity(ActivityInboundCallsInterceptor next) {
        return new CustomActivityInboundCallsInterceptor(next, this.interceptorSpy);
    }
}
