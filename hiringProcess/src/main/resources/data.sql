-- === ORGANISATION ===
INSERT INTO organisation (id, name, description) VALUES (3, 'TechCorp', 'A global tech company');

-- === DEPARTMENT ===
INSERT INTO department (id, name, location, description, organisation_id) VALUES
(4, 'Engineering', 'Athens', 'Handles product dev', 3),
(5, 'HR', 'Thessaloniki', 'Manages HR stuff', 3),
(6, 'Data Science', 'Patra', 'Data and AI department', 3);

-- === OCCUPATION ===
INSERT INTO occupation (id, title, esco_id) VALUES
(6, 'Software Engineer', 'esco_1234'),
(7, 'HR Specialist', 'esco_5678'),
(8, 'Data Analyst', 'esco_9012'),
(9, 'Frontend Developer', 'esco_3456'),
(10, 'Recruiter', 'esco_7890'),
(11, 'Data Engineer', 'esco_1122'),
(12, 'DevOps Engineer', 'esco_3344'),
(13, 'Training Coordinator', 'esco_5566'),
(14, 'Machine Learning Engineer', 'esco_7788');

-- === INTERVIEW ===
INSERT INTO interview (id, title, description) VALUES
(7, 'Backend Interview', 'Spring Boot and APIs'),
(8, 'HR Interview', 'HR processes'),
(9, 'Data Interview', 'SQL, Python'),
(10, 'Frontend Interview', 'React and UI design'),
(11, 'Recruitment Interview', 'Hiring & sourcing'),
(12, 'Data Eng. Interview', 'ETL pipelines & DBs'),
(13, 'DevOps Interview', 'CI/CD, Kubernetes'),
(14, 'Training Interview', 'Internal training processes'),
(15, 'ML Interview', 'ML models & deployment'),
(16, 'Senior Backend Interview', 'Advanced Java and architecture'),
(17, 'HR Assistant Interview', 'Supportive HR operations'),
(18, 'Junior Data Interview', 'Basic analytics and Excel'),
(19, 'Frontend Intern Interview', 'React basics and growth'),
(20, 'Freelance Recruiter Interview', 'Remote sourcing case study'),
(21, 'ETL Specialist Interview', 'Data cleaning and pipelines'),
(22, 'SRE Interview', 'Site reliability scenarios'),
(23, 'L&D Interview', 'Learning strategy alignment'),
(24, 'AI Research Interview', 'Advanced ML knowledge');

-- === JOB_AD ===
INSERT INTO job_ad (id, title, description, publish_date, status, occupation_id, interview_id) VALUES
(8, 'Backend Developer', 'We are hiring a backend Java developer', '2025-08-01', 'Published', 6, 7),
(9, 'HR Specialist', 'Join our HR department', '2025-08-01', 'Published', 7, 8),
(10, 'Data Analyst', 'Hiring data analyst with SQL skills', '2025-08-06', 'Published', 8, 9),
(11, 'Frontend Developer', 'Seeking React developer for dynamic team', '2025-08-06', 'Published', 9, 10),
(12, 'Recruiter', 'Looking for tech recruiter', '2025-08-06', 'Published', 10, 11),
(13, 'Data Engineer', 'Build data pipelines and manage ETL', '2025-08-06', 'Published', 11, 12),
(14, 'DevOps Engineer', 'Manage CI/CD pipelines and infrastructure', '2025-08-06', 'Published', 12, 13),
(15, 'Training Coordinator', 'Plan and manage employee training', '2025-08-06', 'Published', 13, 14),
(16, 'ML Engineer', 'Develop and deploy machine learning models', '2025-08-06', 'Published', 14, 15),
(17, 'Senior Backend Engineer', 'Lead our backend team with Java expertise', '2025-06-15', 'Complete', 6, 16),
(18, 'HR Operations Assistant', 'Assist in HR operations and tasks', '2025-07-10', 'Pending', 7, 17),
(19, 'Junior Data Analyst', 'Assist with data cleansing and reporting', '2025-06-20', 'Complete', 8, 18),
(20, 'React Developer (Intern)', 'Frontend internship with mentorship', '2025-07-01', 'Pending', 9, 19),
(21, 'Recruiter (Freelance)', 'Remote recruiter needed short-term', '2025-06-25', 'Complete', 10, 20),
(22, 'ETL Specialist', 'Build automated data pipelines', '2025-07-15', 'Pending', 11, 21),
(23, 'Site Reliability Engineer', 'DevOps for global-scale apps', '2025-06-10', 'Complete', 12, 22),
(24, 'L&D Coordinator', 'Support learning & development programs', '2025-07-20', 'Pending', 13, 23),
(25, 'AI Research Engineer', 'Work on cutting-edge ML solutions', '2025-06-05', 'Complete', 14, 24);

