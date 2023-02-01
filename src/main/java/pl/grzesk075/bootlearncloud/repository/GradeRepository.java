package pl.grzesk075.bootlearncloud.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.grzesk075.bootlearncloud.model.Grade;

@Repository
public interface GradeRepository extends CrudRepository<Grade, Long> {
}
