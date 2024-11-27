import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class InterviewGuide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "job_ad_id")
    private JobAd jobAd;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills = new ArrayList<>();

    // Constructor
    public InterviewGuide() {}

    public InterviewGuide(JobAd jobAd) {
        this.jobAd = jobAd;
    }

    // Methods
    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public void removeSkill(Skill skill) {
        skills.remove(skill);
    }

    public void reorderSkill(int oldIndex, int newIndex) {
        if (oldIndex >= 0 && oldIndex < skills.size() && newIndex >= 0 && newIndex < skills.size()) {
            Skill skill = skills.remove(oldIndex);
            skills.add(newIndex, skill);
        }
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public JobAd getJobAd() {
        return jobAd;
    }

    public void setJobAd(JobAd jobAd) {
        this.jobAd = jobAd;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
}

