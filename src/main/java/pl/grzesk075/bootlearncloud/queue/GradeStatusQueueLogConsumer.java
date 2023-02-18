package pl.grzesk075.bootlearncloud.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pl.grzesk075.bootlearncloud.queue.message.GradeStatus;

import static pl.grzesk075.bootlearncloud.queue.Destination.GRADE_STATUS_QUEUE_OUT;

@Component
public class GradeStatusQueueLogConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GradeStatusQueueLogConsumer.class);
    public static final String SUBSCRIPTION_1 = "subscription1";
    public static final String SUBSCRIPTION_2 = "subscription2";

    @JmsListener(destination = GRADE_STATUS_QUEUE_OUT,
            subscription = SUBSCRIPTION_1,
            containerFactory = "jmsListenerContainerFactoryPublishSubscribe")
    public void onGradeStatus1(GradeStatus gradeStatus) {
        log(gradeStatus, SUBSCRIPTION_1);
    }

    @JmsListener(destination = GRADE_STATUS_QUEUE_OUT,
            subscription = SUBSCRIPTION_2,
            containerFactory = "jmsListenerContainerFactoryPublishSubscribe")
    public void onGradeStatus2(GradeStatus gradeStatus) {
        log(gradeStatus, SUBSCRIPTION_2);
    }

    private static void log(GradeStatus gradeStatus, String subscription) {
        Long gradeId = gradeStatus.getGrade() != null ? gradeStatus.getGrade().getId() : null;
        LOGGER.debug("Subscription {}: grade message processed: UUID={}, saved Grade id={}, error={}",
                subscription, gradeStatus.getUuid(), gradeId, gradeStatus.getError());
    }
}
