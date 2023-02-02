package pl.grzesk075.bootlearncloud.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = "grades")
@ToString(exclude = "grades", callSuper = true)
public class Student extends Person {

    @JsonManagedReference
    @OneToMany(mappedBy = "student")
    private List<Grade> grades;
}
