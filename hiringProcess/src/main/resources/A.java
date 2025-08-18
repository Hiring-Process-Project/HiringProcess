

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
-- Προσοχή: η στήλη είναι 'tittle' (όχι 'title') όπως στο schema σου.
INSERT INTO job_ad (id, tittle, description, publish_date, status, occupation_id, interview_id) VALUES
-- Published
(8, 'Backend Developer', 'We are hiring a backend Java developer', '2025-08-01', 'Published', 6, 7),
(9, 'HR Specialist', 'Join our HR department', '2025-08-01', 'Published', 7, 8),
(10, 'Data Analyst', 'Hiring data analyst with SQL skills', '2025-08-06', 'Published', 8, 9),
(11, 'Frontend Developer', 'Seeking React developer for dynamic team', '2025-08-06', 'Published', 9, 10),
(12, 'Recruiter', 'Looking for tech recruiter', '2025-08-06', 'Published', 10, 11),
(13, 'Data Engineer', 'Build data pipelines and manage ETL', '2025-08-06', 'Published', 11, 12),
(14, 'DevOps Engineer', 'Manage CI/CD pipelines and infrastructure', '2025-08-06', 'Published', 12, 13),
(15, 'Training Coordinator', 'Plan and manage employee training', '2025-08-06', 'Published', 13, 14),
(16, 'ML Engineer', 'Develop and deploy machine learning models', '2025-08-06', 'Published', 14, 15),

-- Complete / Pending
(17, 'Senior Backend Engineer', 'Lead our backend team with Java expertise', '2025-06-15', 'Complete', 6, 16),
(18, 'HR Operations Assistant', 'Assist in HR operations and tasks', '2025-07-10', 'Pending', 7, 17),
(19, 'Junior Data Analyst', 'Assist with data cleansing and reporting', '2025-06-20', 'Complete', 8, 18),
(20, 'React Developer (Intern)', 'Frontend internship with mentorship', '2025-07-01', 'Pending', 9, 19),
(21, 'Recruiter (Freelance)', 'Remote recruiter needed short-term', '2025-06-25', 'Complete', 10, 20),
(22, 'ETL Specialist', 'Build automated data pipelines', '2025-07-15', 'Pending', 11, 21),
(23, 'Site Reliability Engineer', 'DevOps for global-scale apps', '2025-06-10', 'Complete', 12, 22),
(24, 'L&D Coordinator', 'Support learning & development programs', '2025-07-20', 'Pending', 13, 23),
(25, 'AI Research Engineer', 'Work on cutting-edge ML solutions', '2025-06-05', 'Complete', 14, 24);

-- === jobad_department (join table) ===
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
INSERT INTO candidate (id, first_name, last_name, email, info, decision, reasoning, job_ad_id, interview_report_id) VALUES
(10, 'John', 'Doe', 'john@example.com', 'Experienced dev', 'Accepted', 'Strong coding skills', 8, 9);

-- === STEP ===
-- ΔΙΟΡΘΩΣΗ: Προστέθηκε η στήλη score με τιμή 0 σε όλα τα rows.
INSERT INTO step (id, title, description, interview_id, position, score) VALUES
(11, 'Technical', 'Java & Spring Boot', 7, 0, 0),
(12, 'HR Round', 'Soft skills & culture fit', 7, 1, 0),
(13, 'HR Policies', 'HR processes & labor law basics', 8, 0, 0),
(14, 'Culture Fit', 'Values and teamwork', 8, 1, 0),
(15, 'SQL & Statistics', 'Joins, window functions, hypothesis testing', 9, 0, 0),
(16, 'Python/Analytics Case', 'Pandas/numpy case exercise', 9, 1, 0),
(17, 'React & JS', 'Components, state, hooks', 10, 0, 0),
(18, 'UI/UX Discussion', 'Accessibility & responsive design', 10, 1, 0),
(19, 'Sourcing Strategy', 'Channels, outreach & funnels', 11, 0, 0),
(20, 'Stakeholder Management', 'Collaboration with hiring managers', 11, 1, 0),
(21, 'ETL Design', 'Pipelines & data quality checks', 12, 0, 0),
(22, 'Data Modeling', 'Warehouse patterns & partitioning', 12, 1, 0),
(23, 'CI/CD Deep Dive', 'Pipelines, testing & release', 13, 0, 0),
(24, 'Kubernetes & Infra', 'Containers, k8s, networking', 13, 1, 0),
(25, 'Needs Analysis', 'Identify training needs & gaps', 14, 0, 0),
(26, 'Program Design', 'Learning paths, delivery & KPIs', 14, 1, 0),
(27, 'ML Fundamentals', 'Algorithms, metrics & bias', 15, 0, 0),
(28, 'Deployment/MLOps', 'Serving, monitoring & drift', 15, 1, 0),
(29, 'System Design', 'Scalability, consistency & trade-offs', 16, 0, 0),
(30, 'Code Review', 'Best practices & refactoring', 16, 1, 0),
(31, 'Admin Processes', 'Onboarding & records management', 17, 0, 0),
(32, 'Communication', 'Conflict resolution & empathy', 17, 1, 0),
(33, 'Excel & Basics', 'Formulas, charts, pivots', 18, 0, 0),
(34, 'SQL Basics', 'SELECTs, joins, grouping', 18, 1, 0),
(35, 'JS Basics', 'ES6, DOM & events', 19, 0, 0),
(36, 'Portfolio/Exercises', 'Small tasks & feedback', 19, 1, 0),
(37, 'Pipeline Strategy', 'Sourcing remote candidates', 20, 0, 0),
(38, 'Case Study', 'Scenario-based assessment', 20, 1, 0),
(39, 'Data Cleaning', 'Transforms, mapping & validation', 21, 0, 0),
(40, 'Orchestration', 'Schedulers (Airflow etc.)', 21, 1, 0),
(41, 'Incident Response', 'On-call, runbooks & postmortems', 22, 0, 0),
(42, 'Reliability/Monitoring', 'SLIs/SLOs & observability', 22, 1, 0),
(43, 'Learning Strategy', 'L&D roadmap & stakeholders', 23, 0, 0),
(44, 'Content Creation', 'Workshops & materials', 23, 1, 0),
(45, 'Research Methods', 'Papers, novelty & rigor', 24, 0, 0),
(46, 'Model Evaluation', 'Benchmarks & ablations', 24, 1, 0);

