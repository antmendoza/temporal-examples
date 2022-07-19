package examples.propagationcontext;

import org.slf4j.MDC;

import static examples.propagationcontext.WfPropagationContextImpl.KEY_CONTEXT;

public class ActivityPropagationContextImpl implements ActivityPropagationContext {

    @Override
    public String doSomething() {
        return MDC.get(KEY_CONTEXT);

    }
}
