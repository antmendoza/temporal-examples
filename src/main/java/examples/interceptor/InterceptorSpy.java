package examples.interceptor;

public class InterceptorSpy {

    private int activityInvocation = 0;
    private int workflowInvocation = 0;

    public void incrementActivityInvocation(){
        this.activityInvocation++;
    }

    public int getActivityInvocation() {
        return activityInvocation;
    }


}