-- === QUESTION ===
INSERT INTO question (id, name, description, step_id) VALUES
(13, 'What is a HashMap?', 'Explain usage and performance', 11),
(14, 'SOLID Principles', 'Define the 5 principles', 11),
(15, 'Conflict Resolution', 'How do you resolve team conflicts?', 12);

-- === SKILL (χωρίς question_id) ===
INSERT INTO skill (id, name, title, esco_id) VALUES
(16, 'Java', 'Java Language', 'java123'),
(17, 'OOP', 'Object-Oriented Programming', 'oop456');

-- === question_skill (συσχέτιση) ===
INSERT INTO question_skill (question_id, skill_id) VALUES
(13, 16), -- Q13 -> Java
(14, 17); -- Q14 -> OOP

-- === STEP_RESULTS ===
INSERT INTO step_results (id, step_id, interview_report_id) VALUES
(18, 11, 9),
(19, 12, 9);

-- === QUESTION_SCORE ===
INSERT INTO question_score (id, question_id, step_results_id, score) VALUES
(20, 13, 18, 9.0),
(21, 14, 18, 8.5),
(22, 15, 19, 7.0);

-- === Νέα SKILLS (χωρίς question_id) ===
INSERT INTO skill (id, name, title, esco_id) VALUES
(18, 'SQL', 'Structured Query Language', 'sql999'),
(19, 'Python', 'Python Programming', 'py888'),
(20, 'React', 'ReactJS Framework', 'react777'),
(21, 'CI/CD', 'Continuous Integration & Deployment', 'cicd666'),
(22, 'Kubernetes', 'Container Orchestration', 'k8s555'),
(23, 'Excel', 'Spreadsheet Software', 'excel444'),
(24, 'Teamwork', 'Collaboration and Teamwork', 'team333'),
(25, 'Recruiting', 'Talent Acquisition Skills', 'recruit222'),
(26, 'Data Cleaning', 'Data Preprocessing', 'clean111'),
(27, 'AI', 'Artificial Intelligence', 'ai000');

-- === jobad_skill (join table) ===
INSERT INTO jobad_skill (jobad_id, skill_id) VALUES
-- Backend Developer
(8, 16), (8, 17),

-- HR Specialist
(9, 24), (9, 25),

-- Data Analyst
(10, 17), (10, 18), (10, 23), (10, 26),

-- Frontend Developer
(11, 20), (11, 17),

-- Recruiter
(12, 25), (12, 24),

-- Data Engineer
(13, 16), (13, 18), (13, 26),

-- DevOps Engineer
(14, 16), (14, 21), (14, 22),

-- Training Coordinator
(15, 24), (15, 23),

-- ML Engineer
(16, 16), (16, 17), (16, 18), (16, 27),

-- Senior Backend Engineer
(17, 16), (17, 17), (17, 21),

-- HR Operations Assistant
(18, 23), (18, 24), (18, 25),

-- Junior Data Analyst
(19, 23), (19, 26), (19, 18),

-- React Developer (Intern)
(20, 20), (20, 17),

-- Recruiter (Freelance)
(21, 25), (21, 24),

-- ETL Specialist
(22, 26), (22, 18), (22, 16),

-- Site Reliability Engineer
(23, 21), (23, 22), (23, 16),

-- L&D Coordinator
(24, 23), (24, 24),

-- AI Research Engineer
(25, 27), (25, 18), (25, 16), (25, 17);
