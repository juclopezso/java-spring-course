package com.juancho.mycoolapp.common;

import org.springframework.stereotype.Component;

@Component
public class RedTeam implements Team {

    @Override
    public String getNextRival() {
        return "The Big Bosses";
    }

}
