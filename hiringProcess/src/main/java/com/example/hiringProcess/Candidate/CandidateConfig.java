package com.example.hiringProcess.Candidate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CandidateConfig {
    @Bean(name = "candidateCommandLineRunner")
    CommandLineRunner commandLineRunner(CandidateRepository repository){
        System.out.println("HIIIIIIIIIIIIIIII");
        return args -> {
            System.out.println("Saving candidates...");
           Candidate johny = new Candidate(
                   "Johny");

            Candidate jamal = new Candidate(
                    "Jamal");
            repository.saveAll(List.of(johny, jamal));
            System.out.println("Candidates saved.");
        };
    }
}
