package pl.grzesk075.bootlearncloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.grzesk075.bootlearncloud.model.Grade;
import pl.grzesk075.bootlearncloud.model.Student;
import pl.grzesk075.bootlearncloud.repository.StudentRepository;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/addStudent")
    public Long addStudent(@RequestBody Student student) {
        student.setId(null);
        studentRepository.save(student);
        return student.getId();
    }

    @PostMapping("/addGrade/{studentId:[0-9]+}")
    public Long addGrade(@RequestBody Grade grade, @PathVariable Long studentId) {

        return grade.getId();
    }

    @GetMapping("/getStudent")
    public Student getStudent(@RequestParam Long studentId) {
        return null;
    }
}