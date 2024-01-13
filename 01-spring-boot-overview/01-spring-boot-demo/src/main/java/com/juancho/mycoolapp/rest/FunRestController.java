package com.juancho.mycoolapp.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FunRestController {

    // inject properties
    @Value("${coach.name}")
    private String coachName;

    @Value("${team.name}")
    private String teamName;

    @GetMapping("/")
    public String sayHello() {
        return "Hello world";
    }

    @GetMapping("/workout")
    public String getDailyWortkout() {
        return "I'm working out";
    }

    @GetMapping("/fortune")
    public String getDay() {
        return "Today is " + "tuesday";
    }

    @GetMapping("/team-info")
    public String teamInfo() {
        return "Team: " + teamName + ", Coach: " + coachName;
    }

}
