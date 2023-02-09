package pl.grzesk075.bootlearncloud.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import pl.grzesk075.bootlearncloud.model.Grade;
import pl.grzesk075.bootlearncloud.model.Student;
import pl.grzesk075.bootlearncloud.queue.message.GradeMessage;
import pl.grzesk075.bootlearncloud.queue.message.GradeStatus;
import pl.grzesk075.bootlearncloud.repository.GradeRepository;
import pl.grzesk075.bootlearncloud.repository.StudentRepository;

@Component
public class GradeMessageQueueListener {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @JmsListener(destination = "grade_message_queue_in", containerFactory = "jmsFactory")
    @SendTo("grade_status_queue_out")
    public GradeStatus onGrade(GradeMessage gradeMessage) {
        Grade grade = gradeMessage.getGrade();

        try {
            Student student = studentRepository.findById(gradeMessage.getStudentId()).orElseThrow();
            grade.setId(null);
            grade.setStudent(student);
            gradeRepository.save(grade);
            return GradeStatus.builder()
                    .uuid(gradeMessage.getUuid())
                    .grade(grade)
                    .build();
        } catch (RuntimeException e) {
            return GradeStatus.builder()
                    .uuid(gradeMessage.getUuid())
                    .error(e.toString())
                    .build();
        }
    }
}
