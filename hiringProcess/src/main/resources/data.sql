-- === ORGANISATION ===
INSERT INTO organisation (id, name, description) VALUES (3, 'TechCorp', 'A global tech company');

-- === DEPARTMENT ===
INSERT INTO department (id, name, location, description, organisation_id) VALUES
(4, 'Engineering', 'Athens', 'Handles product dev', 3),
(5, 'HR', 'Thessaloniki', 'Manages HR stuff', 3);

-- === OCCUPATION ===
INSERT INTO occupation (id, title, esco_id) VALUES
(6, 'Software Engineer', 'esco_1234');

-- === INTERVIEW ===
INSERT INTO interview (id, title, description) VALUES
(7, 'Backend Interview', 'Spring Boot and APIs');

-- === JOB_AD ===
INSERT INTO job_ad (id, tittle, description, publish_date, status, occupation_id, interview_id) VALUES
(8, 'Backend', 'We are hiring a backend Java developer', '2025-08-01', 'Open', 6, 7);

-- === jobad_department (join table) ===
INSERT INTO jobad_department (jobad_id, department_id) VALUES (8, 4), (8, 5);

-- === INTERVIEW_REPORT ===
INSERT INTO interview_report (id, interview_id) VALUES (9, 7);

-- === CANDIDATE ===
INSERT INTO candidate (id, first_name, last_name, email, info, decision, reasoning, job_ad_id, interview_report_id) VALUES
(10, 'John', 'Doe', 'john@example.com', 'Experienced dev', 'Accepted', 'Strong coding skills', 8, 9);

-- === STEP ===
INSERT INTO step (id, title, description, interview_id) VALUES
(11, 'Technical', 'Java & Spring Boot', 7),
(12, 'HR Round', 'Soft skills & culture fit', 7);

-- === QUESTION ===
INSERT INTO question (id, name, description, step_id) VALUES
(13, 'What is a HashMap?', 'Explain usage and performance', 11),
(14, 'SOLID Principles', 'Define the 5 principles', 11),
(15, 'Conflict Resolution', 'How do you resolve team conflicts?', 12);

-- === SKILL ===
INSERT INTO skill (id, name, title, esco_id, question_id) VALUES
(16, 'Java', 'Java Language', 'java123', 13),
(17, 'OOP', 'Object-Oriented Programming', 'oop456', 14);

-- === STEP_RESULTS ===
INSERT INTO step_results (id, step_id, interview_report_id) VALUES
(18, 11, 9),
(19, 12, 9);

-- === QUESTION_SCORE ===
INSERT INTO question_score (id, question_id, step_results_id, score) VALUES
(20, 13, 18, 9.0),
(21, 14, 18, 8.5),
(22, 15, 19, 7.0);
