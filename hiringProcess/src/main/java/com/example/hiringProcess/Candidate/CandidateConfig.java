package com.example.hiringProcess.Candidate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CandidateConfig {
    @Bean(name = "candidateCommandLineRunner")
    CommandLineRunner commandLineRunner(CandidateRepository repository){
        System.out.println("HIIIIIIIIIIIIIIII");
        return args -> {
            System.out.println("Saving candidates...");
            System.out.println("Candidates saved.");
        };
    }
}
