package pl.grzesk075.bootlearncloud.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import pl.grzesk075.bootlearncloud.queue.message.GradeMessage;

import static pl.grzesk075.bootlearncloud.queue.Destination.GRADE_MESSAGE_QUEUE_IN;

@Service
public class GradeMessageQueueProducer {

    @Autowired
    private JmsTemplate jmsTemplatePointToPoint;

    public void send(GradeMessage gradeMessage) {
        jmsTemplatePointToPoint.convertAndSend(GRADE_MESSAGE_QUEUE_IN, gradeMessage);
    }
}
