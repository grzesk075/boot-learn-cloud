package pl.grzesk075.bootlearncloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.grzesk075.bootlearncloud.model.Grade;
import pl.grzesk075.bootlearncloud.model.Student;
import pl.grzesk075.bootlearncloud.model.Subject;
import pl.grzesk075.bootlearncloud.repository.GradeRepository;
import pl.grzesk075.bootlearncloud.repository.StudentRepository;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @PostMapping("/addStudent")
    public Long addStudent(@RequestBody Student student) {
        student.setId(null);
        studentRepository.save(student);
        return student.getId();
    }

    @PostMapping("/addGrade/{studentId:[0-9]+}")
    public Long addGrade(@RequestBody Grade grade, @PathVariable Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        grade.setId(null);
        grade.setStudent(student);
        gradeRepository.save(grade);
        return grade.getId();
    }

    @GetMapping("/getStudent")
    public Student getStudent(@RequestParam Long studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    @GetMapping("/getGrades")
    public List<Grade> getGrades(@RequestParam(required = false) String studentLastNameFilter,
                                 @RequestParam(required = false) Subject subjectFilter) {

        return gradeRepository.findBySubjectAndStudentLastName(subjectFilter, studentLastNameFilter);
    }
}