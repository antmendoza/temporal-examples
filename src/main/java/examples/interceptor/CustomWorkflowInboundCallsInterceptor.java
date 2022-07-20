package examples.interceptor;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.interceptors.WorkflowInboundCallsInterceptor;
import io.temporal.common.interceptors.WorkflowInboundCallsInterceptorBase;
import io.temporal.common.interceptors.WorkflowOutboundCallsInterceptor;
import io.temporal.workflow.Workflow;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;

public class CustomWorkflowInboundCallsInterceptor extends WorkflowInboundCallsInterceptorBase {


    private InterceptorSpy interceptorSpy;
    private DBActivity dbActivity;

    public CustomWorkflowInboundCallsInterceptor(WorkflowInboundCallsInterceptor next, InterceptorSpy interceptorSpy) {
        super(next);
        this.interceptorSpy = interceptorSpy;

        dbActivity = Workflow.newActivityStub(DBActivity.class, ActivityOptions.newBuilder()
                .setStartToCloseTimeout(Duration.ofSeconds(2)).build());
    }

    @Override
    public void init(WorkflowOutboundCallsInterceptor outboundCalls) {
        super.init(outboundCalls);
    }

    @Override
    public WorkflowOutput execute(WorkflowInput input) {

        dbActivity.recordSomething("executed inside interceptor, before workflow execution");

        WorkflowOutput execute = super.execute(input);

        dbActivity.recordSomething("executed inside interceptor, after workflow execution");
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
