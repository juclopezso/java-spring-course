package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.dao.ApplicationDao;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import com.luv2code.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class MockAnnotationTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent studentOne;

    @Autowired
    StudentGrades studentGrades;

    // @Mock
    @MockBean
    private ApplicationDao applicationDao;

    // InjectMocks
    // injects @Mock annotated instances to the service
    @Autowired
    private ApplicationService applicationService;

    @BeforeEach
    public void beforeEach() {
        studentOne.setFirstname("Pepe");
        studentOne.setLastname("Perez");
        studentOne.setEmailAddress("pepe@mail.com");
        studentOne.setStudentGrades(studentGrades);
    }

    @Test
    @DisplayName("When and Verify")
    public void assertEqualsTestAddGrades() {
        // setup
        when(applicationDao.addGradeResultsForSingleClass(
                studentGrades.getMathGradeResults())).thenReturn(100.0);

        // assertions
        assertEquals(100.0, applicationService.addGradeResultsForSingleClass(
                studentOne.getStudentGrades().getMathGradeResults()));

        // verify method is called
        verify(applicationDao).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
        // verify the method was called only once
        verify(applicationDao, times(1)).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
    }

    @Test
    @DisplayName("Find GPA")
    public void assertEqualsFindGpa() {
        when(applicationDao.findGradePointAverage(studentGrades.getMathGradeResults()))
                .thenReturn(88.41);

        assertEquals(88.41, applicationService.findGradePointAverage(
                studentOne.getStudentGrades().getMathGradeResults()));

        verify(applicationDao).findGradePointAverage(studentGrades.getMathGradeResults());
    }

    @Test
    @DisplayName("Not null")
    public void testAssertNotNull() {
        when(applicationDao.checkNull(studentGrades.getMathGradeResults()))
                .thenReturn(true);
        assertNotNull(applicationService.checkNull(studentGrades.getMathGradeResults()));
    }

    @Test
    @DisplayName("Throw runtime error")
    public void throwRuntimeError() {
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");

        doThrow(new RuntimeException()).when(applicationDao).checkNull(nullStudent);

        assertThrows(RuntimeException.class, () -> {
            applicationService.checkNull(nullStudent);
        });
        verify(applicationDao, times(1)).checkNull(nullStudent);
    }

    @Test
    @DisplayName("Multiple stubbing")
    public void stubbingConsecutiveCalls() {
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");

        String expected = "Do not throw exception second time";
        // first call throws exception
        // consecutive calls do NOT throw exception, just return a string
        when(applicationDao.checkNull(nullStudent))
                .thenThrow(new RuntimeException())
                .thenReturn(expected);

        // first call
        assertThrows(RuntimeException.class, () -> {
            applicationService.checkNull(nullStudent);
        });
        // second call
        assertEquals(expected, applicationService.checkNull(nullStudent));
        // verify method was called 2 times
        verify(applicationDao, times(2)).checkNull(nullStudent);
    }

}
