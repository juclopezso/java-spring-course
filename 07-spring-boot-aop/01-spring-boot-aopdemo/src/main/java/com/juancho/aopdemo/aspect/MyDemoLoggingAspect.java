package com.juancho.aopdemo.aspect;

import com.juancho.aopdemo.Account;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@Order(10)
public class MyDemoLoggingAspect {

    @Around("execution(* com.juancho.aopdemo.service.TrafficFortuneService.getFortune(..))")
    public Object aroundTheFortune(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // print which method we are advising on
        String method = proceedingJoinPoint.getSignature().toShortString();
        System.out.println("*** Executing @Around on method: " + method);
        // begin timestamp
        long begin = System.currentTimeMillis();
        // execute the method
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Exception exc) {
            // log the exception
            System.out.println(exc.getMessage());
            // give user a custom message
            result = "Major accident! But no worries AOP is on the way!";
            // re-throws the exception
            // REMEMBER: delete line below to not throw the exception
            throw exc;
        }
        // end timestamp
        long end = System.currentTimeMillis();
        // compute duration and display it
        long duration = end - begin;
        System.out.println("*** Duration: " + duration / 1000.0 + " seconds");

        return result;
    }

    @After("execution(* com.juancho.aopdemo.dao.AccountDAO.findAccounts(..))")
    public void afterFinallyFindAccountsAdvice(JoinPoint joinPoint) {
        // print which method we are advising on
        String method = joinPoint.getSignature().toShortString();
        System.out.println("*** Executing @After (finally) on method: " + method);
    }

    @AfterThrowing(
            pointcut = "execution(* com.juancho.aopdemo.dao.AccountDAO.findAccounts(..))",
            throwing = "theExc"
    )
    public void afterThrowingFindAccountsAdvice(JoinPoint joinPoint, Throwable theExc) {
        // print which method we are advising on
        String method = joinPoint.getSignature().toShortString();
        System.out.println("*** Executing @AfterThrowing on method: " + method);

        // log the exception
        System.out.println("*** Sending report to devops... The exception is: " + theExc);
    }

    @AfterReturning(
            pointcut = "execution(* com.juancho.aopdemo.dao.AccountDAO.findAccounts(..))",
            returning = "result"
    )
    public void afterReturingFindAccountsAdvice(JoinPoint joinPoint, List<Account> result) {
        // print out which method we are advising on
        String method = joinPoint.getSignature().toShortString();
        System.out.println("*** Executing @AfterReturning on method: " + method);
        System.out.println("*** Result is: " + result);
        // post-process data - modify data (convert names to uppercase)
        accountNameToUpper(result);
        // print the results of the method call
        System.out.println("*** Result after modifying is: " + result);
    }

    private void accountNameToUpper(List<Account> result) {
        for (Account account : result) {
            account.setName(account.getName().toUpperCase());
        }
    }

    @Before("com.juancho.aopdemo.aspect.AopExpressions.forDaoPackageNoGetterSetter()")
    // can be any method name
    public  void beforeAddAccountAdvice(JoinPoint joinPoint) {
        System.out.println("\n*** Executing @Before advice on addAccount() ***");
        // display the method signature
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        System.out.println("Method: " + methodSignature);

        // display method arguments
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            System.out.println("Arg: " + arg);
            if (arg instanceof Account) {
                // downcast and print Account stuff
                Account account = (Account) arg;
                System.out.println("Account name: " + account.getName());
                System.out.println("Account level: " + account.getLevel());
            }
        }

    }


}
