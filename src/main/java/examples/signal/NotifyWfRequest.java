package examples.signal;

import java.util.Objects;

public class NotifyWfRequest {

    private  String data;


    public NotifyWfRequest() {
    }

    public NotifyWfRequest(final String data) {this.data = data;}


    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NotifyWfRequest that = (NotifyWfRequest) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return "NotifyWfRequest{" + "data='" + data + '\'' + '}';
    }

}
