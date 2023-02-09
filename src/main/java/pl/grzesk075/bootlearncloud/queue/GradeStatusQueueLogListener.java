package pl.grzesk075.bootlearncloud.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pl.grzesk075.bootlearncloud.queue.message.GradeStatus;

@Component
public class GradeStatusQueueLogListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GradeStatusQueueLogListener.class);

    @JmsListener(destination = "grade_status_queue_out", containerFactory = "jmsFactory")
    public void onGradeStatus(GradeStatus gradeStatus) {
        Long gradeId = gradeStatus.getGrade() != null ? gradeStatus.getGrade().getId() : null;
        String logMessage = String.format("grade message processed: UUID=%s, saved Grade id=%d, error=%s",
                gradeStatus.getUuid(), gradeId, gradeStatus.getError());
        LOGGER.debug(logMessage);
    }
}
