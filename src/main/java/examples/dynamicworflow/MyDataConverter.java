package examples.dynamicworflow;

import io.temporal.api.common.v1.Payload;
import io.temporal.api.common.v1.Payloads;
import io.temporal.common.converter.DataConverter;
import io.temporal.common.converter.DataConverterException;

import java.lang.reflect.Type;
import java.util.Optional;

public class MyDataConverter implements DataConverter {
    private DataConverter dataConverter;

    public MyDataConverter(final DataConverter dataConverter) {
        this.dataConverter = dataConverter;
    }

    @Override
    public <T> Optional<Payload> toPayload(final T value) {
        return this.dataConverter.toPayload(value);
    }

    @Override
    public <T> T fromPayload(final Payload payload, final Class<T> valueClass, final Type valueType) {
        return this.dataConverter.fromPayload(payload,valueClass,valueType);
    }

    @Override
    public Optional<Payloads> toPayloads(final Object... values) throws DataConverterException {
        return this.dataConverter.toPayloads(values);
    }

    @Override
    public <T> T fromPayloads(
            final int index, final Optional<Payloads> content, final Class<T> valueType, final Type valueGenericType
    ) throws DataConverterException {
        return this.dataConverter.fromPayloads(index,content,valueType,valueGenericType);
    }

}
