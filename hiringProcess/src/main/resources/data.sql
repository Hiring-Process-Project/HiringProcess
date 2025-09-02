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
INSERT INTO interview_report (id, interview_id) VALUES
(9, 7),
(10, 7),
(11, 7),
(12, 8),
(13, 8),
(14, 9),
(15, 9),
(16, 10),
(17, 10),
(18, 11),
(19, 11),
(20, 12),
(21, 12),
(22, 13),
(23, 13),
(24, 14),
(25, 14),
(26, 15),
(27, 15),
(28, 16),
(29, 16),
(30, 17),
(31, 17),
(32, 18),
(33, 18),
(34, 19),
(35, 19),
(36, 20),
(37, 20),
(38, 21),
(39, 21),
(40, 22),
(42, 22),
(43, 23),
(44, 23),
(45, 24),
(46, 24);

-- === CANDIDATE ===
INSERT INTO candidate (id, first_name, last_name, email, info, status, comments, job_ad_id, interview_report_id)
VALUES
(10, 'John', 'Doe', 'john@example.com', 'Experienced dev', 'Approved', 'Strong coding skills', 8, 9),
-- JobAd 8: Backend Developer
(11, 'Alice', 'Smith', 'alice.smith@example.com', 'Backend dev with 3y exp', 'Pending', 'Good Spring Boot knowledge', 8, 10),
(12, 'Bob', 'Johnson', 'bob.j@example.com', 'Fullstack dev, prefers backend', 'Rejected', 'Weak in SQL', 8, 11),

-- JobAd 9: HR Specialist
(13, 'Maria', 'Papadopoulou', 'maria.p@example.com', 'HR MSc, 2y exp', 'Approved', 'Strong interpersonal skills', 9, 12),
(14, 'Nikos', 'Karas', 'nikos.kara@example.com', 'Recent HR graduate', 'Pending', 'Enthusiastic but junior', 9, 13),

-- JobAd 10: Data Analyst
(15, 'Elena', 'Kostas', 'elena.k@example.com', 'Data analyst, 4y exp in finance', 'Approved', 'SQL expert', 10, 14),
(16, 'George', 'Liakos', 'george.l@example.com', 'Statistician, 1y exp', 'Rejected', 'Struggled in Python', 10, 15),

-- JobAd 11: Frontend Developer
(17, 'Sophia', 'Andreou', 'sophia.a@example.com', 'Frontend dev, 2y React', 'Pending', 'Good UI eye', 11, 16),
(18, 'Panagiotis', 'Dimitriou', 'panos.d@example.com', 'Frontend intern', 'Rejected', 'Weak JavaScript fundamentals', 11,17),

-- JobAd 12: Recruiter
(19, 'Helen', 'Markou', 'helen.m@example.com', 'Recruiter, 5y exp', 'Approved', 'Great sourcing track record', 12, 18),
(20, 'Christos', 'Zafeiris', 'christos.z@example.com', 'HR recruiter, 1y exp', 'Pending', 'Needs mentoring', 12, 19),

-- JobAd 13: Data Engineer
(21, 'Ioanna', 'Petrou', 'ioanna.p@example.com', 'Data engineer, 3y ETL exp', 'Approved', 'Strong in pipelines', 13, 20),
(22, 'Dimitris', 'Alexiou', 'dimitris.a@example.com', 'DBA turned data engineer', 'Rejected', 'Weak in Spark', 13, 21),

-- JobAd 14: DevOps Engineer
(23, 'Giannis', 'Rallis', 'giannis.r@example.com', 'DevOps 4y exp', 'Approved', 'Solid Kubernetes skills', 14, 22),
(24, 'Katerina', 'Sotiropoulou', 'katerina.s@example.com', 'Sysadmin migrating to DevOps', 'Pending', 'Good CI/CD basics', 14, 23),

-- JobAd 15: Training Coordinator
(25, 'Anna', 'Georgiou', 'anna.g@example.com', 'Trainer, 6y exp', 'Approved', 'Strong coordination skills', 15, 24),
(26, 'Vasileios', 'Nikou', 'vasilis.n@example.com', 'Training assistant, 1y exp', 'Pending', 'Shows potential', 15, 25),

