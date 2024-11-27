import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class JobAd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String description;
    private LocalDateTime postingDate;

    @OneToMany(mappedBy = "jobAd", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidate> candidates = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> interviewSkills = new ArrayList<>();

    private Integer hiredCandidateId; // ID του υποψηφίου που προσλήφθηκε

    // Constructor
    public JobAd() {
        this.postingDate = LocalDateTime.now();
    }

    public JobAd(String title, String description) {
        this.title = title;
        this.description = description;
        this.postingDate = LocalDateTime.now();
    }

    // Methods
    public void addCandidate(Candidate candidate) {
        candidate.setJobAd(this);
        candidates.add(candidate);
    }

    public void removeCandidate(Candidate candidate) {
        candidates.remove(candidate);
    }

    public void addSkill(Skill skill) {
        interviewSkills.add(skill);
    }

    public void removeSkill(Skill skill) {
        interviewSkills.remove(skill);
    }

    public void setHiredCandidateId(Integer candidateId) {
        this.hiredCandidateId = candidateId;
    }

        // Methods
        public void addCandidate(Candidate candidate) {
            candidates.add(candidate);
        }

        public void removeCandidate(int candidateId) {
            candidates.removeIf(candidate -> candidate.getId() == candidateId);
        }

        public void addSkill(Skill skill) {
            interviewSkills.add(skill);
        }

        public void removeSkill(int skillId) {
            interviewSkills.removeIf(skill -> skill.getId() == skillId);
        }

        public void setHiredCandidateId(int candidateId) {
            this.hiredCandidateId = candidateId;
        }

        // Getters and Setters
        public int getId() { return id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public LocalDateTime getPostingDate() { return postingDate; }
        public List<Candidate> getCandidates() { return candidates; }
        public List<Skill> getInterviewSkills() { return interviewSkills; }
        public Integer getHiredCandidateId() { return hiredCandidateId; }
    }


