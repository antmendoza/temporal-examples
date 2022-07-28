package examples.continueas;

public class ContinueAsConfiguration {
    private  long secondsToWait;
    private int throwApplicationFailureOnIteration;


    public ContinueAsConfiguration() {
        this(0, 0);
    }

    public ContinueAsConfiguration(long secondsToWait, int throwApplicationFailureOnIteration) {
        this.secondsToWait = secondsToWait;
        this.throwApplicationFailureOnIteration = throwApplicationFailureOnIteration;
    }

    public long secondsToWait() {
        return this.secondsToWait;
    }

    public boolean throwApplicationFailure(int iteration) {
        return this.throwApplicationFailureOnIteration == iteration;
    }
}
