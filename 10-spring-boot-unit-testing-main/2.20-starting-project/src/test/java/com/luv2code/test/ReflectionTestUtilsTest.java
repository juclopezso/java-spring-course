package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class ReflectionTestUtilsTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent student;

    @Autowired
    StudentGrades studentGrades;

    @BeforeEach
    public void beforeEach() {
        student.setFirstname("Pepe");
        student.setLastname("Perez");
        student.setEmailAddress("pepe@mail.com");
        student.setStudentGrades(studentGrades);
        // set private fields using reflection
        ReflectionTestUtils.setField(student, "id", 1);
        ReflectionTestUtils.setField(student, "studentGrades",
                new StudentGrades(new ArrayList<>(Arrays.asList(
                        100.0, 85.0, 76.50, 91.75))));
    }

    @Test
    @DisplayName("Get data from private field")
    public void getPrivateField() {
        // we can delete setters and getters and can still access those fields
        assertEquals(1, ReflectionTestUtils.getField(student, "id"));
    }

    @Test
    @DisplayName("Calling private method")
    public void invokePrivateMethod() {
        assertEquals("Pepe 1",
                ReflectionTestUtils.invokeMethod(student, "getFirstNameAndId"));
    }
}