-- === jobad_department ===
INSERT INTO jobad_department (jobad_id, department_id) VALUES
(8, 4), (9, 5), (10, 6),
(11, 4), (12, 5), (13, 6),
(14, 4), (15, 5), (16, 6),
(17, 4), (18, 5), (19, 6),
(20, 4), (21, 5), (22, 6),
(23, 4), (24, 5), (25, 6);

-- === INTERVIEW_REPORT ===
INSERT INTO interview_report (id, interview_id) VALUES (9, 7);

-- === CANDIDATE ===
INSERT INTO candidate (id, first_name, last_name, email, info, status, comments, job_ad_id, interview_report_id)
VALUES (10, 'John', 'Doe', 'john@example.com', 'Experienced dev', 'Accepted', 'Strong coding skills', 8, 9);

-- === STEP ===
INSERT INTO step (id, title, description, interview_id, position, score) VALUES
(11, 'Technical', 'Java & Spring Boot', 7, 0, 0),
(12, 'HR Round', 'Soft skills & culture fit', 7, 1, 0),
(13, 'HR Policies', 'HR processes & labor law basics', 8, 0, 0),
(14, 'Culture Fit', 'Values and teamwork', 8, 1, 0),
(15, 'SQL & Statistics', 'Joins, window functions, hypothesis testing', 9, 0, 0),
(16, 'Python/Analytics Case', 'Pandas/numpy case exercise', 9, 1, 0);

-- === QUESTION ===
INSERT INTO question (id, title, description, step_id) VALUES
(13, 'What is a HashMap?', 'Explain usage and performance', 11),
(14, 'SOLID Principles', 'Define the 5 principles', 11),
(15, 'Conflict Resolution', 'How do you resolve team conflicts?', 12);

-- === SKILL ===
INSERT INTO skill (id, title, esco_id, score) VALUES
(16, 'Java', 'java001', 0),
(17, 'OOP Principles', 'oop002', 0),
(18, 'SQL', 'sql999', 0),
(19, 'Python Programming', 'py888', 0),
(20, 'ReactJS Framework', 'react777', 0),
(21, 'Continuous Integration & Deployment', 'cicd666', 0),
(22, 'Container Orchestration', 'k8s555', 0),
(23, 'Spreadsheet Software', 'excel444', 0),
(24, 'Collaboration and Teamwork', 'team333', 0),
(25, 'Talent Acquisition Skills', 'recruit222', 0),
(26, 'Data Preprocessing', 'clean111', 0),
(27, 'Artificial Intelligence', 'ai000', 0);

-- === question_skill ===
INSERT INTO question_skill (question_id, skill_id) VALUES
(13, 16),
(14, 17);

-- === STEP_RESULTS ===
INSERT INTO step_results (id, step_id, interview_report_id) VALUES
(18, 11, 9),
(19, 12, 9);

-- === QUESTION_SCORE ===
INSERT INTO question_score (id, question_id, step_results_id, score) VALUES
(20, 13, 18, 9.0),
(21, 14, 18, 8.5),
(22, 15, 19, 7.0);

-- === jobad_skill ===
INSERT INTO jobad_skill (jobad_id, skill_id) VALUES
(8, 16), (8, 17),
(9, 24), (9, 25),
(10, 17), (10, 18), (10, 23), (10, 26),
(11, 20), (11, 17),
(12, 25), (12, 24),
(13, 16), (13, 18), (13, 26),
(14, 16), (14, 21), (14, 22),
(15, 24), (15, 23),
(16, 16), (16, 17), (16, 18), (16, 27),
(17, 16), (17, 17), (17, 21),
(18, 23), (18, 24), (18, 25),
(19, 23), (19, 26), (19, 18),
(20, 20), (20, 17),
(21, 25), (21, 24),
(22, 26), (22, 18), (22, 16),
(23, 21), (23, 22), (23, 16),
(24, 23), (24, 24),
(25, 27), (25, 18), (25, 16), (25, 17);

