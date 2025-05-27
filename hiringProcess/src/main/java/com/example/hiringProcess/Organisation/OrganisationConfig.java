package com.example.hiringProcess.Organisation;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Department.Department;
import com.example.hiringProcess.Interview.Interview;
import com.example.hiringProcess.JobAd.JobAd;
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
    CommandLineRunner commandLineRunner(OrganisationRepository repository) {
        return args -> {
            System.out.println("Saving Organisation...");

            // Δημιουργία Organisation
            Organisation org = new Organisation("Hype", "A good Organisation");

            // Δημιουργία Department και σύνδεση με Organisation
            Department dep = new Department("Software Development", "Abu Dhabi", "the best department");
            org.addDepartment(dep); // σημαντικό: προσθήκη Department στο Organisation και το αντίστροφο

            // Δημιουργία Interview
            Interview interview = new Interview();

            // Δημιουργία JobAd με συνδεδεμένο Interview
            JobAd jobAd1 = new JobAd("title1", "description1", LocalDate.of(2012, 12, 12), "waiting", interview);

            // Σύνδεση JobAd με Department
            jobAd1.getDepartments().add(dep); // προσθήκη Department στο JobAd

            // Δημιουργία Step και προσθήκη στο Interview
            Step step1 = new Step("Technical Interview", "desc1");
            interview.addStep(step1);

            // Δημιουργία Candidate και προσθήκη στο JobAd
            Candidate johny = new Candidate("Johny", "Smith", "johny@gmail.com", "person", "open", "because");
            jobAd1.addCandidate(johny);

            // Δημιουργία Question και προσθήκη στο Step
            Question q1 = new Question("What is polymorphism?");
            step1.addQuestion(q1);

            // Δημιουργία Skill (προαιρετικό αν δεν το συνδέεις)
            Skill s1 = new Skill("Polymorphism");

            // Δημιουργία QuestionScore και προσθήκη στο Question
            QuestionScore qs1 = new QuestionScore(56.2);
            q1.addScore(qs1);

            // Αποθήκευση ολόκληρης της ιεραρχίας μέσω του Organisation repository
            repository.save(org);

            System.out.println("Organisation saved with related entities.");
        };
    }
}
