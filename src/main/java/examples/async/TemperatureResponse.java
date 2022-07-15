package examples.async;

public class TemperatureResponse {
    private String city;
    private int temperature;

    public TemperatureResponse() {
    }

    public TemperatureResponse(final String city, final int temperature) {
        this.city = city;
        this.temperature = temperature;
    }


    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(final int temperature) {
        this.temperature = temperature;
    }

}
