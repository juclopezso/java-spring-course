package com.luv2code.springmvc;

import static org.hamcrest.Matchers.is;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.match.ContentRequestMatchers;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.awt.*;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class GradebookControllerTest {

    private static MockHttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    @Mock
    StudentAndGradeService studentAndGradeServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private CollegeStudent student;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MathGradesDao mathGradeDao;

    @Autowired
    private ScienceGradesDao scienceGradeDao;

    @Autowired
    private HistoryGradesDao historyGradeDao;

    @Autowired
    private StudentAndGradeService studentService;

    @Value("${sql.script.create.student}")
    private String sqlAddStudent;

    @Value("${sql.script.create.math.grade}")
    private String sqlAddMathGrade;

    @Value("${sql.script.create.science.grade}")
    private String sqlAddScienceGrade;

    @Value("${sql.script.create.history.grade}")
    private String sqlAddHistoryGrade;

    @Value("${sql.script.delete.student}")
    private String sqlDeleteStudent;

    @Value("${sql.script.delete.math.grade}")
    private String sqlDeleteMathGrade;

    @Value("${sql.script.delete.science.grade}")
    private String sqlDeleteScienceGrade;

    @Value("${sql.script.delete.history.grade}")
    private String sqlDeleteHistoryGrade;

    public static final MediaType APPLICATION_JSON = MediaType.APPLICATION_JSON;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddStudent);
        jdbc.execute(sqlAddMathGrade);
        jdbc.execute(sqlAddScienceGrade);
        jdbc.execute(sqlAddHistoryGrade);
    }

    @BeforeAll
    public static void setup() {
        request = new MockHttpServletRequest();
        request.setParameter("firstname", "Chad");
        request.setParameter("lastname", "Darby");
        request.setParameter("emailAddress", "chad@mail.com");
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteStudent);
        jdbc.execute(sqlDeleteMathGrade);
        jdbc.execute(sqlDeleteScienceGrade);
        jdbc.execute(sqlDeleteHistoryGrade);
    }

    @Test
    public void getStudentsHttpRequest () throws Exception {
        // add student in db
        student.setFirstname("Pepe");
        student.setLastname("Perez");
        student.setEmailAddress("pepe@gmail.com");
        entityManager.persist(student);
        entityManager.flush();
        // test get request
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                // "$" -> root element
                // we have 2 students in db
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void createStudentHttpRequest() throws Exception {
        student.setFirstname("Pepe");
        student.setLastname("Perez");
        student.setEmailAddress("pepe@gmail.com");
        // test post request
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                // generates Json value using a java object
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        CollegeStudent verifyStudent = studentDao.findByEmailAddress(student.getEmailAddress());
        assertNotNull(verifyStudent, "Student should be found");
    }

    @Test
    public void deleteStudentHttpRequest() throws Exception {
        // check student exists
        assertTrue(studentDao.findById(1).isPresent());
        // assert deletion of student
        mockMvc.perform(MockMvcRequestBuilders.delete("/student/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        assertFalse(studentDao.findById(1).isPresent());
    }

    @Test
    public void deleteStudentDoesNotExistsHttpRequest() throws Exception {
        assertFalse(studentDao.findById(0).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/{id}", 0))
                .andExpect(status().is4xxClientError())
                // import static org.hamcrest.Matchers.is;
                .andExpect(jsonPath("$.status", Matchers.is(404)))
                .andExpect(jsonPath("$.message", Matchers.is("Student or Grade was not found")));
    }

    @Test
    public void studentInformationHttpRequest() throws Exception {
        Optional<CollegeStudent> student = studentDao.findById(1);
        assertTrue(student.isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(student.get().getId())))
                .andExpect(jsonPath("$.firstname", Matchers.is(student.get().getFirstname())))
                .andExpect(jsonPath("$.lastname", Matchers.is(student.get().getLastname())))
                .andExpect(jsonPath("$.emailAddress", Matchers.is(student.get().getEmailAddress())));
    }

    @Test
    public void studentInformationDoesNotExistsHttpRequest() throws Exception {
        Optional<CollegeStudent> student = studentDao.findById(0);
        assertFalse(student.isPresent());
        mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Matchers.is(404)))
                .andExpect(jsonPath("$.message", Matchers.is("Student or Grade was not found")));
    }

    @Test
    public void createGradesHttpRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/grades")
                        .contentType(APPLICATION_JSON)
                        .param("grade", "80.00")
                        .param("gradeType", "math")
                        .param("studentId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.studentGrades.mathGradeResults", hasSize(2)));
    }

    @Test
    public void createGradesStudentDoesNotExistsHttpRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/grades")
                        .contentType(APPLICATION_JSON)
                        .param("grade", "80.00")
                        .param("gradeType", "math")
                        .param("studentId", "0"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Matchers.is(404)))
                .andExpect(jsonPath("$.message", Matchers.is("Student or Grade was not found")));
    }

    @Test
    public void createGradesInvalidSubjectHttpRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/grades")
                        .contentType(APPLICATION_JSON)
                        .param("grade", "80.00")
                        .param("gradeType", "literare")
                        .param("studentId", "1"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Matchers.is(404)))
                .andExpect(jsonPath("$.message", Matchers.is("Student or Grade was not found")));
    }

    @Test
    public void deleteGradesHttpRequest() throws Exception {
        Optional<CollegeStudent> student = studentDao.findById(1);
        Optional<MathGrade> grade = mathGradeDao.findById(1);
        assertTrue(grade.isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete("/grades/{id}/{gradeType}", 1, "math"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(student.get().getId())))
                .andExpect(jsonPath("$.firstname", Matchers.is(student.get().getFirstname())))
                .andExpect(jsonPath("$.lastname", Matchers.is(student.get().getLastname())))
                .andExpect(jsonPath("$.emailAddress", Matchers.is(student.get().getEmailAddress())))
                .andExpect(jsonPath("$.studentGrades.mathGradeResults", hasSize(0)));
    }

    @Test
    public void deleteGradeDoesNotExistsHttpRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/grades/{id}/{gradeType}", 2, "history"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Matchers.is(404)))
                .andExpect(jsonPath("$.message", Matchers.is("Student or Grade was not found")));
    }

    @Test
    public void deleteGradeInvalidGradeTypeHttpRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/grades/{id}/{gradeType}", 1, "literatura"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Matchers.is(404)))
                .andExpect(jsonPath("$.message", Matchers.is("Student or Grade was not found")));
    }

}
