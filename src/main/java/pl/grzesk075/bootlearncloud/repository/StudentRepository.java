package pl.grzesk075.bootlearncloud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.grzesk075.bootlearncloud.model.Student;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
}
