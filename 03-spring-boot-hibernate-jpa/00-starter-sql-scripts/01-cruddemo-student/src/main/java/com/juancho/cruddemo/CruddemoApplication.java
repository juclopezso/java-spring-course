package com.juancho.cruddemo;

import com.juancho.cruddemo.dao.StudentDAO;
import com.juancho.cruddemo.entity.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CruddemoApplication {

	public static void main(String[] args) { SpringApplication.run(CruddemoApplication.class, args); }

	// injecting the DAO
	@Bean
	public CommandLineRunner commandLineRunner(StudentDAO studentDAO) {
		// executed after the spring beans have been loaded
		// java lambda expression
		return runner -> {
//			createStudent(studentDAO);
			createMultipleStudents(studentDAO);
			readStudent(studentDAO);
			queryForStudents(studentDAO);
			queryForStudentsByLastName(studentDAO);
//			updateStudent(studentDAO);
//			deleteStudent(studentDAO);
//			deleteAllStudents(studentDAO);
		};
	}

	private void deleteAllStudents(StudentDAO studentDAO) {
		int rowsDeleted = studentDAO.deleteAll();
		System.out.println("Students deleted: " + rowsDeleted);
	}

	private void deleteStudent(StudentDAO studentDAO) {
		int studentId = 1;
		System.out.println("Deleting student ID: 1");
		studentDAO.delete(studentId);
	}

	private void updateStudent(StudentDAO studentDAO) {
		// retrieve student
		int studentId = 1;
		Student student = studentDAO.findById(studentId);
		// change first name to 'scooby'
		student.setFirstName("Scooby");
		// update the student
		studentDAO.update(student);
		// display updated student
		System.out.println("Updated student: " + student);
	}

	private void queryForStudentsByLastName(StudentDAO studentDAO) {
		// get list of students
		List<Student> students = studentDAO.findByLastName("Lopez");
		// display students
		System.out.println("Students with lastname 'Lopez'");
		for (Student student : students) {
			System.out.println(student);
		}
	}

	private void queryForStudents(StudentDAO studentDAO) {
		// get list of students
		List<Student> students = studentDAO.findAll();
		// display list of students
		System.out.println("Students found ordered by lastName desc:");
		for (Student student : students) {
			System.out.println(student);
		}
	}

	private void readStudent(StudentDAO studentDAO) {
		// retrieve student id 1
		int id = 1;
		Student myStudent = studentDAO.findById(id);
		// display student
		System.out.println("My student id: " + id);
		System.out.println(myStudent);
	}

	private void createStudent(StudentDAO studentDAO) {
		// create the student object
		Student tempStudent = new Student("Juan", "Lopez", "juan@gmail.com");

		// save the student object
		studentDAO.save(tempStudent);

		// display the id of the saved student
		System.out.println("Student created ID: " + tempStudent.getId());
	}

	private void createMultipleStudents(StudentDAO studentDAO) {
		// create the student object
		Student tempStudent1 = new Student("Juan", "Alero", "juan@gmail.com");
		Student tempStudent2 = new Student("Camilo", "Perez", "juan@gmail.com");
		Student tempStudent3 = new Student("Pepe", "Lopez", "juan@gmail.com");

		// save the student object
		studentDAO.save(tempStudent1);
		studentDAO.save(tempStudent2);
		studentDAO.save(tempStudent3);

		// display the id of the saved student
		System.out.println("Students created");
		System.out.println(tempStudent1);
		System.out.println(tempStudent2);
		System.out.println(tempStudent3);

	}

}