-- JobAd 16: ML Engineer
(27, 'Eleni', 'Pappa', 'eleni.p@example.com', 'ML engineer, 2y exp', 'Approved', 'Good ML model deployment', 16, 26),
(28, 'Stavros', 'Michailidis', 'stavros.m@example.com', 'Data scientist transitioning to ML', 'Rejected', 'Weak coding practices', 16, 27),

-- JobAd 17: Senior Backend Engineer
(29, 'Petros', 'Anagnostou', 'petros.a@example.com', 'Senior Java dev, 10y exp', 'Pending', 'Architectural mindset', 17, 28),
(30, 'Despina', 'Lazarou', 'despina.l@example.com', 'Backend lead, 7y exp', 'Pending', 'Strong but prefers Python', 17, 29),

-- JobAd 18: HR Operations Assistant
(31, 'Olga', 'Mantzou', 'olga.m@example.com', 'HR ops assistant, 2y exp', 'Approved', 'Process oriented', 18, 30),
(32, 'Thanasis', 'Vergis', 'thanasis.v@example.com', 'Business admin graduate', 'Rejected', 'Limited HR knowledge', 18, 31),

-- JobAd 19: Junior Data Analyst
(33, 'Kalliopi', 'Xenou', 'kalliopi.x@example.com', 'Math graduate', 'Pending', 'Strong statistics, weak SQL', 19, 32),
(34, 'Leonidas', 'Fotiou', 'leonidas.f@example.com', 'Economics graduate', 'Approved', 'Excel wizard', 19, 33),

-- JobAd 20: React Developer (Intern)
(35, 'Georgia', 'Alexi', 'georgia.a@example.com', 'CS student', 'Pending', 'React basics covered', 20, 34),
(36, 'Marios', 'Spanos', 'marios.s@example.com', 'Self-taught React dev', 'Rejected', 'Needs mentoring in Git', 20, 35),

-- JobAd 21: Recruiter (Freelance)
(37, 'Eftychia', 'Marinou', 'eftychia.m@example.com', 'Freelance recruiter, 3y exp', 'Approved', 'Independent worker', 21, 36),
(38, 'Kostas', 'Chatzis', 'kostas.c@example.com', 'Remote HR recruiter', 'Pending', 'Needs better sourcing tools', 21, 37),

-- JobAd 22: ETL Specialist
(39, 'Natalia', 'Karagianni', 'natalia.k@example.com', 'ETL specialist, 4y exp', 'Approved', 'Excellent SQL pipelines', 22, 38),
(40, 'Michalis', 'Arvanitis', 'michalis.a@example.com', 'Data analyst moving to ETL', 'Rejected', 'Struggled in automation', 22, 39),

-- JobAd 23: Site Reliability Engineer
(41, 'Christina', 'Drosou', 'christina.d@example.com', 'SRE 5y exp', 'Approved', 'Solid incident management', 23, 40),
(42, 'Dionysis', 'Panou', 'dionysis.p@example.com', 'Junior DevOps', 'Pending', 'Needs on-call experience', 23, 42),

-- JobAd 24: L&D Coordinator
(43, 'Ioannis', 'Tzanos', 'ioannis.t@example.com', 'Trainer, 3y exp', 'Pending', 'Good program design', 24, 43),
(44, 'Sofia', 'Ntouka', 'sofia.n@example.com', 'L&D admin', 'Approved', 'Strong organization skills', 24, 44),

-- JobAd 25: AI Research Engineer
(45, 'Markos', 'Filippou', 'markos.f@example.com', 'PhD ML researcher', 'Approved', 'Deep learning expert', 25, 45),
(46, 'Irini', 'Kallergi', 'irini.k@example.com', 'AI MSc student', 'Rejected', 'Too junior for role', 25, 46);


