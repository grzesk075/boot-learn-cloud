package pl.grzesk075.bootlearncloud.controller;

import org.springframework.web.bind.annotation.*;
import pl.grzesk075.bootlearncloud.model.Grade;
import pl.grzesk075.bootlearncloud.model.Student;

@RestController("/student")
public class StudentController {

    @PostMapping("/addStudent")
    public Long addStudent(@RequestBody Student student) {
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