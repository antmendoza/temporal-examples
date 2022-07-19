package examples.interceptor;

import io.temporal.common.interceptors.WorkflowInboundCallsInterceptor;
import io.temporal.common.interceptors.WorkflowInboundCallsInterceptorBase;
import io.temporal.common.interceptors.WorkflowOutboundCallsInterceptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CustomWorkflowInboundCallsInterceptor extends WorkflowInboundCallsInterceptorBase {
    public CustomWorkflowInboundCallsInterceptor(WorkflowInboundCallsInterceptor next, InterceptorSpy interceptorSpy) {
        super(next);
    }

    @Override
    public void init(WorkflowOutboundCallsInterceptor outboundCalls) {
        super.init(outboundCalls);
    }

    @Override
    public WorkflowOutput execute(WorkflowInput input) {
        WorkflowOutput execute = super.execute(input);
        return new WorkflowOutput("result modified from inside interceptor");
    }

    @Override
    public void handleSignal(SignalInput input) {
        super.handleSignal(input);
    }

    @Override
    public QueryOutput handleQuery(QueryInput input) {
        return super.handleQuery(input);
    }

    @Nonnull
    @Override
    public Object newWorkflowMethodThread(Runnable runnable, @Nullable String name) {
        return super.newWorkflowMethodThread(runnable, name);
    }

    @Nonnull
    @Override
    public Object newCallbackThread(Runnable runnable, @Nullable String name) {
        return super.newCallbackThread(runnable, name);
    }
}