-- === STEP ===
INSERT INTO step (id, title, description, interview_id, position, score) VALUES
(11, 'Technical', 'Java & Spring Boot', 7, 0, 0),
(12, 'HR Round', 'Soft skills & culture fit', 7, 1, 0),
(13, 'HR Policies', 'HR processes & labor law basics', 8, 0, 0),
(14, 'Culture Fit', 'Values and teamwork', 8, 1, 0),
(15, 'SQL & Statistics', 'Joins, window functions, hypothesis testing', 9, 0, 0),
(16, 'Python/Analytics Case', 'Pandas/numpy case exercise', 9, 1, 0),
(17, 'System Design', 'Scalability, caching, trade-offs', 7, 2, 0),
(18, 'Case Scenarios', 'Practical HR scenarios & decisions', 8, 2, 0),
(19, 'Data Modeling', 'Star/Snowflake, normalization', 9, 2, 0),
(20, 'React Core', 'Components, state, hooks', 10, 0, 0),
(21, 'UI/UX', 'Accessibility & design systems', 10, 1, 0),
(22, 'Frontend Build', 'Tooling, testing, performance', 10, 2, 0),
(23, 'Sourcing', 'Channels & boolean search', 11, 0, 0),
(24, 'Screening', 'Structured interviews & bias', 11, 1, 0),
(25, 'Offer/Close', 'Negotiation & stakeholder mgmt', 11, 2, 0),
(26, 'ETL Design', 'Batch vs streaming', 12, 0, 0),
(27, 'Data Warehousing', 'Partitioning, orchestration', 12, 1, 0),
(28, 'Ops & Reliability', 'Monitoring & SLAs', 12, 2, 0),
(29, 'CI/CD', 'Pipelines & artifacts', 13, 0, 0),
(30, 'Containers', 'Docker/Kubernetes', 13, 1, 0),
(31, 'Observability', 'Metrics, logs, tracing', 13, 2, 0),
(32, 'Needs Analysis', 'Gap analysis & stakeholders', 14, 0, 0),
(33, 'Program Delivery', 'Scheduling & facilitation', 14, 1, 0),
(34, 'ML Fundamentals', 'Bias/variance, evaluation', 15, 0, 0),
(35, 'Production ML', 'Serving & monitoring', 15, 1, 0),
(36, 'Feature Engineering', 'Preprocessing & leakage', 15, 2, 0),
(37, 'Architecture', 'DDD, microservices', 16, 0, 0),
(38, 'Performance', 'Profiling & tuning', 16, 1, 0),
(39, 'Leadership', 'Mentoring & reviews', 16, 2, 0),
(40, 'Admin & Compliance', 'Docs, GDPR basics', 17, 0, 0),
(41, 'Communication', 'Ticketing & employee support', 17, 1, 0),
(42, 'Excel/Spreadsheets', 'Functions & pivots', 18, 0, 0),
(43, 'Basic SQL', 'SELECT, JOIN, GROUP BY', 18, 1, 0),
(44, 'HTML/CSS', 'Semantics & layout', 19, 0, 0),
(45, 'React Basics', 'Components & props', 19, 1, 0),
(46, 'Remote Sourcing', 'Async comms & tools', 20, 0, 0),
(47, 'Client Handling', 'Req intake & updates', 20, 1, 0),
(48, 'Data Cleansing', 'Quality & validation', 21, 0, 0),
(49, 'Pipelines', 'Scheduling & retries', 21, 1, 0),
(50, 'Reliability', 'SLI/SLO/Error budgets', 22, 0, 0),
(51, 'Incident Response', 'On-call & postmortems', 22, 1, 0),
(52, 'Performance Eng', 'Capacity & load', 22, 2, 0),
(53, 'Learning Strategy', 'KPIs & outcomes', 23, 0, 0),
(54, 'Content Design', 'Curricula & materials', 23, 1, 0),
(55, 'Research Methods', 'Reading & experimentation', 24, 0, 0),
(56, 'Advanced ML', 'DL architectures', 24, 1, 0),
(57, 'Paper Review', 'Critique & replication', 24, 2, 0);

