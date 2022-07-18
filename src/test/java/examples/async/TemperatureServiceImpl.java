package examples.async;

import java.util.Random;

public class TemperatureServiceImpl implements TemperatureService {

    @Override
    public TemperatureResponse getTemperature(String city) {

        int timeSleep = new Random().ints(100, 200)
                .findFirst()
                .getAsInt();

        sleep(timeSleep);


        switch (city){
            case "BCN":
                return new TemperatureResponse(city, 20);
            case "MUR":
                return new TemperatureResponse(city, 30);
            default:
                return new TemperatureResponse(city, 25);

        }

    }

    private void sleep(final int timeSleep) {
        try {
            Thread.sleep(timeSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
