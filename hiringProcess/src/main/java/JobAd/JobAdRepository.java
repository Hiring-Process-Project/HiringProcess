package JobAd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//για την αλληλεπίδραση με τη βάση δεδομένων

    @Repository
    public interface JobAdRepository extends JpaRepository<JobAd, Integer> {
        @Query("SELECT c FROM JobAd c WHERE c.title = ?1")
        Optional<JobAd> findJobAdByTitle(String title);
    }