-- === QUESTION ===  (ΠΡΟΣΟΧΗ: με position)
INSERT INTO question (id, title, description, step_id, position) VALUES
(13, 'What is a HashMap?', 'Explain usage and performance', 11, 0),
(14, 'SOLID Principles', 'Define the 5 principles',        11, 1),
(15, 'Conflict Resolution', 'How do you resolve team conflicts?', 12, 0),
(16, 'Scale an API', 'Design a high-traffic read API', 17, 0),
(17, 'Caching Strategy', 'Where and what to cache', 17, 1),
(18, 'Conflict Case', 'Resolve conflict between teams', 18, 0),
(19, 'Policy Dilemma', 'Handle borderline policy breach', 18, 1),
(20, 'Dimensional Model', 'Design sales analytics model', 19, 0),
(21, 'Normalization', '3NF vs denormalization trade-offs', 19, 1),
(22, 'Hooks', 'useEffect pitfalls', 20, 0),
(23, 'State Mgmt', 'Lifting state vs context', 20, 1),
(24, 'Accessibility', 'ARIA and keyboard nav', 21, 0),
(25, 'Design Tokens', 'Scale a design system', 21, 1),
(26, 'Performance', 'Core Web Vitals improvements', 22, 0),
(27, 'Testing', 'Unit vs E2E in FE', 22, 1),
(28, 'Boolean Search', 'Build queries for LinkedIn', 23, 0),
(29, 'Sourcing Plan', '30-day plan for hard role', 23, 1),
(30, 'Structured Interview', 'Scorecards & consistency', 24, 0),
(31, 'Bias Mitigation', 'Reduce bias in interviews', 24, 1),
(32, 'Negotiation', 'Handle multiple offers', 25, 0),
(33, 'Stakeholders', 'Sync with hiring manager', 25, 1),
(34, 'Batch vs Stream', 'Choose for clickstream', 26, 0),
(35, 'Idempotency', 'Design safe re-runs', 26, 1),
(36, 'Partitioning', 'Strategy for large tables', 27, 0),
(37, 'Orchestration', 'Airflow vs alternatives', 27, 1),
(38, 'Monitoring', 'What to monitor in ETL', 28, 0),
(39, 'SLAs', 'Define SLAs for data jobs', 28, 1),
(40, 'Pipeline Stages', 'Build/test/deploy gates', 29, 0),
(41, 'Rollback', 'Safe rollback strategies', 29, 1),
(42, 'K8s Objects', 'Deployments vs StatefulSets', 30, 0),
(43, 'Networking', 'Ingress vs Service', 30, 1),
(44, 'Tracing', 'When to trace spans', 31, 0),
(45, 'Logging', 'Structure logs for value', 31, 1),
(46, 'TNA', 'Define training needs', 32, 0),
(47, 'Stakeholders', 'Align on objectives', 32, 1),
(48, 'Facilitation', 'Engagement techniques', 33, 0),
(49, 'Scheduling', 'Cohort vs on-demand', 33, 1),
(50, 'Overfitting', 'Detect and prevent', 34, 0),
(51, 'Metrics', 'Choose metrics per task', 34, 1),
(52, 'Model Serving', 'Batch vs online', 35, 0),
(53, 'Monitoring ML', 'Data drift & alerts', 35, 1),
(54, 'Leakage', 'Find and avoid leakage', 36, 0),
(55, 'Encoding', 'Categorical encoding choices', 36, 1),
(56, 'Bounded Contexts', 'Identify contexts', 37, 0),
(57, 'Service Boundaries', 'Split monolith safely', 37, 1),
(58, 'Profiling', 'Find hotspots', 38, 0),
(59, 'Caching Layers', 'DB vs app cache', 38, 1),
(60, 'Code Reviews', 'Set standards & coach', 39, 0),
(61, 'Roadmapping', 'Plan & communicate', 39, 1),
(62, 'GDPR Basics', 'Data requests handling', 40, 0),
(63, 'Records', 'Maintain employee files', 40, 1),
(64, 'Ticket Prioritization', 'SLA-based triage', 41, 0),
(65, 'Empathy', 'Handle sensitive cases', 41, 1),
(66, 'Formulas', 'VLOOKUP/XLOOKUP/INDEX-MATCH', 42, 0),
(67, 'Pivot Tables', 'Summarize datasets', 42, 1),
(68, 'Joins', 'Inner vs left with examples', 43, 0),
(69, 'Aggregation', 'GROUP BY & HAVING', 43, 1),
(70, 'Semantics', 'Accessible HTML', 44, 0),
(71, 'Layout', 'Flexbox vs Grid', 44, 1),
(72, 'Props vs State', 'Identify usage', 45, 0),
(73, 'Lists/Keys', 'Render lists correctly', 45, 1),
(74, 'Tools', 'ATS/CRM usage', 46, 0),
(75, 'Async', 'Status updates cadence', 46, 1),
(76, 'Intake', 'Clarify must-haves', 47, 0),
(77, 'Reporting', 'Progress & metrics', 47, 1),
(78, 'Validation', 'Detect anomalies', 48, 0),
(79, 'Standardization', 'Normalize inputs', 48, 1),
(80, 'Retries', 'Backoff strategies', 49, 0),
(81, 'Scheduling', 'Daily vs event-driven', 49, 1),
(82, 'SLOs', 'Define and track', 50, 0),
(83, 'Error Budgets', 'Use in decisions', 50, 1),
(84, 'Runbooks', 'Create effective runbooks', 51, 0),
(85, 'Postmortems', 'Blameless approach', 51, 1),
(86, 'Load Testing', 'Plan & execute', 52, 0),
(87, 'Capacity', 'Forecast & scale', 52, 1),
(88, 'KPIs', 'Define learning KPIs', 53, 0),
(89, 'ROI', 'Measure training impact', 53, 1),
(90, 'Curricula', 'Learning paths', 54, 0),
(91, 'Materials', 'Create reusable assets', 54, 1),
(92, 'Lit Review', 'Find & assess papers', 55, 0),
(93, 'Experiment Design', 'Hypotheses & controls', 55, 1),
(94, 'Architectures', 'CNN/RNN/Transformers', 56, 0),
(95, 'Regularization', 'Dropout/weight decay', 56, 1),
(96, 'Reproducibility', 'Replication plan', 57, 0),
(97, 'Critique', 'Assess assumptions', 57, 1);


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
(27, 'Artificial Intelligence', 'ai000', 0),
(28, 'Communication',        'comm001', 0),
(29, 'Problem Solving',      'prob001', 0),
(30, 'Conflict Management',  'conf001', 0);

