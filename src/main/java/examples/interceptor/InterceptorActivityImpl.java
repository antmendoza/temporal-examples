package examples.interceptor;


import io.temporal.activity.Activity;

public class InterceptorActivityImpl implements InterceptorActivity {
    @Override
    public String doActivity(String action) {


        int attempt = Activity.getExecutionContext().getInfo().getAttempt();

        if(attempt < 2 && action.equals("inside workflow")){
            //throw new IllegalArgumentException("testing ");
        }



        System.out.println(action);


        return "doActivityResponse";
    }
}
