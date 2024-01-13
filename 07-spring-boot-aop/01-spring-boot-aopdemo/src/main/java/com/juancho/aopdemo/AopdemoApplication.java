package com.juancho.aopdemo;

import com.juancho.aopdemo.dao.AccountDAO;
import com.juancho.aopdemo.dao.MembershipDAO;
import com.juancho.aopdemo.service.TrafficFortuneService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class AopdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AopdemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AccountDAO accountDAO,
			MembershipDAO membershipDAO,
			TrafficFortuneService trafficFortuneService
	) {
		return runner -> {
//			demoTheBeforeAdvice(accountDAO, membershipDAO);
//			demoTheAfterRerturningAdvice(accountDAO);
//			demoTheAfterThrowingAdvice(accountDAO);
//			demoTheAfterAdvice(accountDAO);
//			demoTheAroundAdvice(trafficFortuneService);
			demoTheAroundAdviceHandlingException(trafficFortuneService);
		};

	}

	private void demoTheAroundAdviceHandlingException(TrafficFortuneService trafficFortuneService) {
		System.out.println("\n Main program: demoTheAroundAdviceHandlingException");
		System.out.println("Calling getFortune()...");
		boolean tripWire = true;
		String data = trafficFortuneService.getFortune(tripWire);
		System.out.println("My fortune is: " + data);
	}

	private void demoTheAroundAdvice(TrafficFortuneService trafficFortuneService) {
		System.out.println("\n Main program: demoTheAroundAdvice");
		System.out.println("Calling getFortune()...");
		String data = trafficFortuneService.getFortune();
		System.out.println("My fortune is: " + data);
	}

	private void demoTheAfterAdvice(AccountDAO accountDAO) {
		List<Account> accounts = null;
		try {
			// add boolean flag to simulate exception
			boolean tripWire = false;
			accounts = accountDAO.findAccounts(tripWire);
		} catch (Exception exc) {
			System.out.println("\nMain Program: caught exception: " + exc);
		}
	}

	private void demoTheAfterThrowingAdvice(AccountDAO accountDAO) {
		List<Account> accounts = null;
		try {
			// add boolean flag to simulate exception
			boolean tripWire = true;
			accounts = accountDAO.findAccounts(true);
		}
		catch (Exception exc) {
			System.out.println("\nMain Program: caught exception: " + exc);
		}
	}

	private void demoTheAfterRerturningAdvice(AccountDAO accountDAO) {
		List<Account> accounts = accountDAO.findAccounts();
		System.out.println("\nMain Program: demoTheAfterRerturningAdvice\n");
		System.out.println(accounts);
	}

	private void demoTheBeforeAdvice(AccountDAO accountDAO, MembershipDAO membershipDAO) {
		// call the business method
		Account account = new Account("Pepe", "Admin");
		accountDAO.addAccount(account);
		// call account setters and getters
		accountDAO.setName("Foobar");
		accountDAO.setServiceCode("Silver");
		String name = accountDAO.getName();
		String code = accountDAO.getServiceCode();

		// call membership business method
		membershipDAO.addAccount();

	}

}
