package com.example.hiringProcess.Analytics;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class AnalyticsRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public AnalyticsRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /* ======================  ORGANIZATION  ====================== */

    // Μετρά ΟΛΟΥΣ τους υποψήφιους του οργανισμού orgId.
    public long countTotalCandidatesByOrg(int orgId) {
        String sql = """
        SELECT COUNT(*) 
        FROM candidate c
        WHERE EXISTS (
            SELECT 1
            FROM job_ad ja
            JOIN jobad_department jd ON jd.jobad_id = ja.id
            JOIN department d ON d.id = jd.department_id
            WHERE ja.id = c.job_ad_id
              AND d.organisation_id = :orgId
        )
        """;
        Long n = jdbc.queryForObject(sql, Map.of("orgId", orgId), Long.class);
        return n == null ? 0L : n;
    }

    // Μετρά ΟΛΟΥΣ τους υποψήφιους ενός οργανισμού με συγκεκριμένο status
    // (όποιο status του δώσoυμε στο όρισμα status)
    public long countCandidatesByStatusOrg(int orgId, String status) {
        String sql = """
        SELECT COUNT(*)
        FROM candidate c
        WHERE c.status = :status
          AND EXISTS (
            SELECT 1
            FROM job_ad ja
            JOIN jobad_department jd ON jd.jobad_id = ja.id
            JOIN department d ON d.id = jd.department_id
            WHERE ja.id = c.job_ad_id
              AND d.organisation_id = :orgId
          )
        """;
        Long n = jdbc.queryForObject(sql, Map.of("orgId", orgId, "status", status), Long.class);
        return n == null ? 0L : n;
    }

    /* === Mappers === */
    // Επιστρέφει τα καλύτερα N skills του οργανισμού
    public List<SkillAvgDto> topSkillsByOrg(int orgId, int limit) {
        String sql = """
        SELECT s.title AS skill, AVG(ss.score) AS avg_score
        FROM skill_score ss
        JOIN candidate c           ON c.id = ss.candidate_id
        JOIN job_ad ja             ON ja.id = c.job_ad_id
        JOIN jobad_department jd   ON jd.jobad_id = ja.id
        JOIN department d          ON d.id = jd.department_id
        JOIN skill s               ON s.id = ss.skill_id
        WHERE d.organisation_id = :orgId
        GROUP BY s.title
        HAVING COUNT(ss.score) > 0
        ORDER BY avg_score DESC
        LIMIT :limit
        """;
        return jdbc.query(sql, Map.of("orgId", orgId, "limit", limit), AnalyticsMapper.SKILL_AVG);
    }

    // Επιστρέφει τα χειρότερα N skills του οργανισμού
    public List<SkillAvgDto> weakestSkillsByOrg(int orgId, int limit) {
        String sql = """
        SELECT s.title AS skill, AVG(ss.score) AS avg_score
        FROM skill_score ss
        JOIN candidate c           ON c.id = ss.candidate_id
        JOIN job_ad ja             ON ja.id = c.job_ad_id
        JOIN jobad_department jd   ON jd.jobad_id = ja.id
        JOIN department d          ON d.id = jd.department_id
        JOIN skill s               ON s.id = ss.skill_id
        WHERE d.organisation_id = :orgId
        GROUP BY s.title
        HAVING COUNT(ss.score) > 0
        ORDER BY avg_score ASC
        LIMIT :limit
        """;
        return jdbc.query(sql, Map.of("orgId", orgId, "limit", limit), AnalyticsMapper.SKILL_AVG);
    }

    // Προσθήκη: μέσοι όροι υποψηφίων (0..10) για ΟΛΟ τον οργανισμό
    public List<Double> candidateAvgScoresByOrg(int orgId) {
        String sql = """
    WITH q AS (
      SELECT c.id AS cid, AVG(qs.score) AS avg10  -- question_score ήδη 0..10
      FROM candidate c
      JOIN job_ad ja             ON ja.id = c.job_ad_id
      JOIN jobad_department jd   ON jd.jobad_id = ja.id
      JOIN department d          ON d.id = jd.department_id
      LEFT JOIN interview_report ir ON ir.id = c.interview_report_id
      LEFT JOIN step_score sr       ON sr.interview_report_id = ir.id
      LEFT JOIN question_score qs   ON qs.step_score_id = sr.id
      WHERE d.organisation_id = :orgId
      GROUP BY c.id
    ),
    s AS (
      SELECT c.id AS cid, AVG(ss.score) / 10.0 AS avg10  -- skill_score 0..100 -> 0..10
      FROM candidate c
      JOIN job_ad ja             ON ja.id = c.job_ad_id
      JOIN jobad_department jd   ON jd.jobad_id = ja.id
      JOIN department d          ON d.id = jd.department_id
      LEFT JOIN skill_score ss   ON ss.candidate_id = c.id
      WHERE d.organisation_id = :orgId
      GROUP BY c.id
    )
    SELECT COALESCE(q.avg10, s.avg10) AS avg10
    FROM (
      SELECT id FROM candidate
      WHERE EXISTS (
        SELECT 1
        FROM job_ad ja
        JOIN jobad_department jd ON jd.jobad_id = ja.id
        JOIN department d        ON d.id = jd.department_id
        WHERE ja.id = candidate.job_ad_id AND d.organisation_id = :orgId
      )
    ) cands
    LEFT JOIN q ON q.cid = cands.id
    LEFT JOIN s ON s.cid = cands.id
    WHERE q.avg10 IS NOT NULL OR s.avg10 IS NOT NULL
    """;
        return jdbc.query(sql, Map.of("orgId", orgId), (rs, i) -> rs.getDouble("avg10"));
    }


    // Μέσος αριθμός υποψηφίων ανά Job Ad για έναν οργανισμό
    public double avgCandidatesPerJobAdOrg(int orgId) {
        String sql = """
        SELECT COALESCE(AVG(cnt), 0.0)
        FROM (
            SELECT ja.id AS jobad_id, COUNT(c.id) AS cnt
            FROM job_ad ja
            JOIN jobad_department jd ON jd.jobad_id = ja.id
            JOIN department d        ON d.id = jd.department_id
            LEFT JOIN candidate c    ON c.job_ad_id = ja.id
            WHERE d.organisation_id = :orgId
            GROUP BY ja.id
        ) t
        """;
        Double v = jdbc.queryForObject(sql, Map.of("orgId", orgId), Double.class);
        return v == null ? 0.0 : v;
    }

    /* ======================  DEPARTMENT  ====================== */

    // Μετρά ΟΛΟΥΣ τους υποψήφιους του department deptId.
    public long countTotalCandidatesByDept(int deptId) {
        String sql = """
        SELECT COUNT(c.id)
        FROM candidate c
        JOIN job_ad ja ON c.job_ad_id = ja.id
        JOIN jobad_department jd ON ja.id = jd.jobad_id
        WHERE jd.department_id = :deptId
        """;
        Long n = jdbc.queryForObject(sql, Map.of("deptId", deptId), Long.class);
        return n == null ? 0L : n;
    }

    // Μετρά ΟΛΟΥΣ τους υποψήφιους ενός department με συγκεκριμένο status (όποιο status του δώσoυμε στο όρισμα status)
    public long countCandidatesByStatusDept(int deptId, String status) {
        String sql = """
        SELECT COUNT(c.id)
        FROM candidate c
        JOIN job_ad ja ON c.job_ad_id = ja.id
        JOIN jobad_department jd ON ja.id = jd.jobad_id
        WHERE jd.department_id = :deptId AND c.status = :status
        """;
        Long n = jdbc.queryForObject(sql, Map.of("deptId", deptId, "status", status), Long.class);
        return n == null ? 0L : n;
    }

    // Υπολογίζει τον μέσο αριθμό υποψηφίων ανά αγγελία εργασίας (job_ad) για το συγκεκριμένο department.
    public Double avgCandidatesPerJobAdInDept(int deptId) {
        String sql = """
        SELECT AVG(cnt) FROM (
            SELECT COUNT(c.id) AS cnt
            FROM job_ad ja
            JOIN jobad_department jd ON ja.id = jd.jobad_id
            LEFT JOIN candidate c ON c.job_ad_id = ja.id
            WHERE jd.department_id = :deptId
            GROUP BY ja.id
        ) sub
        """;
        Double v = jdbc.queryForObject(sql, Map.of("deptId", deptId), Double.class);
        return v == null ? 0.0 : v;
    }

    // Επιστρέφει histogram κατανομής βαθμών (buckets 0–9) ανά department (0–100 ranges)
    // NEW: μέσοι όροι βαθμών (0–10) για όλα τα candidates ενός department
    public List<Double> candidateAvgScoresByDept(int deptId) {
        String sql = """
    WITH q AS (
      SELECT c.id AS cid, AVG(qs.score) AS avg10      -- question_score 0..10
      FROM candidate c
      JOIN job_ad ja           ON ja.id = c.job_ad_id
      JOIN jobad_department jd ON jd.jobad_id = ja.id
      LEFT JOIN interview_report ir ON ir.id = c.interview_report_id
      LEFT JOIN step_score sr       ON sr.interview_report_id = ir.id
      LEFT JOIN question_score qs   ON qs.step_score_id = sr.id
      WHERE jd.department_id = :deptId
      GROUP BY c.id
    ),
    s AS (
      SELECT c.id AS cid, AVG(ss.score) / 10.0 AS avg10   -- skill_score 0..100 -> 0..10
      FROM candidate c
      JOIN job_ad ja           ON ja.id = c.job_ad_id
      JOIN jobad_department jd ON jd.jobad_id = ja.id
      LEFT JOIN skill_score ss ON ss.candidate_id = c.id
      WHERE jd.department_id = :deptId
      GROUP BY c.id
    )
    SELECT COALESCE(q.avg10, s.avg10) AS avg10
    FROM (
      SELECT c.id
      FROM candidate c
      JOIN job_ad ja           ON ja.id = c.job_ad_id
      JOIN jobad_department jd ON jd.jobad_id = ja.id
      WHERE jd.department_id = :deptId
    ) cands
    LEFT JOIN q ON q.cid = cands.id
    LEFT JOIN s ON s.cid = cands.id
    WHERE COALESCE(q.avg10, s.avg10) IS NOT NULL
    """;
        return jdbc.query(sql, Map.of("deptId", deptId), (rs, i) -> rs.getDouble("avg10"));
    }


    //Υπολογίζει για το συγκεκριμένο department, τον μέσο όρο σκορ ανά occupation (χαμηλότερο = δυσκολότερο)
    //με βάση τα averages των υποψηφίων.
    public List<OccupationAvgDto> occupationDifficultyByDept(int deptId) {
        String sql = """
        WITH cand_avg AS (
            SELECT
                c.id AS cand_id,
                c.job_ad_id,
                CASE
                    WHEN COUNT(qs.score) > 0 THEN AVG(qs.score)
                    WHEN COUNT(ss.score) > 0 THEN AVG(ss.score)
                    ELSE NULL
                END AS avg_score
            FROM candidate c
            JOIN job_ad ja            ON c.job_ad_id = ja.id
            JOIN jobad_department jd  ON ja.id = jd.jobad_id
            LEFT JOIN interview_report ir ON ir.id = c.interview_report_id
            LEFT JOIN step_score sr       ON sr.interview_report_id = ir.id
            LEFT JOIN question_score qs   ON qs.step_score_id = sr.id
            LEFT JOIN skill_score ss      ON ss.candidate_id = c.id
            WHERE jd.department_id = :deptId
            GROUP BY c.id, c.job_ad_id
        )
        SELECT oc.title AS occupation, AVG(ca.avg_score) AS avg_score
        FROM cand_avg ca
        JOIN job_ad ja ON ja.id = ca.job_ad_id
        JOIN occupation oc ON oc.id = ja.occupation_id
        WHERE ca.avg_score IS NOT NULL
        GROUP BY oc.title
        ORDER BY avg_score ASC
        """;
        return jdbc.query(sql, Map.of("deptId", deptId),
                (rs, i) -> new OccupationAvgDto(rs.getString("occupation"),
                        rs.getObject("avg_score") == null ? 0.0 : rs.getDouble("avg_score")));
    }

    /* ======================  OCCUPATION (within Dept)  ====================== */

    // Μετρά όλους τους υποψήφιους για το συγκεκριμένο department -> occupation
    public long countTotalCandidatesByDeptOcc(int deptId, int occId) {
        String sql = """
        SELECT COUNT(c.id)
        FROM candidate c
        JOIN job_ad ja           ON c.job_ad_id = ja.id
        JOIN jobad_department jd ON ja.id = jd.jobad_id
        WHERE jd.department_id = :deptId AND ja.occupation_id = :occId
        """;
        Long n = jdbc.queryForObject(sql, Map.of("deptId", deptId, "occId", occId), Long.class);
        return n == null ? 0L : n;
    }

    // Μετρά τους υποψήφιους με συγκεκριμένο status στο συγκεκριμένο department & occupation.
    public long countCandidatesByStatusDeptOcc(int deptId, int occId, String status) {
        String sql = """
        SELECT COUNT(c.id)
        FROM candidate c
        JOIN job_ad ja           ON c.job_ad_id = ja.id
        JOIN jobad_department jd ON ja.id = jd.jobad_id
        WHERE jd.department_id = :deptId AND ja.occupation_id = :occId AND c.status = :status
        """;
        Long n = jdbc.queryForObject(sql, Map.of("deptId", deptId, "occId", occId, "status", status), Long.class);
        return n == null ? 0L : n;
    }

    // Υπολογίζει τον Μ.Ο. υποψηφίων ανά αγγελία για το συγκεκριμένο department & occupation
    public Double avgCandidatesPerJobAdDeptOcc(int deptId, int occId) {
        String sql = """
        SELECT AVG(cnt) 
        FROM (
            SELECT ja.id, COUNT(c.id) AS cnt
            FROM job_ad ja
            JOIN jobad_department jd ON ja.id = jd.jobad_id
            LEFT JOIN candidate c     ON c.job_ad_id = ja.id
            WHERE jd.department_id = :deptId AND ja.occupation_id = :occId
            GROUP BY ja.id
        ) t
        """;
        Double v = jdbc.queryForObject(sql, Map.of("deptId", deptId, "occId", occId), Double.class);
        return v == null ? 0.0 : v;
    }

    // Υπολογίζει τον Μ.Ο. τελικού score υποψηφίου σε ΚΛΙΜΑΚΑ 0–10 για dept+occupation
    public List<Double> candidateAvgScoresByDeptOcc(int deptId, int occId) {
        String sql = """
    WITH q AS (
      SELECT c.id AS cid, AVG(qs.score) AS avg10      -- question_score 0..10
      FROM candidate c
      JOIN job_ad ja           ON ja.id = c.job_ad_id
      JOIN jobad_department jd ON jd.jobad_id = ja.id
      LEFT JOIN interview_report ir ON ir.id = c.interview_report_id
      LEFT JOIN step_score sr       ON sr.interview_report_id = ir.id
      LEFT JOIN question_score qs   ON qs.step_score_id = sr.id
      WHERE jd.department_id = :deptId AND ja.occupation_id = :occId
      GROUP BY c.id
    ),
    s AS (
      SELECT c.id AS cid, AVG(ss.score) / 10.0 AS avg10   -- skill_score 0..100 -> 0..10
      FROM candidate c
      JOIN job_ad ja           ON ja.id = c.job_ad_id
      JOIN jobad_department jd ON jd.jobad_id = ja.id
      LEFT JOIN skill_score ss ON ss.candidate_id = c.id
      WHERE jd.department_id = :deptId AND ja.occupation_id = :occId
      GROUP BY c.id
    )
    SELECT COALESCE(q.avg10, s.avg10) AS avg10
    FROM (
      SELECT c.id
      FROM candidate c
      JOIN job_ad ja           ON ja.id = c.job_ad_id
      JOIN jobad_department jd ON jd.jobad_id = ja.id
      WHERE jd.department_id = :deptId AND ja.occupation_id = :occId
    ) cands
    LEFT JOIN q ON q.cid = cands.id
    LEFT JOIN s ON s.cid = cands.id
    WHERE COALESCE(q.avg10, s.avg10) IS NOT NULL
    """;
        return jdbc.query(sql, Map.of("deptId", deptId, "occId", occId), (rs, i) -> rs.getDouble("avg10"));
    }

    // Eπιστρέφει για το συγκεκριμενο occupation λίστα με τον μέσο (κανονικοποιημένο σε 0–10)
    // βαθμό υποψηφίων ανά αγγελία, ταξινομημένη αυξάνουσα.
    public List<JobAdAvgDto> jobAdDifficultyByDeptOcc(int deptId, int occId) {
        String sql = """
    WITH scale AS (
      SELECT
        CASE WHEN (SELECT COALESCE(MAX(score),0) FROM question_score) > 10 THEN 0.1 ELSE 1.0 END AS qf,
        CASE WHEN (SELECT COALESCE(MAX(score),0) FROM skill_score)    > 10 THEN 0.1 ELSE 1.0 END AS sf
    ),
    cand_avg AS (
      SELECT
        c.id AS cand_id,
        c.job_ad_id,
        CASE
          WHEN COUNT(qs.score) > 0 THEN AVG(qs.score) * (SELECT qf FROM scale)
          WHEN COUNT(ss.score) > 0 THEN AVG(ss.score) * (SELECT sf FROM scale)
          ELSE NULL
        END AS avg10
      FROM candidate c
      JOIN job_ad ja           ON ja.id = c.job_ad_id
      JOIN jobad_department jd ON jd.jobad_id = ja.id
      LEFT JOIN interview_report ir ON ir.id = c.interview_report_id
      LEFT JOIN step_score sr       ON sr.interview_report_id = ir.id
      LEFT JOIN question_score qs   ON qs.step_score_id = sr.id
      LEFT JOIN skill_score ss      ON ss.candidate_id = c.id
      WHERE jd.department_id = :deptId AND ja.occupation_id = :occId
      GROUP BY c.id, c.job_ad_id
      HAVING COUNT(qs.score) > 0 OR COUNT(ss.score) > 0
    )
    SELECT ja.title AS job_ad, AVG(ca.avg10) AS avg_score
    FROM cand_avg ca
    JOIN job_ad ja ON ja.id = ca.job_ad_id
    WHERE ca.avg10 IS NOT NULL
    GROUP BY ja.title
    ORDER BY avg_score ASC
    """;
        return jdbc.query(sql, Map.of("deptId", deptId, "occId", occId), AnalyticsMapper.JOBAD_AVG);
    }

    /* ======================  JOB AD  ====================== */

    // Μετρά όλους τους υποψήφιους για το συγκεκριμένο job ad
    public long countTotalCandidatesByJobAd(int jobAdId) {
        String sql = """
        SELECT COUNT(c.id)
        FROM candidate c
        WHERE c.job_ad_id = :jobAdId
        """;
        Long n = jdbc.queryForObject(sql, Map.of("jobAdId", jobAdId), Long.class);
        return n == null ? 0L : n;
    }

    // Μετρά τους υποψήφιους με συγκεκριμένο status στο συγκεκριμένο job ad
