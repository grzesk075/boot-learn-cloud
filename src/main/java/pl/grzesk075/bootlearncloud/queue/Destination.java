package pl.grzesk075.bootlearncloud.queue;

public abstract class Destination {
    public static final String GRADE_MESSAGE_QUEUE_IN = "grade_message_queue_in"; //point to point destination
    public static final String GRADE_STATUS_QUEUE_OUT = "grade_status_queue_out"; //topic name for publish subscribe

    private Destination() {
    }
}
