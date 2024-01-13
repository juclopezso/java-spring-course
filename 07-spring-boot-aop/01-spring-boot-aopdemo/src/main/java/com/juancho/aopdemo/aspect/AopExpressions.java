package com.juancho.aopdemo.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class AopExpressions {

    // class of related advices for logging

    // args inside "" are pointcut expression
    // run this code BEFORE - target object method: all public void addAccount()
    // @Before("execution(public void addAccount())")

    // only matches AccountDAO.addAccount()
    // @Before("execution(public void com.juancho.aopdemo.dao.AccountDAO.addAccount())")

    // matches every add* method
    // @Before("execution(public void add*())")

    // matches every method that has return type of void and it's name matches add*
    // @Before("execution(void add*())")

    // matches every method that it's name matches add* with no args
    // @Before("execution(* add*())")

    // matches only the add* method that has Account as param and multiple params after
    // @Before("execution(* add*(com.juancho.aopdemo.Account, ..))")

    // matches only the add* method that has any params
    // @Before("execution(* add*(..))")

    // match methods in the dao package with any params
    // 1st * -> any return type
    // 2nd * -> any class in the package
    // 3rd * -> any method in the classes
    @Pointcut("execution(* com.juancho.aopdemo.dao.*.*(..))")
    public void forDaoPackage() {}

    // create a pointcut for getter methods
    @Pointcut("execution(* com.juancho.aopdemo.dao.*.get*(..))")
    public void forDaoGetters() {}

    // create a pointcut for setter methods
    @Pointcut("execution(* com.juancho.aopdemo.dao.*.set*(..))")
    public void forDaoSetters() {}

    // create a pointcut: include package and exclude getters/setters
    @Pointcut("forDaoPackage() && !(forDaoGetters() || forDaoSetters())")
    public void forDaoPackageNoGetterSetter() {}

}
