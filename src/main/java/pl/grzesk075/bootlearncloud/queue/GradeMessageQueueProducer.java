package pl.grzesk075.bootlearncloud.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import pl.grzesk075.bootlearncloud.queue.message.GradeMessage;

import static pl.grzesk075.bootlearncloud.queue.Destination.GRADE_MESSAGE_QUEUE_IN;

/**
 * Point To Point
 */
@Service
public class GradeMessageQueueProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(GradeMessage gradeMessage) {
        jmsTemplate.convertAndSend(GRADE_MESSAGE_QUEUE_IN, gradeMessage);
    }
}
