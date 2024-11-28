package com.example.hiringProcess.Candidate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CandidateConfig {
    @Bean
    CommandLineRunner commandLineRunner(CandidateRepository repository){
        return args -> {
           Candidate johny = new Candidate(
                   "Johny");

            Candidate jamal = new Candidate(
                    "Jamal");
            repository.saveAll(List.of(johny, jamal));

        };
    }
}
