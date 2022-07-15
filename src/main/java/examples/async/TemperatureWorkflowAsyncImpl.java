package examples.async;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class TemperatureWorkflowAsyncImpl implements TemperatureWorkflowAsync {

    private final ActivityOptions options = ActivityOptions.newBuilder()
            .setScheduleToCloseTimeout(Duration.ofSeconds(2))
            .build();

    private final TemperatureService temperatureService = Workflow.newActivityStub(TemperatureService.class, options);

    @Override
    public double start(List<String> cities) {

        final List<Promise<TemperatureResponse>> tmpPromises = cities.stream()
                .map(city -> Async.function(temperatureService::getTemperature, city))
                .collect(Collectors.toList());

        Promise.allOf(tmpPromises)
                .get();

        OptionalDouble tempAverage = tmpPromises.stream()
                .mapToInt(tmp -> tmp.get()
                        .getTemperature())
                .average();


        return tempAverage.getAsDouble();

    }

}
