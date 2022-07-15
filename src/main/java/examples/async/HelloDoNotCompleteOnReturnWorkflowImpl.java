package examples.async;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import io.temporal.activity.ActivityOptions;
import io.temporal.client.ActivityCompletionClient;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.concurrent.ForkJoinPool;

public class HelloDoNotCompleteOnReturnWorkflowImpl implements HelloDoNotCompleteOnReturnWorkflow {


    private final ActivityOptions options = ActivityOptions.newBuilder()
            .setScheduleToCloseTimeout(Duration.ofSeconds(2))
            .build();

    private final HelloService helloService = Workflow.newActivityStub(HelloService.class, options);


    @Override
    public String execute(final String name) {

        Promise<String> promiseHelloService = Async.function(helloService::requestHello, name);

        return promiseHelloService.get();

        //This will also block the execution until the task in completed by completeHello
        //return helloService.requestHello(name);

    }


    @ActivityInterface
    public interface HelloService {

        @ActivityMethod
        String requestHello(String name);

    }

    public static class HelloServiceImpl implements HelloService {


        private final ActivityCompletionClient completionClient;

        public HelloServiceImpl(
                final ActivityCompletionClient completionClient
        ) {this.completionClient = completionClient;}


        @Override
        public String requestHello(final String name) {


            byte[] taskToken = Activity.getExecutionContext()
                    .getTaskToken();

            ForkJoinPool.commonPool()
                    .execute(() -> completeHello(taskToken, name));

            Activity.getExecutionContext()
                    .doNotCompleteOnReturn();
            return "";
        }

        private void completeHello(byte[] token, String name) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            completionClient.complete(token, "Hello " + name);
        }


    }


}
