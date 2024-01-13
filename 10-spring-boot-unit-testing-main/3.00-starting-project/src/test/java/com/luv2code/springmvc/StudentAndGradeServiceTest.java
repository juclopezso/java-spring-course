package com.luv2code.springmvc;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class StudentAndGradeServiceTest {

    @Autowired
    StudentAndGradeService studentService;

    @Autowired
    StudentDao studentDao;

    @Autowired
    MathGradesDao mathGradesDao;

    @Autowired
    HistoryGradesDao historyGradesDao;

    @Autowired
    ScienceGradesDao scienceGradesDao;

    @Autowired
    private JdbcTemplate jdbc;

    @Value("${sql.scripts.create.student}")
    private String sqlAddStudent;

    @Value("${sql.scripts.create.math.grade}")
    private String sqlAddMathGrade;

    @Value("${sql.scripts.create.science.grade}")
    private String sqlAddScienceGrade;

    @Value("${sql.scripts.create.history.grade}")
    private String sqlAddHistoryGrade;

    @Value("${sql.scripts.delete.student}")
    private String sqlDeleteStudent;

    @Value("${sql.scripts.delete.math.grade}")
    private String sqlDeleteMathGrade;

    @Value("${sql.scripts.delete.science.grade}")
    private String sqlDeleteScienceGrade;

    @Value("${sql.scripts.delete.history.grade}")
    private String sqlDeleteHistoryGrade;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddStudent);
        jdbc.execute(sqlAddMathGrade);
        jdbc.execute(sqlAddScienceGrade);
        jdbc.execute(sqlAddHistoryGrade);
    }

    @AfterEach
    public void cleanDatabase() {
        jdbc.execute(sqlDeleteStudent);
        jdbc.execute(sqlDeleteMathGrade);
        jdbc.execute(sqlDeleteScienceGrade);
        jdbc.execute(sqlDeleteHistoryGrade);
    }

    @Test
    public void createStudentService() {
        studentService.createStudent("Chad", "Perez", "chad@gmail.com");
        CollegeStudent student = studentDao.findByEmailAddress("chad@gmail.com");
        assertEquals("chad@gmail.com", student.getEmailAddress(), "Find by email");
    }

    @Test
    public void isStudentNullCheck() {
        assertTrue(studentService.checkIfStudentNullById(1));
        assertFalse(studentService.checkIfStudentNullById(0));
    }

    @Test
    public void deleteStudentService() {
        Optional<CollegeStudent> student = studentDao.findById(1);
        Optional<MathGrade> mathGrade = mathGradesDao.findById(1);
        Optional<ScienceGrade> scienceGrade = scienceGradesDao.findById(1);
        Optional<HistoryGrade> historyGrade = historyGradesDao.findById(1);
        // check values exists
        assertTrue(student.isPresent(), "Should Return true");
        assertTrue(mathGrade.isPresent(), "Should Return true");
        assertTrue(scienceGrade.isPresent(), "Should Return true");
        assertTrue(historyGrade.isPresent(), "Should Return true");
        // deletions
        studentService.deleteStudent(1);
        // asserts values don't exists
        student = studentDao.findById(1);
        mathGrade = mathGradesDao.findById(1);
        scienceGrade = scienceGradesDao.findById(1);
        historyGrade = historyGradesDao.findById(1);

        assertFalse(student.isPresent(), "Should Return false");
        assertFalse(mathGrade.isPresent(), "Should Return false");
        assertFalse(scienceGrade.isPresent(), "Should Return false");
        assertFalse(historyGrade.isPresent(), "Should Return false");
    }

    // @Sql executes sql script before the given test method
    // the @BeforeEach executes first
    @Sql("/insert-data.sql")
    @Test
    public void getGradeBookService() {
        Iterable<CollegeStudent> iterableStudents = studentService.getGradebook();
        List<CollegeStudent> collegeStudentList = new ArrayList<>();
        for (CollegeStudent student : iterableStudents) {
            collegeStudentList.add(student);
        }
        // test the size of the list
        assertEquals(5, collegeStudentList.size());
    }

    @Test
    public void createGradeService() {
        // create the grade
        assertTrue(studentService.createGrade(80.50, 1, "math"));
        assertTrue(studentService.createGrade(80.50, 1, "science"));
        assertTrue(studentService.createGrade(80.50, 1, "history"));
        // get all grades with studentId
        Iterable<MathGrade> mathGrades = mathGradesDao.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findGradeByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradesDao.findGradeByStudentId(1);
        // verify there is grades
        assertTrue(((Collection<MathGrade>) mathGrades).size() == 2, "Student has 2 math grades");
        assertTrue(((Collection<ScienceGrade>) scienceGrades).size() == 2, "Student has 2 science grades");
        assertTrue(((Collection<HistoryGrade>) historyGrades).size() == 2, "Student has 2 history grades");
    }

    @Test
    public void createGradeServiceReturnFalse() {
        // edge cases
        assertFalse(studentService.createGrade(105, 1, "math"));
        assertFalse(studentService.createGrade(-10, 1, "math"));
        assertFalse(studentService.createGrade(10, 0, "math"));
        assertFalse(studentService.createGrade(10, 1, "chemistry"));
    }

    @Test
    public void deleteGradeService() {
        assertEquals(1, studentService.deleteGrade(1, "math"));
        assertEquals(1, studentService.deleteGrade(1, "science"));
        assertEquals(1, studentService.deleteGrade(1, "history"));
    }

    @Test
    public void deleteGradeServiceEdgeCases() {
        assertEquals(0, studentService.deleteGrade(0, "science"),
                "No student should have 0 id");
        assertEquals(1, studentService.deleteGrade(1, "science"),
                "No student should have a literature class");
    }

    @Test
    public void studentInformation() {
        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(1);
        assertNotNull(gradebookCollegeStudent);
        assertEquals(1, gradebookCollegeStudent.getId());
        assertEquals("Eric", gradebookCollegeStudent.getFirstname());
        assertEquals("Perez", gradebookCollegeStudent.getLastname());
        assertEquals("eric@mail.com", gradebookCollegeStudent.getEmailAddress());
        assertTrue(gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size() == 1);
    }

    @Test
    public void studentInformationReturnNull() {
        GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(0);
        assertNull(gradebookCollegeStudent);
    }

}
