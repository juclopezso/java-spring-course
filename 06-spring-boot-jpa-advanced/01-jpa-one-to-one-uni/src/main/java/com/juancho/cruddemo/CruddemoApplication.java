package com.juancho.cruddemo;

import com.juancho.cruddemo.dao.AppDAO;
import com.juancho.cruddemo.entity.Instructor;
import com.juancho.cruddemo.entity.InstructorDetail;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
//			deleteInstructorById(appDAO);
//			findInstructorDetailById(appDAO);
			deleteInstructorDetail(appDAO);

		};
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
