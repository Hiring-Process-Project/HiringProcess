// src/main/java/com/example/hiringProcess/Candidate/CandidateRepository.java
package com.example.hiringProcess.Candidate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    List<Candidate> findByJobAd_Id(Integer jobAdId);

//    @Query("""
//   SELECT new com.example.hiringProcess.Candidate.CandidateFinalScoreDTO(
//       c.id,
//       c.firstName,
//       c.lastName,
//       c.status,
//       AVG(ss.score),
//       COUNT(ss.score),
//       COUNT(ss)
//   )
//   FROM Candidate c
//   LEFT JOIN SkillScore ss
//          ON ss.candidate = c
//         AND ss.score IS NOT NULL
//   WHERE c.jobAd.id = :jobAdId
//   GROUP BY c.id, c.firstName, c.lastName, c.status
//   ORDER BY
//     CASE WHEN AVG(ss.score) IS NULL THEN 1 ELSE 0 END,
//     AVG(ss.score) DESC
//""")
//    List<CandidateFinalScoreDTO> findFinalScoresByJobAd(@Param("jobAdId") Integer jobAdId);

    @Query("""
SELECT new com.example.hiringProcess.Candidate.CandidateFinalScoreDTO(
    c.id,
    c.firstName,
    c.lastName,
    c.status,
    AVG(ss.score),
    SUM(CASE WHEN ss.score IS NOT NULL THEN 1 ELSE 0 END), 
    COUNT(ss)                                              
)
FROM Candidate c
LEFT JOIN SkillScore ss
       ON ss.candidate = c                               
WHERE c.jobAd.id = :jobAdId
GROUP BY c.id, c.firstName, c.lastName, c.status
ORDER BY
  CASE WHEN AVG(ss.score) IS NULL THEN 1 ELSE 0 END,
  AVG(ss.score) DESC
""")
    List<CandidateFinalScoreDTO> findFinalScoresByJobAd(@Param("jobAdId") Integer jobAdId);


}
