package com.example.hiringProcess.JobAd;

import com.example.hiringProcess.Cand_Score.Cand_Score;
import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.Questions.Questions;
import com.example.hiringProcess.Step.Step;
import com.example.hiringProcess.Skill.Skill;
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

            // Δημιουργία skills
            Skill s1 = new Skill("Polymorphism");
            Skill s2 = new Skill("Dependency injection");
            Skill s3 = new Skill("Difference between abstract classes and interfaces");

            //Δημιοργία score για τα skills
            Cand_Score score1 = new Cand_Score(20);
            Cand_Score score2 = new Cand_Score(30);



            // Προσθήκη των ερωτήσεων στο Step
            step1.addQuestion(q1);
            step1.addQuestion(q2);
            step1.addQuestion(q3);

            // Προσθήκη των skill στο question
            q1.addSkill(s1);
            q2.addSkill(s2);
            q3.addSkill(s3);

            //Προσθήκη score στα skill
            s1.addcand_score(score1);
            s2.addcand_score(score2);

            jobAd1.setInterview(interview);
            repository.save(jobAd1);
            System.out.println("JobAds saved.");
        };
    }
}