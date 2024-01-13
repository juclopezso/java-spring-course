package com.juancho.mycoolapp.common;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Primary
@Lazy
// changes the bean scope to prototype
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TennisCoach implements Coach {

    public TennisCoach() {
        System.out.println("In constructor: " + getClass().getSimpleName());
    }

    @Override
    public String getDailyWorkout() {
        return "Practice serve";
    }

    // define our init bean method
    @PostConstruct
    public void doStartupStuff() {
        System.out.println("In doStartupStuff(): " + getClass().getSimpleName());
    }

    // define our destroy bean method
    @PostConstruct
    public void doCleanupStuff() {
        System.out.println("In doCleanupStuff(): " + getClass().getSimpleName());
    }

}
