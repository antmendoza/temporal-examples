package examples.interceptor;

import io.temporal.activity.Activity;

public class DBActivityImpl implements DBActivity {
    @Override
    public String recordSomething(String something) {



        return something;
    }
}
