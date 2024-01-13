package com.juancho.config;

import com.juancho.mycoolapp.common.Coach;
import com.juancho.mycoolapp.common.SwimCoach;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// configuring beans without the usage of @Component
// java config bean
@Configuration
public class SportConfig {

    @Bean("aquatic")
    public Coach swimCoach() {
        return new SwimCoach();
    }

}
