package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    @Autowired
    StudentDao studentDao;

    @Autowired
    @Qualifier("mathGrades")
    private MathGrade mathGrade;

    @Autowired
    @Qualifier("scienceGrades")
    private ScienceGrade scienceGrades;

    @Autowired
    @Qualifier("historyGrades")
    private HistoryGrade historyGrade;

    @Autowired
    private StudentGrades studentGrades;

    @Autowired
    private MathGradesDao mathGradesDao;

    @Autowired
    private ScienceGradesDao scienceGradesDao;

    @Autowired
    private HistoryGradesDao historyGradesDao;

    public void createStudent(String firstname, String lastname, String email) {
        CollegeStudent student = new CollegeStudent(firstname, lastname, email);
        student.setId(0);
        studentDao.save(student);
    }

    public boolean checkIfStudentNullById(int id) {
        Optional<CollegeStudent> student = studentDao.findById(id);
        if (student.isPresent()) {
            return true;
        }
        return false;
    }

    public void deleteStudent(int id) {
        if (checkIfStudentNullById(id)) {
            studentDao.deleteById(id);
            mathGradesDao.deleteByStudentId(id);
            scienceGradesDao.deleteByStudentId(id);
            historyGradesDao.deleteByStudentId(id);
        }
    }

    public Iterable<CollegeStudent> getGradebook() {
        Iterable<CollegeStudent> students = studentDao.findAll();
        return students;
    }


    public Boolean createGrade (double grade, int id, String gradeType) {
        if (!checkIfStudentNullById(id)) {
            return false;
        }
        if (grade >= 0 && grade <= 100) {
            if (gradeType.equals("math")) {
                mathGrade.setId(0);
                mathGrade.setGrade(grade);
                mathGrade.setStudentId(id);
                mathGradesDao.save(mathGrade);
                return true;
            } else if (gradeType.equals("science")) {
                scienceGrades.setId(0);
                scienceGrades.setGrade(grade);
                scienceGrades.setStudentId(id);
                scienceGradesDao.save(scienceGrades);
                return true;
            } else if (gradeType.equals("history")) {
                historyGrade.setId(0);
                historyGrade.setGrade(grade);
                historyGrade.setStudentId(id);
                historyGradesDao.save(historyGrade);
                return true;
            }
        }
        return false;
    }

    public int deleteGrade(int id, String gradeType) {
        int studenId = 0;
        if (gradeType == "math") {
            Optional<MathGrade> grade = mathGradesDao.findById(id);
            if(!grade.isPresent()) {
                return 0;
            }
            studenId = grade.get().getStudentId();
            mathGradesDao.deleteById(id);
        } else if (gradeType == "science") {
            Optional<ScienceGrade> grade = scienceGradesDao.findById(id);
            if(!grade.isPresent()) {
                return 0;
            }
            studenId = grade.get().getStudentId();
            scienceGradesDao.deleteById(id);
        } else if (gradeType == "history") {
            Optional<HistoryGrade> grade = historyGradesDao.findById(id);
            if(!grade.isPresent()) {
                return 0;
            }
            studenId = grade.get().getStudentId();
            historyGradesDao.deleteById(id);
        }
        return studenId;
    }

    public GradebookCollegeStudent studentInformation(int id) {
        if (!checkIfStudentNullById(id)) {
            return null;
        }
        Optional<CollegeStudent> student = studentDao.findById(id);
        Iterable<MathGrade> mathGrades = mathGradesDao.findGradeByStudentId(id);
        Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findGradeByStudentId(id);
        Iterable<HistoryGrade> historyGrades = historyGradesDao.findGradeByStudentId(id);
        // convert iterables to lists
        List<Grade> mathGradeList = new ArrayList<>();
        List<Grade> scienceGradeList = new ArrayList<>();
        List<Grade> historyGradeList = new ArrayList<>();
        // kind of lambda expression to iterate over the iterable and add the value to the list
        mathGrades.forEach(mathGradeList::add);
        scienceGrades.forEach(scienceGradeList::add);
        historyGrades.forEach(historyGradeList::add);

        studentGrades.setMathGradeResults(mathGradeList);
        studentGrades.setScienceGradeResults(scienceGradeList);
        studentGrades.setHistoryGradeResults(historyGradeList);

        CollegeStudent studentFetched = student.get();

        GradebookCollegeStudent gradebookCollegeStudent = new GradebookCollegeStudent(
                studentFetched.getId(), studentFetched.getFirstname(), studentFetched.getLastname(),
                studentFetched.getEmailAddress(), studentGrades);

        return gradebookCollegeStudent;
    }

    public void configureStudentInformationModel(int id, Model m) {
        GradebookCollegeStudent student = studentInformation(id);
        m.addAttribute("student", student);
        if (student.getStudentGrades().getMathGradeResults().size() > 0) {
            m.addAttribute("mathAverage", student.getStudentGrades().findGradePointAverage(
                    student.getStudentGrades().getMathGradeResults()
            ));
        } else {
            m.addAttribute("mathAverage", "N/A");
        }
        if (student.getStudentGrades().getScienceGradeResults().size() > 0) {
            m.addAttribute("scienceAverage", student.getStudentGrades().findGradePointAverage(
                    student.getStudentGrades().getScienceGradeResults()
            ));
        } else {
            m.addAttribute("scienceAverage", "N/A");
        }
        if (student.getStudentGrades().getHistoryGradeResults().size() > 0) {
            m.addAttribute("historyAverage", student.getStudentGrades().findGradePointAverage(
                    student.getStudentGrades().getHistoryGradeResults()
            ));
        } else {
            m.addAttribute("historyAverage", "N/A");
        }
    }

}
