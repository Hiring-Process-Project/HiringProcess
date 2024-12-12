package JobAd;

import com.example.hiringProcess.Candidate.Candidate;
import com.example.hiringProcess.Candidate.CandidateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class JobAdConfig {
    @Bean
    CommandLineRunner commandLineRunner(JobAdRepository repository) {
        return args -> {
            System.out.println("Saving jobAds...");

            JobAd jobAd1= new JobAd("title1","desc1" , LocalDate.of(2012, 12, 12) ,"waiting");

            repository.save(jobAd1);
            System.out.println("JobAds saved.");
        };
    }
}