//    public long countCandidatesByStatusJobAd(int jobAdId, String status) {
//        String sql = """
//        SELECT COUNT(c.id)
//        FROM candidate c
//        WHERE c.job_ad_id = :jobAdId AND c.status = :status
//        """;
//        Long n = jdbc.queryForObject(sql, Map.of("jobAdId", jobAdId, "status", status), Long.class);
//        return n == null ? 0L : n;
//    }
    // Κράτα αυτή τη μέθοδο αλλά κάν’ την case-insensitive
    public long countCandidatesByStatusJobAd(int jobAdId, String status) {
        String sql = """
        SELECT COUNT(c.id)
        FROM candidate c
        WHERE c.job_ad_id = :jobAdId
          AND LOWER(TRIM(c.status)) = LOWER(:status)
        """;
        Long n = jdbc.queryForObject(sql, Map.of("jobAdId", jobAdId, "status", status), Long.class);
        return n == null ? 0L : n;
    }


    // Υπολογίζει την κατανομή (histogram) των μέσων βαθμών υποψηφίων σε buckets 0–9, 10–19, … για συγκεκριμένο Job Ad
    // 0..10 per-candidate averages (QS as-is, SS/10)
    public List<Double> candidateAvgScoresByJobAd(int jobAdId) {
        String sql = """
    WITH q AS (
      SELECT c.id AS cid, AVG(qs.score) AS avg10           -- question_score 0..10
      FROM candidate c
      LEFT JOIN interview_report ir ON ir.id = c.interview_report_id
      LEFT JOIN step_score sr       ON sr.interview_report_id = ir.id
      LEFT JOIN question_score qs   ON qs.step_score_id = sr.id
      WHERE c.job_ad_id = :jobAdId
      GROUP BY c.id
    ),
    s AS (
      SELECT c.id AS cid, AVG(ss.score) / 10.0 AS avg10    -- skill_score 0..100 -> 0..10
      FROM candidate c
      LEFT JOIN skill_score ss ON ss.candidate_id = c.id
      WHERE c.job_ad_id = :jobAdId
      GROUP BY c.id
    )
    SELECT COALESCE(q.avg10, s.avg10) AS avg10
    FROM candidate c
    LEFT JOIN q ON q.cid = c.id
    LEFT JOIN s ON s.cid = c.id
    WHERE c.job_ad_id = :jobAdId
      AND COALESCE(q.avg10, s.avg10) IS NOT NULL
    """;
        return jdbc.query(sql, Map.of("jobAdId", jobAdId), (rs, i) -> rs.getDouble("avg10"));
    }

    // Υπολογίζει τον μέσο βαθμό ανά step της αγγελίας (χαμηλότερο = δυσκολότερο)
    public List<StepAvgDto> stepDifficultyByJobAd(int jobAdId) {
        String sql = """
                WITH scale AS (
                  SELECT CASE WHEN (SELECT COALESCE(MAX(score),0) FROM skill_score) > 10
                              THEN 0.1 ELSE 1.0 END AS sf
                ),
                -- βαθμός ανά ΥΠΟΨΗΦΙΟ + ΕΡΩΤΗΣΗ από τα skill scores των skills της ερώτησης
                cand_q AS (
                  SELECT
                    st.title              AS step,
                    c.id                  AS cand_id,
                    q.id                  AS question_id,
                    AVG(ss.score) * (SELECT sf FROM scale) AS qscore10
                  FROM candidate c
                  JOIN job_ad ja            ON ja.id = c.job_ad_id
                  JOIN step st              ON st.interview_id = ja.interview_id
                  JOIN question q           ON q.step_id = st.id
                  JOIN question_skill qsk   ON qsk.question_id = q.id
                  JOIN skill_score ss       ON ss.candidate_id = c.id
                                            AND ss.skill_id     = qsk.skill_id
                  WHERE c.job_ad_id = :jobAdId
                  GROUP BY st.title, c.id, q.id
                )
                SELECT step, AVG(qscore10) AS avg_score
                FROM cand_q
                GROUP BY step
                HAVING COUNT(qscore10) > 0
                ORDER BY step
                """;
        return jdbc.query(sql, Map.of("jobAdId", jobAdId), AnalyticsMapper.STEP_AVG);
    }

    // Υπολογίζει τον μέσο βαθμό ανά question της αγγελίας (χαμηλότερο = δυσκολότερο)
    public List<QuestionAvgDto> questionDifficultyByJobAd(int jobAdId) {
        String sql = """
                WITH scale AS (
                  SELECT CASE WHEN (SELECT COALESCE(MAX(score),0) FROM skill_score) > 10
                              THEN 0.1 ELSE 1.0 END AS sf
                ),
                cand_q AS (
                  SELECT
                    q.id    AS question_id,
                    q.title AS question,
                    c.id    AS cand_id,
                    AVG(ss.score) * (SELECT sf FROM scale) AS qscore10
                  FROM candidate c
                  JOIN job_ad ja            ON ja.id = c.job_ad_id
                  JOIN step st              ON st.interview_id = ja.interview_id
                  JOIN question q           ON q.step_id = st.id
                  JOIN question_skill qsk   ON qsk.question_id = q.id
                  JOIN skill_score ss       ON ss.candidate_id = c.id
                                            AND ss.skill_id     = qsk.skill_id
                  WHERE c.job_ad_id = :jobAdId
                  GROUP BY q.id, q.title, c.id
                )
                SELECT question, AVG(qscore10) AS avg_score
                FROM cand_q
                GROUP BY question
                HAVING COUNT(qscore10) > 0
                ORDER BY avg_score ASC, question
                """;
        return jdbc.query(sql, Map.of("jobAdId", jobAdId), AnalyticsMapper.QUESTION_AVG);
    }

    // Υπολογίζει τον μέσο βαθμό ανά skill της αγγελίας (χαμηλότερο = δυσκολότερο)
    public List<SkillAvgDto> skillDifficultyByJobAd(int jobAdId) {
        String sql = """
                WITH scale AS (
                  SELECT CASE WHEN (SELECT COALESCE(MAX(score),0) FROM skill_score) > 10
                              THEN 0.1 ELSE 1.0 END AS sf
                ),
                -- per-candidate per-skill (για να μην “μετράει διπλά” όταν μια δεξιότητα εμφανίζεται σε πολλές ερωτήσεις)
                cand_skill AS (
                  SELECT
                    qsk.skill_id,
                    c.id AS cand_id,
                    AVG(ss.score) * (SELECT sf FROM scale) AS score10
                  FROM candidate c
                  JOIN job_ad ja            ON ja.id = c.job_ad_id
                  JOIN step st              ON st.interview_id = ja.interview_id
                  JOIN question q           ON q.step_id = st.id
                  JOIN question_skill qsk   ON qsk.question_id = q.id
                  JOIN skill_score ss       ON ss.candidate_id = c.id
                                           AND ss.skill_id     = qsk.skill_id
                  WHERE c.job_ad_id = :jobAdId
                  GROUP BY qsk.skill_id, c.id
                )
                SELECT sk.title AS skill, AVG(cs.score10) AS avg_score
                FROM cand_skill cs
                JOIN skill sk ON sk.id = cs.skill_id
                GROUP BY sk.title
                ORDER BY avg_score ASC
                """;
        return jdbc.query(sql, Map.of("jobAdId", jobAdId), AnalyticsMapper.SKILL_AVG);
    }

    /* ======================  CANDIDATE  ====================== */

    // Επιστρέφει το συνολικό score ενός υποψηφίου
    public Double candidateOverallScore(int candidateId) {
        String sql = """
    SELECT COALESCE(
      -- αν υπάρχουν question scores, πάρε αυτά
      (SELECT AVG(qs.score)
       FROM candidate c
       JOIN interview_report ir ON ir.id = c.interview_report_id
       JOIN step_score sr       ON sr.interview_report_id = ir.id
       JOIN question_score qs   ON qs.step_score_id = sr.id
       WHERE c.id = :candId),
      -- αλλιώς υπολόγισε από skill_score
      (SELECT AVG(ss.score)
       FROM skill_score ss
       WHERE ss.candidate_id = :candId),
      0.0
    ) AS overall
    """;
        Double v = jdbc.queryForObject(sql, Map.of("candId", candidateId), Double.class);
        return v == null ? 0.0 : v;
    }

    // Επιστρέφει λίστα με μέσο όρο βαθμολογίας ανά ερώτηση για τον υποψήφιο
    public List<QuestionScoreDto> candidateQuestionScores(int candidateId) {
        // από skill_score -> μέσος όρος ανά ερώτηση (μέσος των δεξιοτήτων της ερώτησης)
        String sqlSkills = """
    SELECT q.title AS question, AVG(ss.score) AS score
    FROM candidate c
    JOIN job_ad ja           ON ja.id = c.job_ad_id
    JOIN interview i         ON i.id = ja.interview_id
    JOIN step st             ON st.interview_id = i.id
    JOIN question q          ON q.step_id = st.id
    JOIN question_skill qsk  ON qsk.question_id = q.id
    JOIN skill_score ss      ON ss.candidate_id = c.id AND ss.skill_id = qsk.skill_id AND ss.question_id  = q.id
    WHERE c.id = :candId
    GROUP BY q.title
    ORDER BY q.title
    """;

        // αν προτιμάς τα question_score όταν υπάρχουν, τρέξε πρώτα αυτό:
        String sqlQs = """
    SELECT q.title AS question, AVG(qs.score) AS score
    FROM candidate c
    JOIN interview_report ir ON ir.id = c.interview_report_id
    JOIN step_score sr       ON sr.interview_report_id = ir.id
    JOIN question_score qs   ON qs.step_score_id = sr.id
    JOIN question q          ON q.id = qs.question_id
    WHERE c.id = :candId
    GROUP BY q.title
    ORDER BY q.title
    """;

        var fromQs = jdbc.query(sqlQs, Map.of("candId", candidateId), AnalyticsMapper.QUESTION_SCORE);
        if (!fromQs.isEmpty()) return fromQs;      // προτεραιότητα στα question_score
        return jdbc.query(sqlSkills, Map.of("candId", candidateId), AnalyticsMapper.QUESTION_SCORE);
    }

    // Επιστρέφει μέσο όρο βαθμολογίας ανά step για τον υποψήφιο
    public List<StepAvgDto> candidateStepScores(int candidateId) {
        String sql = """
    WITH q_avg AS (
      SELECT q.id AS qid, st.title AS step, AVG(ss.score) AS qscore
      FROM candidate c
      JOIN job_ad ja           ON ja.id = c.job_ad_id
      JOIN interview i         ON i.id = ja.interview_id
      JOIN step st             ON st.interview_id = i.id
      JOIN question q          ON q.step_id = st.id
      JOIN question_skill qsk  ON qsk.question_id = q.id
      JOIN skill_score ss      ON ss.candidate_id = c.id AND ss.skill_id = qsk.skill_id AND ss.question_id  = q.id
      WHERE c.id = :candId
      GROUP BY q.id, st.title
    )
    SELECT step, AVG(qscore) AS avg_score
    FROM q_avg
    GROUP BY step
    ORDER BY step
    """;
        return jdbc.query(sql, Map.of("candId", candidateId), AnalyticsMapper.STEP_AVG);
    }

    // Επιστρέφει μέσο όρο βαθμολογίας ανά skill του υποψηφίου
    public List<SkillAvgDto> candidateSkillScores(int candidateId) {
        String sql = """
    SELECT s.title AS skill, AVG(ss.score) AS avg_score
    FROM skill_score ss
    JOIN skill s ON s.id = ss.skill_id
    WHERE ss.candidate_id = :candId
    GROUP BY s.title
    ORDER BY s.title
    """;
        return jdbc.query(sql, Map.of("candId", candidateId), AnalyticsMapper.SKILL_AVG);
    }

    // Επιστρέφει τα N ισχυρότερα skills του υποψηφίου
    public List<SkillAvgDto> topSkillsForCandidate(int candidateId, int limit) {
        String sql = """
    SELECT s.title AS skill, AVG(ss.score) AS avg_score
    FROM skill_score ss
    JOIN skill s ON s.id = ss.skill_id
    WHERE ss.candidate_id = :candId
    GROUP BY s.title
    ORDER BY avg_score DESC
    LIMIT :limit
    """;
        return jdbc.query(sql, Map.of("candId", candidateId, "limit", limit), AnalyticsMapper.SKILL_AVG);
    }

    // Επιστρέφει τα N πιο αδύναμα skills του υποψηφίου
    public List<SkillAvgDto> weakSkillsForCandidate(int candidateId, int limit) {
        String sql = """
    SELECT s.title AS skill, AVG(ss.score) AS avg_score
    FROM skill_score ss
    JOIN skill s ON s.id = ss.skill_id
    WHERE ss.candidate_id = :candId
    GROUP BY s.title
    ORDER BY avg_score ASC
    LIMIT :limit
    """;
        return jdbc.query(sql, Map.of("candId", candidateId, "limit", limit), AnalyticsMapper.SKILL_AVG);
    }

    // Επιστρέφει λίστα υποψηφίων για συγκεκριμένο Job Ad
    public List<CandidateLiteDto> candidatesByJobAd(int jobAdId) {
        String sql = """
        SELECT *
        FROM candidate c
        WHERE c.job_ad_id = :jobAdId
        ORDER BY c.id
        """;
        return jdbc.query(sql, Map.of("jobAdId", jobAdId), AnalyticsMapper.CANDIDATE_LITE);
    }

    // Παίρνει με ασφάλεια τιμή String από ResultSet για δεδομένη στήλη, επιστρέφοντας null σε SQLException.
    private static String getStringSafe(ResultSet rs, String col) {
        try { return rs.getString(col); } catch (SQLException e) { return null; }
    }
    // Επιστρέφει την πρώτη μη-κενή/μη-λευκή συμβολοσειρά από τη λίστα τιμών.
    private static String firstNonBlank(String... vals) {
        if (vals == null) return null;
        for (String v : vals) if (v != null && !v.isBlank()) return v;
        return null;
    }
    // Συνενώνει ονοματεπώνυμο (first + last) καθαρίζοντας κενά και επιστρέφει null αν το αποτέλεσμα είναι κενό.
    private static String joinNames(String a, String b) {
        String s = ((a == null ? "" : a.trim()) + " " + (b == null ? "" : b.trim())).trim();
        return s.isBlank() ? null : s;
    }

    /* ======================  STEPS  ====================== */

    // Επιστρέφει λίστα με όλα τα steps του Job Ad, με τη σειρά τους
    public List<StepLiteDto> stepsForJobAd(int jobAdId) {
        String sql = """
        SELECT st.id, st.title
        FROM job_ad ja
        JOIN interview i ON i.id = ja.interview_id
        JOIN step st     ON st.interview_id = i.id
        WHERE ja.id = :jobAdId
        ORDER BY COALESCE(st.position, 999), st.id
    """;
        return jdbc.query(sql, Map.of("jobAdId", jobAdId), AnalyticsMapper.STEP_LITE);
    }

    // Επιστρέφει τον μέσο όρο όλων των scores του συγκεκριμένου step, για το συγκεκριμένο job ad
    public Double avgStepScoreForJobAdStep(int jobAdId, int stepId) {
        String sql = """
    WITH pairs AS (  -- όλα τα (candidate, question) του step για το συγκεκριμένο job ad
      SELECT c.id AS cand_id, q.id AS qid
      FROM job_ad ja
      JOIN candidate c ON c.job_ad_id = ja.id
      JOIN interview i ON i.id = ja.interview_id
      JOIN step st     ON st.interview_id = i.id AND st.id = :stepId
      JOIN question q  ON q.step_id = st.id
      WHERE ja.id = :jobAdId
    ),
    qs AS (          -- σκορ από question_score
      SELECT c.id AS cand_id, qs.question_id AS qid, AVG(qs.score) AS sc
      FROM candidate c
      JOIN interview_report ir ON ir.id = c.interview_report_id
      JOIN step_score sr       ON sr.interview_report_id = ir.id AND sr.step_id = :stepId
      JOIN question_score qs   ON qs.step_score_id = sr.id
      WHERE c.job_ad_id = :jobAdId
      GROUP BY c.id, qs.question_id
    ),
    ss AS (          -- σκορ από skill_score (0..100 -> 0..10)
      SELECT c.id AS cand_id, q.id AS qid, AVG(ss.score)/10.0 AS sc
      FROM candidate c
      JOIN job_ad ja   ON ja.id = c.job_ad_id
      JOIN interview i ON i.id = ja.interview_id
      JOIN step st     ON st.interview_id = i.id AND st.id = :stepId
      JOIN question q  ON q.step_id = st.id
      JOIN question_skill qsk ON qsk.question_id = q.id
      JOIN skill_score ss ON ss.candidate_id = c.id
                          AND ss.skill_id     = qsk.skill_id
                          AND ss.question_id  = q.id
      WHERE ja.id = :jobAdId
      GROUP BY c.id, q.id
    )
    SELECT COALESCE(AVG(COALESCE(qs.sc, ss.sc)), 0.0) AS avg_step
    FROM pairs p
    LEFT JOIN qs ON qs.cand_id = p.cand_id AND qs.qid = p.qid
    LEFT JOIN ss ON ss.cand_id = p.cand_id AND ss.qid = p.qid
    """;
        Double v = jdbc.queryForObject(sql, Map.of("jobAdId", jobAdId, "stepId", stepId), Double.class);
        return v == null ? 0.0 : v;
    }

    // Επιστρέφει τον μέσο όρο (για το συγκεκριμένο step) ανά υποψήφιο του job ad – για pass rate & histogram.
    public List<Double> candidateStepAverages(int jobAdId, int stepId) {
        String sql = """
        WITH qs AS (
          SELECT c.id AS cid, AVG(qs.score) AS sc
          FROM candidate c
          JOIN job_ad ja            ON ja.id = c.job_ad_id
          JOIN interview_report ir  ON ir.id = c.interview_report_id
          JOIN step_score sr        ON sr.interview_report_id = ir.id AND sr.step_id = :stepId
          JOIN question_score qs    ON qs.step_score_id = sr.id
          WHERE ja.id = :jobAdId
          GROUP BY c.id
        ),
        ss AS (
          SELECT c.id AS cid, AVG(ss.score) / 10.0 AS sc
          FROM candidate c
          JOIN job_ad ja   ON ja.id = c.job_ad_id
          JOIN interview i ON i.id = ja.interview_id
          JOIN step st     ON st.interview_id = i.id AND st.id = :stepId
          JOIN question q  ON q.step_id = st.id
          JOIN question_skill qsk ON qsk.question_id = q.id
          JOIN skill_score ss ON ss.candidate_id = c.id
                              AND ss.skill_id     = qsk.skill_id
                              AND ss.question_id  = q.id
          WHERE ja.id = :jobAdId
          GROUP BY c.id
        )
        SELECT sc FROM qs
        UNION ALL
        SELECT s.sc
        FROM ss s
        WHERE NOT EXISTS (SELECT 1 FROM qs WHERE qs.cid = s.cid)
    """;
        return jdbc.query(sql, Map.of("jobAdId", jobAdId, "stepId", stepId),
                (rs, i) -> rs.getObject(1) == null ? null : rs.getDouble(1));
    }

    // Κατάταξη των questions του step (ευκολότερη -> δυσκολότερη)
    public List<QuestionAvgDto> questionRankingByStep(int jobAdId, int stepId) {
        String sqlQs = """
        SELECT q.title AS question, AVG(qs.score) AS avg_score
        FROM job_ad ja
        JOIN interview i  ON i.id = ja.interview_id
        JOIN step st      ON st.interview_id = i.id AND st.id = :stepId
        JOIN question q   ON q.step_id = st.id
        JOIN interview_report ir ON 1=1
        JOIN step_score sr     ON sr.interview_report_id = ir.id AND sr.step_id = st.id
        JOIN candidate c       ON c.interview_report_id = ir.id AND c.job_ad_id = ja.id
        JOIN question_score qs ON qs.step_score_id = sr.id AND qs.question_id = q.id
        WHERE ja.id = :jobAdId
        GROUP BY q.title
        ORDER BY AVG(qs.score) DESC
    """;
        var fromQs = jdbc.query(sqlQs, Map.of("jobAdId", jobAdId, "stepId", stepId), AnalyticsMapper.QUESTION_AVG);
        if (!fromQs.isEmpty()) return fromQs;

        String sqlSs = """
        SELECT q.title AS question, AVG(ss.score) / 10.0 AS avg_score
        FROM job_ad ja
        JOIN interview i  ON i.id = ja.interview_id
        JOIN step st      ON st.interview_id = i.id AND st.id = :stepId
        JOIN question q   ON q.step_id = st.id
        JOIN question_skill qsk ON qsk.question_id = q.id
        JOIN candidate c  ON c.job_ad_id = ja.id
        JOIN skill_score ss ON ss.candidate_id = c.id
                           AND ss.skill_id     = qsk.skill_id
                           AND ss.question_id  = q.id
        WHERE ja.id = :jobAdId
        GROUP BY q.title
        ORDER BY AVG(ss.score) / 10.0 DESC
    """;
        return jdbc.query(sqlSs, Map.of("jobAdId", jobAdId, "stepId", stepId), AnalyticsMapper.QUESTION_AVG);
    }

    // Κατάταξη των skills του step (ευκολότερη -> δυσκολότερη)
    public List<SkillAvgDto> skillRankingByStep(int jobAdId, int stepId) {
        String sqlQs = """
        SELECT s.title AS skill, AVG(qs.score) AS avg_score
        FROM job_ad ja
        JOIN interview i  ON i.id = ja.interview_id
        JOIN step st      ON st.interview_id = i.id AND st.id = :stepId
        JOIN question q   ON q.step_id = st.id
        JOIN question_skill qsk ON qsk.question_id = q.id
        JOIN skill s      ON s.id = qsk.skill_id
        JOIN interview_report ir ON 1=1
        JOIN step_score sr     ON sr.interview_report_id = ir.id AND sr.step_id = st.id
        JOIN candidate c       ON c.interview_report_id = ir.id AND c.job_ad_id = ja.id
        JOIN question_score qs ON qs.step_score_id = sr.id AND qs.question_id = q.id
        WHERE ja.id = :jobAdId
        GROUP BY s.title
        ORDER BY AVG(qs.score) DESC
    """;
        var fromQs = jdbc.query(sqlQs, Map.of("jobAdId", jobAdId, "stepId", stepId), AnalyticsMapper.SKILL_AVG);
        if (!fromQs.isEmpty()) return fromQs;

        String sqlSs = """
        SELECT s.title AS skill, AVG(ss.score) / 10.0 AS avg_score
        FROM job_ad ja
        JOIN interview i  ON i.id = ja.interview_id
        JOIN step st      ON st.interview_id = i.id AND st.id = :stepId
        JOIN question q   ON q.step_id = st.id
        JOIN question_skill qsk ON qsk.question_id = q.id
        JOIN skill s      ON s.id = qsk.skill_id
        JOIN candidate c  ON c.job_ad_id = ja.id
        JOIN skill_score ss ON ss.candidate_id = c.id
                           AND ss.skill_id     = qsk.skill_id
                           AND ss.question_id  = q.id
        WHERE ja.id = :jobAdId
        GROUP BY s.title
        ORDER BY AVG(ss.score) / 10.0 DESC
    """;
        return jdbc.query(sqlSs, Map.of("jobAdId", jobAdId, "stepId", stepId), AnalyticsMapper.SKILL_AVG);
    }

    /* ======================  QUESTIONS  ====================== */

    // Επιστρέφει το Avg score της συγκεκριμένης ερώτησης στο context ενός job ad (0..10)
    public Double avgScoreForQuestionInJobAd(int jobAdId, int questionId) {
        String sql = """
    WITH pairs AS (
      -- Όλα τα candidate ids του job ad για την ερώτηση
      SELECT DISTINCT c.id AS cand_id
      FROM job_ad ja
      JOIN candidate c ON c.job_ad_id = ja.id
      WHERE ja.id = :jobAdId
    ),
    qs AS (
      -- QS ανά υποψήφιο για τη συγκεκριμένη ερώτηση
      SELECT c.id AS cand_id, AVG(qs.score) AS sc
      FROM candidate c
      JOIN interview_report ir ON ir.id = c.interview_report_id
      JOIN step_score sr       ON sr.interview_report_id = ir.id
      JOIN question_score qs   ON qs.step_score_id = sr.id
      WHERE c.job_ad_id = :jobAdId AND qs.question_id = :qid
      GROUP BY c.id
    ),
    ss AS (
      -- SS ανά υποψήφιο για τη συγκεκριμένη ερώτηση (0..100 -> 0..10)
      SELECT c.id AS cand_id, AVG(ss.score)/10.0 AS sc
      FROM candidate c
      JOIN job_ad ja   ON ja.id = c.job_ad_id
      JOIN question_skill qsk ON qsk.question_id = :qid
      JOIN skill_score ss ON ss.candidate_id = c.id
                         AND ss.skill_id     = qsk.skill_id
                         AND ss.question_id  = :qid
      WHERE ja.id = :jobAdId
      GROUP BY c.id
    )
    SELECT COALESCE(AVG(COALESCE(qs.sc, ss.sc)), 0.0) AS avg_q
    FROM pairs p
    LEFT JOIN qs ON qs.cand_id = p.cand_id
    LEFT JOIN ss ON ss.cand_id = p.cand_id
    """;
        Double v = jdbc.queryForObject(sql, Map.of("jobAdId", jobAdId, "qid", questionId), Double.class);
        return v == null ? 0.0 : v;
    }

    // Επιστρέφει τον μέσο όρο της ερώτησης ανά υποψήφιο (για pass rate & histogram)
    public List<Double> candidateQuestionAverages(int jobAdId, int questionId) {
        String sql = """
    WITH pairs AS (
      SELECT DISTINCT c.id AS cand_id
      FROM job_ad ja
      JOIN candidate c ON c.job_ad_id = ja.id
      WHERE ja.id = :jobAdId
    ),
    qs AS (
      SELECT c.id AS cand_id, AVG(qs.score) AS sc
      FROM candidate c
      JOIN interview_report ir ON ir.id = c.interview_report_id
      JOIN step_score sr       ON sr.interview_report_id = ir.id
      JOIN question_score qs   ON qs.step_score_id = sr.id
      WHERE c.job_ad_id = :jobAdId AND qs.question_id = :qid
      GROUP BY c.id
    ),
    ss AS (
      SELECT c.id AS cand_id, AVG(ss.score)/10.0 AS sc
      FROM candidate c
      JOIN job_ad ja   ON ja.id = c.job_ad_id
      JOIN question_skill qsk ON qsk.question_id = :qid
      JOIN skill_score ss ON ss.candidate_id = c.id
                         AND ss.skill_id     = qsk.skill_id
                         AND ss.question_id  = :qid
      WHERE ja.id = :jobAdId
      GROUP BY c.id
    )
    SELECT COALESCE(qs.sc, ss.sc) AS cand_avg
    FROM pairs p
    LEFT JOIN qs ON qs.cand_id = p.cand_id
    LEFT JOIN ss ON ss.cand_id = p.cand_id
    """;
        return jdbc.query(sql, Map.of("jobAdId", jobAdId, "qid", questionId),
                (rs, i) -> rs.getObject(1) == null ? null : rs.getDouble(1));
    }

    // Επιστρέφει τον μέσο όρο ανά skill για τη συγκεκριμένη ερώτηση μέσα στο job ad
    public List<SkillAvgDto> skillAveragesForQuestion(int jobAdId, int questionId) {
        String sql = """
    SELECT s.title AS skill, AVG(ss.score)/10.0 AS avg_score
    FROM job_ad ja
    JOIN candidate c        ON c.job_ad_id = ja.id
    JOIN question_skill qsk ON qsk.question_id = :qid
    JOIN skill s            ON s.id = qsk.skill_id
    JOIN skill_score ss     ON ss.candidate_id = c.id
                           AND ss.skill_id     = qsk.skill_id
                           AND ss.question_id  = :qid
    WHERE ja.id = :jobAdId
    GROUP BY s.title
    ORDER BY AVG(ss.score)/10.0 DESC
    """;
        return jdbc.query(sql, Map.of("jobAdId", jobAdId, "qid", questionId), AnalyticsMapper.SKILL_AVG);
    }

    // Επιστρέφει τις ερωτήσεις που βαθμολογήθηκαν για το συγκεκριμένο JobAd+Step
    public List<QuestionLiteDto> questionsForJobAdStep(int jobAdId, int stepId) {
        String sql = """
    SELECT q.id, q.title
    FROM job_ad ja
    JOIN interview i ON i.id = ja.interview_id
    JOIN step st     ON st.interview_id = i.id AND st.id = :stepId
    JOIN question q  ON q.step_id = st.id
    WHERE ja.id = :jobAdId
    ORDER BY COALESCE(q.position, 999), q.id
    """;
        return jdbc.query(sql, Map.of("jobAdId", jobAdId, "stepId", stepId), AnalyticsMapper.QUESTION_LITE);
    }

    // Επιστρέφει λίστα δεξιοτήτων που είναι συνδεδεμένες με μια ερώτηση
    public List<SkillLiteDto> skillsForQuestion(int questionId) {
        String sql = """
    SELECT s.id, s.title
    FROM question_skill qsk
    JOIN skill s ON s.id = qsk.skill_id
    WHERE qsk.question_id = :qid
    ORDER BY s.title
    """;
        return jdbc.query(sql, Map.of("qid", questionId), AnalyticsMapper.SKILL_LITE);
    }

    /* ======================  SKILLS  ====================== */

    // Επιστρέφει τον μέσο όρο του σκορ της δεξιότητας σε όλο το dataset (όλα τα question scores που την αφορούν)
    public Double avgScoreForSkill(int skillId) {
        String sql = """
    SELECT COALESCE(AVG(x.sc), 0.0) AS avg_skill
    FROM (
        -- QS που αφορούν τη δεξιότητα
        SELECT qs.score AS sc
        FROM question_score qs
        JOIN step_score sr        ON sr.id = qs.step_score_id
        JOIN interview_report ir  ON ir.id = sr.interview_report_id
        JOIN candidate c          ON c.interview_report_id = ir.id
        JOIN question_skill k     ON k.question_id = qs.question_id
        WHERE k.skill_id = :sid

        UNION ALL

        -- SS/10 ΜΟΝΟ όταν ΔΕΝ υπάρχει QS για το ίδιο (candidate, question)
        SELECT ss.score / 10.0 AS sc
        FROM skill_score ss
        JOIN question_skill k ON k.question_id = ss.question_id
        WHERE k.skill_id = :sid
          AND NOT EXISTS (
              SELECT 1
              FROM question_score qs
              JOIN step_score sr        ON sr.id = qs.step_score_id
              JOIN interview_report ir  ON ir.id = sr.interview_report_id
              JOIN candidate c          ON c.interview_report_id = ir.id
              WHERE qs.question_id = ss.question_id
                AND c.id          = ss.candidate_id
          )
    ) x
    """;
        Double v = jdbc.queryForObject(sql, Map.of("sid", skillId), Double.class);
        return v == null ? 0.0 : v;
    }

    // Επιστρέφει τον μέσο όρο της δεξιότητας ανά υποψήφιο (για pass rate & histogram)
    public java.util.List<Double> candidateSkillAverages(int skillId) {
        String sql = """
    WITH unioned AS (
        -- QS για το skill, με (candidate, question)
        SELECT c.id AS cid, qs.question_id AS qid, qs.score AS sc
        FROM question_score qs
        JOIN step_score sr        ON sr.id = qs.step_score_id
        JOIN interview_report ir  ON ir.id = sr.interview_report_id
        JOIN candidate c          ON c.interview_report_id = ir.id
        JOIN question_skill k     ON k.question_id = qs.question_id
        WHERE k.skill_id = :sid

        UNION ALL

        -- SS/10 ΜΟΝΟ όταν δεν υπάρχει QS για το ίδιο (cid,qid)
        SELECT ss.candidate_id AS cid, ss.question_id AS qid, ss.score/10.0 AS sc
        FROM skill_score ss
        JOIN question_skill k ON k.question_id = ss.question_id
        WHERE k.skill_id = :sid
          AND NOT EXISTS (
              SELECT 1
              FROM question_score qs
              JOIN step_score sr        ON sr.id = qs.step_score_id
              JOIN interview_report ir  ON ir.id = sr.interview_report_id
              JOIN candidate c          ON c.interview_report_id = ir.id
              WHERE qs.question_id = ss.question_id
                AND c.id          = ss.candidate_id
          )
    )
    SELECT AVG(sc) AS avg_per_candidate
    FROM unioned
    GROUP BY cid
    """;
        return jdbc.query(sql, Map.of("sid", skillId),
                (rs, i) -> rs.getObject(1) == null ? null : rs.getDouble(1));
    }

    // Μετρά προσλήψεις (Hired) ανά department
    public long countHiresByDept(int deptId) {
        String sql = """
        SELECT COUNT(DISTINCT c.id)
        FROM candidate c
        JOIN job_ad ja         ON c.job_ad_id = ja.id
        JOIN jobad_department jd ON ja.id = jd.jobad_id
        WHERE jd.department_id = :deptId
          AND c.status = 'Hired'
        """;
        Long n = jdbc.queryForObject(sql, Map.of("deptId", deptId), Long.class);
        return n == null ? 0L : n;
    }
}

