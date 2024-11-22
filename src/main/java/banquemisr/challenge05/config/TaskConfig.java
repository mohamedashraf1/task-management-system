package banquemisr.challenge05.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfig {

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
