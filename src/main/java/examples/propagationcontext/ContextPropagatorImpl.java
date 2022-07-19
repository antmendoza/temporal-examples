package examples.propagationcontext;

import io.temporal.api.common.v1.Payload;
import io.temporal.common.context.ContextPropagator;
import io.temporal.common.converter.DataConverter;
import org.slf4j.MDC;

import java.util.Collections;
import java.util.Map;

public class ContextPropagatorImpl implements ContextPropagator {

    private final String contextKey;

    public ContextPropagatorImpl(String contextKey) {
        this.contextKey = contextKey;
    }


    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public Map<String, Payload> serializeContext(Object context) {
        String testKey = (String) context;
        if (testKey != null) {
            return Collections.singletonMap(
                    contextKey, DataConverter.getDefaultInstance().toPayload(testKey).get());
        } else {
            return Collections.emptyMap();
        }
    }

    @Override
    public Object deserializeContext(Map<String, Payload> context) {
        if (context.containsKey(contextKey)) {
            return DataConverter.getDefaultInstance()
                    .fromPayload(context.get(contextKey), String.class, String.class);

        } else {
            return null;
        }
    }

    @Override
    public Object getCurrentContext() {
        return MDC.get(contextKey);
    }

    @Override
    public void setCurrentContext(Object context) {
        MDC.put(contextKey, String.valueOf(context));
    }
}
