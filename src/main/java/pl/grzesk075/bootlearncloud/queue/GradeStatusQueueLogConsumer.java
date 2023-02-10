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

    @JmsListener(destination = GRADE_STATUS_QUEUE_OUT, containerFactory = "jmsFactory")
    public void onGradeStatus(GradeStatus gradeStatus) {
        Long gradeId = gradeStatus.getGrade() != null ? gradeStatus.getGrade().getId() : null;
        LOGGER.debug("grade message processed: UUID={}, saved Grade id={}, error={}",
                gradeStatus.getUuid(), gradeId, gradeStatus.getError());
    }
}
