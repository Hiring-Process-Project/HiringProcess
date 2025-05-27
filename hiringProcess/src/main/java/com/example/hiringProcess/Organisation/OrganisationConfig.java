package com.example.hiringProcess.Organisation;


import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Department.Department;
import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.JobAd.JobAd;
import com.example.hiringProcess.JobAd.JobAdRepository;
import com.example.hiringProcess.Question.Question;
import com.example.hiringProcess.QuestionScore.QuestionScore;
import com.example.hiringProcess.Skill.Skill;
import com.example.hiringProcess.Step.Step;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class OrganisationConfig {
    @Bean(name = "organisationCommandLineRunner")
    CommandLineRunner commandLineRunner(JobAdRepository repository) {
        return args -> {
            System.out.println("Saving Organisation...");

            Organisation org = new Organisation("Hype" ,"A good Organisation");
            Department dep = new Department("Software Development" ,"Abu Dhabi","the best department");
            Interview interview = new Interview();
            JobAd jobAd1= new JobAd("title1","description1" , LocalDate.of(2012, 12, 12) ,"waiting", interview);
            Step step1 = new Step("Technical Interview", "desc1");
            Candidate johny = new Candidate("Johny","Smith", "johny@gmail.com","person","open","because");

            interview.addStep(step1);

            jobAd1.addCandidate(johny);

            Question q1 = new Question("What is polymorphism?");

            Skill s1 = new Skill("Polymorphism");

            step1.addQuestion(q1);

            QuestionScore qs1 = new QuestionScore(56.2);

            q1.addScore(qs1);


        };
    }
}
