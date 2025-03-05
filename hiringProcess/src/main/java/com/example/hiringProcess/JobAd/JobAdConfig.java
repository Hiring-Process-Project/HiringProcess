package com.example.hiringProcess.JobAd;

import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.Questions.Questions;
import com.example.hiringProcess.Step.Step;
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

            Interview interview = new Interview();



            Step step1 = new Step("Technical Interview");
            Step step2 = new Step("HR Interview");

            // Πρόσθεσε τα steps στο interview
            interview.addStep(step1);
            interview.addStep(step2);

            // Δημιουργία ερωτήσεων
            Questions q1 = new Questions("What is polymorphism?");
            Questions q2 = new Questions("Explain dependency injection.");
            Questions q3 = new Questions("What is the difference between abstract classes and interfaces?");

            // Προσθήκη των ερωτήσεων στο Step
            step1.addQuestion(q1);
            step1.addQuestion(q2);
            step1.addQuestion(q3);

            jobAd1.setInterview(interview);
            repository.save(jobAd1);
            System.out.println("JobAds saved.");
        };
    }
}