package pl.grzesk075.bootlearncloud.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Enumerated(EnumType.STRING)
    private Subject subject;

//    @Enumerated(EnumType.STRING)
//    private Value value;
//
//    public enum Value {
//        A,
//        B,
//        C,
//        D,
//        E;
//    }
}
