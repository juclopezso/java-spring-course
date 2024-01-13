package com.juancho.cruddemo.dao;

import com.juancho.cruddemo.entity.Course;
import com.juancho.cruddemo.entity.Instructor;
import com.juancho.cruddemo.entity.InstructorDetail;
import com.juancho.cruddemo.entity.Student;

import java.util.List;

public interface AppDAO {

    void save(Instructor instructor);

    Instructor findInstructorById(int id);

    void deleteInstructorById(int id);

    InstructorDetail findInstructorDetailById(int id);

    void deleteInstructorDetailById(int id);

    List<Course> findCoursesByInstructorId(int id);

    Instructor findInstructorByIdJoinFetch(int id);

    void update(Instructor instructor);

    void update(Course course);

    Course findCouseById(int id);

    void deleteCourseById(int id);

    void save(Course course);

    Course findCourseWithReviewsByCourseId(int id);

    Course findCourseAndStudentsByCourseId(int id);

    Student findStudentAndCoursesByStudentId(int id);

    void update(Student student);

    void deleteStudentById(int id);

}
