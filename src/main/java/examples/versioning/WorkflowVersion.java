package examples.versioning;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface WorkflowVersion {

    @WorkflowMethod
    void start(String name);

    @SignalMethod
    void signal();

    public static class WorkflowVersionImpl implements WorkflowVersion {
        private boolean signalArrived;

        @Override
        public void start(String name) {



            io.temporal.workflow.Workflow.await(()-> this.signalArrived);


//             long millisFromStart = Workflow.getInfo().getRunStartedTimestampMillis();
//             if(millisFromStart < 4000){
//                //Do something here
//
//                 //added this
//                 int version = Workflow.getVersion("addedNewMethod",
//                         Workflow.DEFAULT_VERSION, 1);
//
//
//                 if(version == Workflow.DEFAULT_VERSION){
//                     System.out.println("version");
//                 }
//
//             }

        }

        @Override
        public void signal() {
            this.signalArrived = true;
        }

    }


    @WorkflowInterface
    public  interface IWorkflow {


        @WorkflowMethod
        void start(String name);

        @SignalMethod
        void signal();
    }

}

