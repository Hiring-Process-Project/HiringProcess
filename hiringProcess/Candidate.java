import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ElementCollection
    private List<String> uploadedFiles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "job_ad_id")
    private JobAd jobAd;

    // Constructor
    public Candidate() {}

    public Candidate(String name) {
        this.name = name;
    }

    // Methods
    public void uploadFile(String filePath) {
        uploadedFiles.add(filePath);
    }

    public void removeFile(String filePath) {
        uploadedFiles.remove(filePath);
    }

    // Methods
        public void uploadFile(String filePath) {
            uploadedFiles.add(filePath);
        }

        public void removeFile(String filePath) {
            uploadedFiles.remove(filePath);
        }

        // Getters and Setters
        public int getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public List<String> getUploadedFiles() { return uploadedFiles; }
        public int getJobAdId() { return jobAdId; }
        public void setJobAdId(int jobAdId) { this.jobAdId = jobAdId; }
    }


