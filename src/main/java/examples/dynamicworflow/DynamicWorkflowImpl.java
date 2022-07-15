package examples.dynamicworflow;

import io.temporal.activity.*;
import io.temporal.common.converter.EncodedValues;
import io.temporal.workflow.ActivityStub;
import io.temporal.workflow.DynamicWorkflow;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class DynamicWorkflowImpl implements DynamicWorkflow {


    private ActivityStub activity;

    @Override
    public Object execute(final EncodedValues args) {

        final String action = ACTION.COUNT_WORDS.toString();
        final String input1 = args.get(0, String.class);
        if (input1.equals(action)) {

            final ActivityOptions activityOptions = ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(4))
                    .build();
            activity = Workflow.newUntypedActivityStub(activityOptions);

            final String wordToCount = args.get(1, String.class);
            Integer numOfWords = activity.execute(action,
                    Integer.class, wordToCount);

            return new DynamicWorkflowResponse(String.valueOf(numOfWords));

        }


        return new DynamicWorkflowResponse(input1);
    }

    public enum ACTION {
        COUNT_WORDS
    }

    public static class CountWordsActivityImpl implements DynamicActivity {
        private final Logger log = LoggerFactory.getLogger(CountWordsActivityImpl.class);


        @Override
        public Object execute(final EncodedValues args) {

            ActivityExecutionContext ctx = Activity.getExecutionContext();
            ActivityInfo info = ctx.getInfo();

            log.info("namespace= " +  info.getActivityNamespace());
            log.info("workflowId= " + info.getWorkflowId());
            log.info("runId= " + info.getRunId());
            log.info("activityId= " + info.getActivityId());
            log.info("startToCloseTimeout= " + info.getStartToCloseTimeout().getSeconds());

            return args.get(0, String.class)
                    .length();

        }

    }

}


