package com.juancho.mycoolapp.rest;

import com.juancho.mycoolapp.common.Coach;
import com.juancho.mycoolapp.common.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoachController {

    private Coach coach;
    private Coach anotherCoach;
    private Team team;

    // constructor dependency injection
    @Autowired
    // Qualifier usage
    public CoachController(
            @Qualifier("tennisCoach") Coach coach,
            @Qualifier("tennisCoach") Coach anotherCoach) {
        this.coach = coach;
        this.anotherCoach = anotherCoach;
        System.out.println("In constructor: " + getClass().getSimpleName());
    }

//    @Autowired
    // Primary annotation used in the implementation
//    public CoachController(@Qualifier("tennisCoach") Coach coach) {
//        this.coach = coach;
//    }

    // setter dependency injection
    // the method could be called anything
    @Autowired
    public void setTeam(Team team) {
        this.team = team;
    }

    @GetMapping("/daily-workout")
    public String getDailyWorkout() {
        return coach.getDailyWorkout();
    }

    @GetMapping("/next-rival")
    public String getNextRival() {
        return team.getNextRival();
    }

    @GetMapping("/check-beans")
    public String checkBeans() {
        // checks is Coach instances are the same
        // true if beans are singleton
        // false if beans are prototype
        Boolean equalBeans = (coach == anotherCoach);
        return "Comparing beans: coach == anotherCoach" + " -> " + equalBeans.toString();
    }

}
