package com.example.hiringProcess.JobAd;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class JobAdConfig {
    @Bean(name = "jobAdCommandLineRunner")
    CommandLineRunner commandLineRunner(JobAdRepository repository) {
        return args -> {
            System.out.println("Saving jobAds...");

            JobAd jobAd1= new JobAd("title1","description1" , LocalDate.of(2012, 12, 12) ,"waiting");

            repository.save(jobAd1);
            System.out.println("JobAds saved.");
        };
    }
}