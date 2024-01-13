package com.juancho.demo.rest;

import com.juancho.demo.entity.Student;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentRestController {

    private List<Student> students;

    // @PostConstruct runs when the bean is constructed
    @PostConstruct
    public void loadData() {
        students = new ArrayList<>();
        students.add(new Student("Juan", "Lopez"));
        students.add(new Student("Pepe", "Perez"));
        students.add(new Student("Hernan", "Galindo"));
    }

    @GetMapping("/students")
    public List<Student> getStudents() {
        return students;
    }

    @GetMapping("/students/{studentId}")
    public Student getStudent(@PathVariable int studentId) {
        // check if student exists
        if (studentId >= students.size() || studentId < 0) {
            throw new StudenNotFoundException("Student id not found - " + studentId);
        }
        return students.get(studentId);
    }

}
