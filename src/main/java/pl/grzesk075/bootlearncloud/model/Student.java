package pl.grzesk075.bootlearncloud.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "grades")
@ToString(exclude = "grades", callSuper = true)
public class Student extends Person {

    @Builder
    public Student(String firstName, String lastName) {
        super(null, firstName, lastName);
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "student")
    private List<Grade> grades;
}
