package pl.grzesk075.bootlearncloud.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import pl.grzesk075.bootlearncloud.model.Grade;
import pl.grzesk075.bootlearncloud.model.Student;
import pl.grzesk075.bootlearncloud.queue.message.GradeMessage;
import pl.grzesk075.bootlearncloud.queue.message.GradeStatus;
import pl.grzesk075.bootlearncloud.repository.GradeRepository;
import pl.grzesk075.bootlearncloud.repository.StudentRepository;

import static pl.grzesk075.bootlearncloud.queue.Destination.GRADE_MESSAGE_QUEUE_IN;
import static pl.grzesk075.bootlearncloud.queue.Destination.GRADE_STATUS_QUEUE_OUT;

@Component
public class GradeMessageQueueConsumer {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private JmsTemplate jmsTemplatePubSub;

    @JmsListener(destination = GRADE_MESSAGE_QUEUE_IN,
            containerFactory = "jmsListenerContainerFactoryPointToPoint")
    //@SendTo(GRADE_STATUS_QUEUE_OUT) point to point queue sending solution
    public void onGrade(GradeMessage gradeMessage) {
        Grade grade = gradeMessage.getGrade();
        GradeStatus gradeStatus;
        try {
            Student student = studentRepository.findById(gradeMessage.getStudentId()).orElseThrow();
            grade.setId(null);
            grade.setStudent(student);
            gradeRepository.save(grade);
            gradeStatus = GradeStatus.builder()
                    .uuid(gradeMessage.getUuid())
                    .grade(grade)
                    .build();
        } catch (RuntimeException e) {
            gradeStatus = GradeStatus.builder()
                    .uuid(gradeMessage.getUuid())
                    .error(e.toString())
                    .build();
        }
        jmsTemplatePubSub.convertAndSend(GRADE_STATUS_QUEUE_OUT, gradeStatus);
    }
}
