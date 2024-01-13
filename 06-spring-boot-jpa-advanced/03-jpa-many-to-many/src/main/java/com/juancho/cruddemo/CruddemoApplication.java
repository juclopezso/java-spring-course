package com.juancho.cruddemo;

import com.juancho.cruddemo.dao.AppDAO;
import com.juancho.cruddemo.entity.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CruddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CruddemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AppDAO appDAO) {
		return runner -> {

//			createInstructor(appDAO);
//			findInstructorById(appDAO);
//			findInstructorDetailById(appDAO);
//			deleteInstructorDetail(appDAO);
//			createInstructorWithCourses(appDAO);
//			findInstructorWithCourses(appDAO);
//			findCoursesByInstructor(appDAO);
//			findInstructorWithCoursesJoinFetch(appDAO);
//			updateInstructor(appDAO);
//			updateCourse(appDAO);
//			deleteInstructorById(appDAO);
//			deleteCourse(appDAO);
//			createCourseWithReviews(appDAO);
//			findCourseAndReviews(appDAO);
//			deleteCourseAndReviews(appDAO);
//			createCourseAndStudents(appDAO);
//			findCourseAndStudents(appDAO);
//			findStudentAndCourses(appDAO);
//			addMoreCoursesForStudent(appDAO);
//			deleteCourse(appDAO);
			deleteStudent(appDAO);

		};
	}

	private void deleteStudent(AppDAO appDAO) {
		int id = 1;
		System.out.println("Deleting student id " + id);
		appDAO.deleteStudentById(id);
		System.out.println("Done!");
	}

	private void addMoreCoursesForStudent(AppDAO appDAO) {
		int id = 2;
		Student student = appDAO.findStudentAndCoursesByStudentId(id);
		// add couses to the student
		student.addCourse(new Course("How to speed cube"));
		student.addCourse(new Course("Atari 2000 - Game dev"));
		// save the course
		System.out.println("Updating student " + student);
		System.out.println("Courses " + student.getCourses());
		appDAO.update(student);
	}

	private void findStudentAndCourses(AppDAO appDAO) {
		int id = 2;
		System.out.println("Finding student id " + id);
		Student student = appDAO.findStudentAndCoursesByStudentId(id);
		System.out.println("Student " + student);
		System.out.println("Courses " + student.getCourses());
	}

	private void findCourseAndStudents(AppDAO appDAO) {
		int id = 10;
		System.out.println("Finding course id " + id);
		Course course = appDAO.findCourseAndStudentsByCourseId(id);
		System.out.println("Course " + course);
		System.out.println("Student " + course.getStudents());
	}

	private void createCourseAndStudents(AppDAO appDAO) {
		// create courses
		Course courseOne = new Course("How to be poor");
		// create students
		Student studentOne = new Student("Pepe", "Perez", "pepe@gmail.com");
		Student studentTwo = new Student("Pedro", "Palo", "pedro@gmail.com");
		// add students to the course
		courseOne.addStudent(studentOne);
		courseOne.addStudent(studentTwo);
		// save course and students
		System.out.println("Saving course " + courseOne);
		System.out.println("associated students " + courseOne.getStudents());
		appDAO.save(courseOne); // cascade saves the students
	}

	private void deleteCourseAndReviews(AppDAO appDAO) {
		int id = 10;
		System.out.println("Deleting course id " + id);
		appDAO.deleteCourseById(id);
	}

	private void findCourseAndReviews(AppDAO appDAO) {
		int id = 10;
		System.out.println("Course with reviews id " + id);
		Course course = appDAO.findCourseWithReviewsByCourseId(id);
		System.out.println(course);
		System.out.println(course.getReviews());
	}

	private void createCourseWithReviews(AppDAO appDAO) {
		// create a course
		Course course = new Course("Pacman - How to score a million points");
		// create and add reviews
		course.add(new Review("Great course!"));
		course.add(new Review("Bad course!"));
		course.add(new Review("Good course, well done!"));
		// save the course - cacade.all do the job of saving course and reviews
		System.out.println("Saving course");
		System.out.println(course);
		System.out.println(course.getReviews());
		appDAO.save(course);
		System.out.println("Course saved!");
	}

	private void deleteCourse(AppDAO appDAO) {
		int id = 10;
		System.out.println("Deleting course " + id);
		appDAO.deleteCourseById(id);
		System.out.println("Done!");
	}

	private void updateCourse(AppDAO appDAO) {
		int id = 10;
		Course course = appDAO.findCouseById(id);
		// find course
		course.setTitle("Enjoy new things");
		appDAO.update(course);
		System.out.println("Course updated " + course);
	}

	private void updateInstructor(AppDAO appDAO) {
		int id = 1;
		Instructor instructor = appDAO.findInstructorById(id);
		// update data
		instructor.setLastName("TESTER");
		appDAO.update(instructor);
		System.out.println("Updated instructor " + instructor);
	}

	private void findInstructorWithCoursesJoinFetch(AppDAO appDAO) {
		int id = 1;
		System.out.println("Finding instructor " + id);
		Instructor instructor = appDAO.findInstructorByIdJoinFetch(id);
		System.out.println("Instructor " + instructor);
		System.out.println("Courses " + instructor.getCourses());
	}

	private void findCoursesByInstructor(AppDAO appDAO) {
		int id = 1;
		System.out.println("Finding instructor " + id);
		Instructor instructor = appDAO.findInstructorById(id);
		System.out.println("Instructor " + instructor);
		List<Course> courses = appDAO.findCoursesByInstructorId(id);
		instructor.setCourses(courses);
		System.out.println("associated courses " + instructor.getCourses());
	}

	private void findInstructorWithCourses(AppDAO appDAO) {
		int id = 1;
		System.out.println("Finding instructor " + id);
		Instructor instructor = appDAO.findInstructorById(id);
		System.out.println("Instructor " + instructor);
		System.out.println("associated courses " + instructor.getCourses());
	}

	private void createInstructorWithCourses(AppDAO appDAO) {
		Instructor instructor = new Instructor("Susan", "Pub", "susan@mail.com");
		InstructorDetail instructorDetail = new InstructorDetail("youtube.com/ukesusan", "Ukelele");
		// set the instructor detail
		instructor.setInstructorDetail(instructorDetail);
		// create courses
		Course courseOne = new Course("Air ukelele");
		Course courseTwo = new Course("Ping pong master class");
		// add courses to instructor
		instructor.add(courseOne);
		instructor.add(courseTwo);
		// save the instructor
		// will save instructor and the instructorDetail and courses
		System.out.println("Saving instructor...");
		System.out.println("Courses saved " + instructor.getCourses());
		appDAO.save(instructor);
		System.out.println("Done saving instructor");
	}

	private void deleteInstructorDetail(AppDAO appDAO) {
		int id = 3;
		System.out.println("Deleting InstructorDetail " + id);
		appDAO.deleteInstructorDetailById(id);
		System.out.println("Done!");
	}

	private void findInstructorDetailById(AppDAO appDAO) {
		int id = 2;
		InstructorDetail instructorDetail = appDAO.findInstructorDetailById(id);
		System.out.println("Instructor detail found");
		System.out.println(instructorDetail);
		System.out.println("Instructor: " + instructorDetail.getInstructor());
	}

	private void deleteInstructorById(AppDAO appDAO) {
		int id = 1;
		System.out.println("Deleting instructor " + id);
		appDAO.deleteInstructorById(id);
		System.out.println("Done!");
	}

	private void findInstructorById(AppDAO appDAO) {
		int id = 1;
		Instructor instructor = appDAO.findInstructorById(id);
		System.out.println("Instructor found");
		System.out.println(instructor);
	}

	private void createInstructor(AppDAO appDAO) {
		Instructor instructor = new Instructor("Pedro", "Lopez", "pedro@mail.com");
		InstructorDetail instructorDetail = new InstructorDetail("youtube.com/pedro", "Guitar");
		// set the instructor detail
		instructor.setInstructorDetail(instructorDetail);
		// save the instructor
		// will save instructor and the instructorDetail
		System.out.println("Saving instructor...");
		appDAO.save(instructor);
		System.out.println("Done saving instructor");
	}

}
