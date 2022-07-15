package examples.async;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface TemperatureService {

    @ActivityMethod
    TemperatureResponse getTemperature(String city);

}
