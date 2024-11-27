import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Employer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobAd> jobAds = new ArrayList<>();

    // Constructor
    public Employer() {}

    public Employer(String name) {
        this.name = name;
    }

    // Methods
    public void addJobAd(JobAd jobAd) {
        jobAds.add(jobAd);
    }

    public void removeJobAd(JobAd jobAd) {
        jobAds.remove(jobAd);
    }
    // Methods
    public void createJobAd(JobAd jobAd) {
        jobAds.add(jobAd);
    }

    public void deleteJobAd(int jobAdId) {
        jobAds.removeIf(jobAd -> jobAd.getId() == jobAdId);
    }

    public JobAd getJobAdById(int jobAdId) {
        return jobAds.stream()
                .filter(jobAd -> jobAd.getId() == jobAdId)
                .findFirst()
                .orElse(null);
    }

    public List<JobAd> getJobAds() {
        return jobAds;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

