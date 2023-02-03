package pl.grzesk075.bootlearncloud.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.grzesk075.bootlearncloud.model.Grade;
import pl.grzesk075.bootlearncloud.model.Subject;

import java.util.List;

@Repository
public interface GradeRepository extends CrudRepository<Grade, Long> {

    List<Grade> findBySubject(Subject subject);

    @Query("SELECT g FROM Grade g WHERE g.subject = :subject AND g.student.lastName = :studentLastName")
    List<Grade> findBySubjectAndStudentLastName(Subject subject, String studentLastName);
}