-- === question_skill ===
INSERT INTO question_skill (question_id, skill_id) VALUES
(13, 16),
(14,17),(15,28),(15,30),
(16,16),(16,17),(17,16),
(18,24),(18,25),(19,24),
(20,18),(20,26),(21,18),
(22,20),(22,24),(23,20),
(24,20),(24,24),(25,24),
(26,20),(26,21),(27,20),
(28,25),(28,24),(29,25),
(30,25),(30,24),(31,24),
(32,25),(32,24),(33,24),
(34,18),(34,26),(35,26),
(36,18),(36,26),(37,21),
(38,21),(38,26),(39,21),
(40,21),(40,24),(41,21),
(42,22),(42,21),(43,22),
(44,21),(44,22),(45,21),
(46,24),(46,23),(47,24),
(48,24),(48,23),(49,23),
(50,27),(50,26),(51,27),
(52,27),(52,21),(53,27),
(54,26),(54,27),(55,26),
(56,17),(56,16),(57,16),
(58,16),(58,21),(59,16),
(60,24),(61,24),
(62,24),(62,23),(63,24),
(64,24),(65,24),
(66,23),(66,26),(67,23),
(68,18),(69,18),
(70,20),(71,20),
(72,20),(73,20),
(74,25),(74,24),(75,24),
(76,25),(77,25),
(78,26),(78,18),(79,26),
(80,26),(81,26),
(82,21),(82,22),(83,21),
(84,21),(84,24),(85,24),
(86,21),(87,21),
(88,24),(89,24),
(90,24),(91,23),
(92,27),(92,23),(93,27),
(94,27),(95,27),
(96,27),(97,27);


-- === STEP_SCORE ===
INSERT INTO step_score (id, step_id, interview_report_id) VALUES
(18, 11, 9),
(19, 12, 9),
(20, 17, 9);

-- === QUESTION_SCORE ===
INSERT INTO question_score (id, question_id, step_score_id, score) VALUES
(20, 13, 18, 9.0),
(21, 14, 18, 8.5),
(22, 15, 19, 7.0),
(23, 16, 20, 8.0),
(24, 17, 20, 7.5);

