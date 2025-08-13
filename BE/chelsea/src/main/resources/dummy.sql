-- ==================================================================
-- 상위 코드 (main_code) 데이터
-- ==================================================================
INSERT IGNORE INTO main_code (MAIN_CODE, MAIN_CODE_NAME, MAIN_CODE_DESCRIPTION, USE_YN, created_at, updated_at)
VALUES ('TRK', '트랙', '트랙 종류', true, NOW(), NOW()),
       ('POS', '포지션', '포지션 종류', true, NOW(), NOW()),
       ('GOAL', '목표', '프로젝트 목표 종류', true, NOW(), NOW()),
       ('TECH', '기술 스택', '기술 스택 종류', true, NOW(), NOW()),
       ('MBTI', 'MBTI', '성격 유형 지표', true, NOW(), NOW()),
       ('RULE', '규칙', '팀 빌딩 규칙', true, NOW(), NOW()),
       ('CLS', '반', '소속 반 종류', true, NOW(), NOW());


-- ==================================================================
-- 하위 코드 (sub_code) 데이터
-- ==================================================================
-- == TRK (트랙) ==
INSERT IGNORE INTO sub_code (SUB_CODE, MAIN_CODE, SUB_CODE_NAME, SUB_CODE_DESCRIPTION, USE_YN, created_at, updated_at)
VALUES ('TRK001', 'TRK', '웹 기술', '웹 기술을(를) 나타내는 코드. (코드: TRK001)', true, NOW(), NOW()),
       ('TRK002', 'TRK', '웹 디자인', '웹 디자인을(를) 나타내는 코드. (코드: TRK002)', true, NOW(), NOW()),
       ('TRK003', 'TRK', 'AIOT', 'AIOT을(를) 나타내는 코드. (코드: TRK003)', true, NOW(), NOW()),
       ('TRK004', 'TRK', 'MOBILE', 'MOBILE을(를) 나타내는 코드. (코드: TRK004)', true, NOW(), NOW());

-- == POS (포지션) ==
INSERT IGNORE INTO sub_code (SUB_CODE, MAIN_CODE, SUB_CODE_NAME, SUB_CODE_DESCRIPTION, USE_YN, created_at, updated_at)
VALUES ('POS001', 'POS', '프론트', '프론트을(를) 나타내는 코드. (코드: POS001)', true, NOW(), NOW()),
       ('POS002', 'POS', '백엔드', '백엔드을(를) 나타내는 코드. (코드: POS002)', true, NOW(), NOW()),
       ('POS003', 'POS', '풀스택', '풀스택을(를) 나타내는 코드. (코드: POS003)', true, NOW(), NOW()),
       ('POS004', 'POS', '인프라', '인프라을(를) 나타내는 코드. (코드: POS004)', true, NOW(), NOW()),
       ('POS005', 'POS', '임베디드', '임베디드을(를) 나타내는 코드. (코드: POS005)', true, NOW(), NOW()),
       ('POS006', 'POS', '모바일', '모바일을(를) 나타내는 코드. (코드: POS006)', true, NOW(), NOW()),
       ('POS007', 'POS', 'AI', 'AI을(를) 나타내는 코드. (코드: POS007)', true, NOW(), NOW());

-- == GOAL (목표) ==
INSERT IGNORE INTO sub_code (SUB_CODE, MAIN_CODE, SUB_CODE_NAME, SUB_CODE_DESCRIPTION, USE_YN, created_at, updated_at)
VALUES ('GOAL001', 'GOAL', '포트폴리오', '포트폴리오을(를) 나타내는 코드. (코드: GOAL001)', true, NOW(), NOW()),
       ('GOAL002', 'GOAL', '수상', '수상을(를) 나타내는 코드. (코드: GOAL002)', true, NOW(), NOW()),
       ('GOAL003', 'GOAL', '취업', '취업을(를) 나타내는 코드. (코드: GOAL003)', true, NOW(), NOW());

-- == MBTI (성격 유형 지표) ==
INSERT IGNORE INTO sub_code (SUB_CODE, MAIN_CODE, SUB_CODE_NAME, SUB_CODE_DESCRIPTION, USE_YN, created_at, updated_at)
VALUES ('MBTI001', 'MBTI', 'INTJ', 'INTJ을(를) 나타내는 코드. (코드: MBTI001)', true, NOW(), NOW()),
       ('MBTI002', 'MBTI', 'INTP', 'INTP을(를) 나타내는 코드. (코드: MBTI002)', true, NOW(), NOW()),
       ('MBTI003', 'MBTI', 'ENTJ', 'ENTJ을(를) 나타내는 코드. (코드: MBTI003)', true, NOW(), NOW()),
       ('MBTI004', 'MBTI', 'ENTP', 'ENTP을(를) 나타내는 코드. (코드: MBTI004)', true, NOW(), NOW()),
       ('MBTI005', 'MBTI', 'INFJ', 'INFJ을(를) 나타내는 코드. (코드: MBTI005)', true, NOW(), NOW()),
       ('MBTI006', 'MBTI', 'INFP', 'INFP을(를) 나타내는 코드. (코드: MBTI006)', true, NOW(), NOW()),
       ('MBTI007', 'MBTI', 'ENFJ', 'ENFJ을(를) 나타내는 코드. (코드: MBTI007)', true, NOW(), NOW()),
       ('MBTI008', 'MBTI', 'ENFP', 'ENFP을(를) 나타내는 코드. (코드: MBTI008)', true, NOW(), NOW()),
       ('MBTI009', 'MBTI', 'ISTJ', 'ISTJ을(를) 나타내는 코드. (코드: MBTI009)', true, NOW(), NOW()),
       ('MBTI010', 'MBTI', 'ISFJ', 'ISFJ을(를) 나타내는 코드. (코드: MBTI010)', true, NOW(), NOW()),
       ('MBTI011', 'MBTI', 'ESTJ', 'ESTJ을(를) 나타내는 코드. (코드: MBTI011)', true, NOW(), NOW()),
       ('MBTI012', 'MBTI', 'ESFJ', 'ESFJ을(를) 나타내는 코드. (코드: MBTI012)', true, NOW(), NOW()),
       ('MBTI013', 'MBTI', 'ISTP', 'ISTP을(를) 나타내는 코드. (코드: MBTI013)', true, NOW(), NOW()),
       ('MBTI014', 'MBTI', 'ISFP', 'ISFP을(를) 나타내는 코드. (코드: MBTI014)', true, NOW(), NOW()),
       ('MBTI015', 'MBTI', 'ESTP', 'ESTP을(를) 나타내는 코드. (코드: MBTI015)', true, NOW(), NOW()),
       ('MBTI016', 'MBTI', 'ESFP', 'ESFP을(를) 나타내는 코드. (코드: MBTI016)', true, NOW(), NOW());

-- == TECH (기술 스택) ==
INSERT IGNORE INTO sub_code (SUB_CODE, MAIN_CODE, SUB_CODE_NAME, SUB_CODE_DESCRIPTION, USE_YN, created_at, updated_at)
VALUES ('FE_TECH001', 'TECH', 'React', 'React을(를) 나타내는 코드. (코드: FE_TECH001)', true, NOW(), NOW()),
       ('FE_TECH002', 'TECH', 'Vue.js', 'Vue.js을(를) 나타내는 코드. (코드: FE_TECH002)', true, NOW(), NOW()),
       ('FE_TECH003', 'TECH', 'Angular', 'Angular을(를) 나타내는 코드. (코드: FE_TECH003)', true, NOW(), NOW()),
       ('FE_TECH004', 'TECH', 'Svelte', 'Svelte을(를) 나타내는 코드. (코드: FE_TECH004)', true, NOW(), NOW()),
       ('FE_TECH005', 'TECH', 'Next.js', 'Next.js을(를) 나타내는 코드. (코드: FE_TECH005)', true, NOW(), NOW()),
       ('FE_TECH006', 'TECH', 'Nuxt.js', 'Nuxt.js을(를) 나타내는 코드. (코드: FE_TECH006)', true, NOW(), NOW()),
       ('LN_TECH007', 'TECH', 'TypeScript', 'TypeScript을(를) 나타내는 코드. (코드: LN_TECH007)', true, NOW(), NOW()),
       ('LN_TECH008', 'TECH', 'JavaScript', 'JavaScript을(를) 나타내는 코드. (코드: LN_TECH008)', true, NOW(), NOW()),
       ('FE_TECH009', 'TECH', 'HTML5', 'HTML5을(를) 나타내는 코드. (코드: FE_TECH009)', true, NOW(), NOW()),
       ('FE_TECH010', 'TECH', 'CSS3', 'CSS3을(를) 나타내는 코드. (코드: FE_TECH010)', true, NOW(), NOW()),
       ('FE_TECH011', 'TECH', 'Sass', 'Sass을(를) 나타내는 코드. (코드: FE_TECH011)', true, NOW(), NOW()),
       ('FE_TECH012', 'TECH', 'TailwindCSS', 'TailwindCSS을(를) 나타내는 코드. (코드: FE_TECH012)', true, NOW(), NOW()),
       ('FE_TECH013', 'TECH', 'Styled Component', 'Styled Component을(를) 나타내는 코드. (코드: FE_TECH013)', true, NOW(), NOW()),
       ('FE_TECH014', 'TECH', 'Bootstrap', 'Bootstrap을(를) 나타내는 코드. (코드: FE_TECH014)', true, NOW(), NOW()),
       ('BE_TECH015', 'TECH', 'Spring Boot', 'Spring Boot을(를) 나타내는 코드. (코드: BE_TECH015)', true, NOW(), NOW()),
       ('BE_TECH016', 'TECH', 'Node.js', 'Node.js을(를) 나타내는 코드. (코드: BE_TECH016)', true, NOW(), NOW()),
       ('BE_TECH017', 'TECH', 'Express', 'Express을(를) 나타내는 코드. (코드: BE_TECH017)', true, NOW(), NOW()),
       ('BE_TECH018', 'TECH', 'Nest.js', 'Nest.js을(를) 나타내는 코드. (코드: BE_TECH018)', true, NOW(), NOW()),
       ('BE_TECH019', 'TECH', 'Django', 'Django을(를) 나타내는 코드. (코드: BE_TECH019)', true, NOW(), NOW()),
       ('BE_TECH020', 'TECH', 'Flask', 'Flask을(를) 나타내는 코드. (코드: BE_TECH020)', true, NOW(), NOW()),
       ('BE_TECH021', 'TECH', 'FastAPI', 'FastAPI을(를) 나타내는 코드. (코드: BE_TECH021)', true, NOW(), NOW()),
       ('BE_TECH022', 'TECH', 'Ruby on Rails', 'Ruby on Rails을(를) 나타내는 코드. (코드: BE_TECH022)', true, NOW(), NOW()),
       ('BE_TECH023', 'TECH', 'ASP.NET Core', 'ASP.NET Core을(를) 나타내는 코드. (코드: BE_TECH023)', true, NOW(), NOW()),
       ('LN_TECH024', 'TECH', 'PHP', 'PHP을(를) 나타내는 코드. (코드: LN_TECH024)', true, NOW(), NOW()),
       ('LN_TECH025', 'TECH', 'Java', 'Java을(를) 나타내는 코드. (코드: LN_TECH025)', true, NOW(), NOW()),
       ('LN_TECH026', 'TECH', 'Kotlin', 'Kotlin을(를) 나타내는 코드. (코드: LN_TECH026)', true, NOW(), NOW()),
       ('LN_TECH027', 'TECH', 'Python', 'Python을(를) 나타내는 코드. (코드: LN_TECH027)', true, NOW(), NOW()),
       ('LN_TECH028', 'TECH', 'Go', 'Go을(를) 나타내는 코드. (코드: LN_TECH028)', true, NOW(), NOW()),
       ('LN_TECH029', 'TECH', 'C#', 'C#을(를) 나타내는 코드. (코드: LN_TECH029)', true, NOW(), NOW()),
       ('LN_TECH030', 'TECH', 'Elixir', 'Elixir을(를) 나타내는 코드. (코드: LN_TECH030)', true, NOW(), NOW()),
       ('BE_TECH031', 'TECH', 'RESTful API', 'RESTful API을(를) 나타내는 코드. (코드: BE_TECH031)', true, NOW(), NOW()),
       ('BE_TECH032', 'TECH', 'GraphQL', 'GraphQL을(를) 나타내는 코드. (코드: BE_TECH032)', true, NOW(), NOW()),
       ('BE_TECH033', 'TECH', 'gRPC', 'gRPC을(를) 나타내는 코드. (코드: BE_TECH033)', true, NOW(), NOW()),
       ('BE_TECH034', 'TECH', 'WebSocket', 'WebSocket을(를) 나타내는 코드. (코드: BE_TECH034)', true, NOW(), NOW()),
       ('BE_TECH035', 'TECH', 'SSE', 'SSE을(를) 나타내는 코드. (코드: BE_TECH035)', true, NOW(), NOW()),
       ('DB_TECH036', 'TECH', 'MySQL', 'MySQL을(를) 나타내는 코드. (코드: DB_TECH036)', true, NOW(), NOW()),
       ('DB_TECH037', 'TECH', 'MariaDB', 'MariaDB을(를) 나타내는 코드. (코드: DB_TECH037)', true, NOW(), NOW()),
       ('DB_TECH038', 'TECH', 'PostgreSQL', 'PostgreSQL을(를) 나타내는 코드. (코드: DB_TECH038)', true, NOW(), NOW()),
       ('DB_TECH039', 'TECH', 'SQLite', 'SQLite을(를) 나타내는 코드. (코드: DB_TECH039)', true, NOW(), NOW()),
       ('DB_TECH040', 'TECH', 'MS-SQL', 'MS-SQL을(를) 나타내는 코드. (코드: DB_TECH040)', true, NOW(), NOW()),
       ('DB_TECH041', 'TECH', 'Oracle', 'Oracle을(를) 나타내는 코드. (코드: DB_TECH041)', true, NOW(), NOW()),
       ('DB_TECH042', 'TECH', 'MongoDB', 'MongoDB을(를) 나타내는 코드. (코드: DB_TECH042)', true, NOW(), NOW()),
       ('DB_TECH043', 'TECH', 'DynamoDB', 'DynamoDB을(를) 나타내는 코드. (코드: DB_TECH043)', true, NOW(), NOW()),
       ('MO_TECH044', 'TECH', 'Flutter', 'Flutter을(를) 나타내는 코드. (코드: MO_TECH044)', true, NOW(), NOW()),
       ('MO_TECH045', 'TECH', 'React Native', 'React Native을(를) 나타내는 코드. (코드: MO_TECH045)', true, NOW(), NOW()),
       ('MO_TECH046', 'TECH', 'Ionic', 'Ionic을(를) 나타내는 코드. (코드: MO_TECH046)', true, NOW(), NOW()),
       ('MO_TECH047', 'TECH', 'Android (Java/Kotlin)', 'Android (Java/Kotlin)을(를) 나타내는 코드. (코드: MO_TECH047)', true,
        NOW(),
        NOW()),
       ('LN_TECH048', 'TECH', 'Objective-C', 'Objective-C을(를) 나타내는 코드. (코드: LN_TECH048)', true, NOW(), NOW()),
       ('LN_TECH049', 'TECH', 'Swift', 'Swift을(를) 나타내는 코드. (코드: LN_TECH049)', true, NOW(), NOW()),
       ('MO_TECH050', 'TECH', 'Kotlin Multiplatform Mobile (KMM)',
        'Kotlin Multiplatform Mobile (KMM)을(를) 나타내는 코드. (코드: MO_TECH050)', true, NOW(), NOW()),
       ('MO_TECH051', 'TECH', 'Xamarin', 'Xamarin을(를) 나타내는 코드. (코드: MO_TECH051)', true, NOW(), NOW()),
       ('LN_TECH052', 'TECH', 'C', 'C을(를) 나타내는 코드. (코드: LN_TECH052)', true, NOW(), NOW()),
       ('LN_TECH053', 'TECH', 'C++', 'C++을(를) 나타내는 코드. (코드: LN_TECH053)', true, NOW(), NOW()),
       ('EM_TECH054', 'TECH', 'FreeRTOS', 'FreeRTOS을(를) 나타내는 코드. (코드: EM_TECH054)', true, NOW(), NOW()),
       ('EM_TECH055', 'TECH', 'Zephyr', 'Zephyr을(를) 나타내는 코드. (코드: EM_TECH055)', true, NOW(), NOW()),
       ('EM_TECH056', 'TECH', 'Arduino Platform', 'Arduino Platform을(를) 나타내는 코드. (코드: EM_TECH056)', true, NOW(), NOW()),
       ('EM_TECH057', 'TECH', 'MicroPython', 'MicroPython을(를) 나타내는 코드. (코드: EM_TECH057)', true, NOW(), NOW()),
       ('EM_TECH058', 'TECH', 'MQTT', 'MQTT을(를) 나타내는 코드. (코드: EM_TECH058)', true, NOW(), NOW()),
       ('IN_TECH059', 'TECH', 'Docker', 'Docker을(를) 나타내는 코드. (코드: IN_TECH059)', true, NOW(), NOW()),
       ('IN_TECH060', 'TECH', 'Kubernetes', 'Kubernetes을(를) 나타내는 코드. (코드: IN_TECH060)', true, NOW(), NOW()),
       ('IN_TECH061', 'TECH', 'Nginx', 'Nginx을(를) 나타내는 코드. (코드: IN_TECH061)', true, NOW(), NOW()),
       ('IN_TECH062', 'TECH', 'Apache', 'Apache을(를) 나타내는 코드. (코드: IN_TECH062)', true, NOW(), NOW()),
       ('IN_TECH063', 'TECH', 'Jenkins', 'Jenkins을(를) 나타내는 코드. (코드: IN_TECH063)', true, NOW(), NOW()),
       ('IN_TECH064', 'TECH', 'GitHub Actions', 'GitHub Actions을(를) 나타내는 코드. (코드: IN_TECH064)', true, NOW(), NOW()),
       ('IN_TECH065', 'TECH', 'RabbitMQ', 'RabbitMQ을(를) 나타내는 코드. (코드: IN_TECH065)', true, NOW(), NOW()),
       ('IN_TECH066', 'TECH', 'Redis', 'Redis을(를) 나타내는 코드. (코드: IN_TECH066)', true, NOW(), NOW()),
       ('IN_TECH067', 'TECH', 'Kafka', 'Kafka을(를) 나타내는 코드. (코드: IN_TECH067)', true, NOW(), NOW()),
       ('IN_TECH068', 'TECH', 'OpenSearch', 'OpenSearch을(를) 나타내는 코드. (코드: IN_TECH068)', true, NOW(), NOW()),
       ('IN_TECH069', 'TECH', 'Elasticsearch', 'Elasticsearch을(를) 나타내는 코드. (코드: IN_TECH069)', true, NOW(), NOW()),
       ('IN_TECH070', 'TECH', 'Rush (Monorepo tool)', 'Rush (Monorepo tool)을(를) 나타내는 코드. (코드: IN_TECH070)', true, NOW(),
        NOW());

-- ------------------------------------------------------------------
-- Sample data for Teams table (12 rows)
-- ------------------------------------------------------------------
INSERT INTO teams (name, description, track_code, is_deleted, major_count, non_major_count, created_at, updated_at)
VALUES
-- 웹 기술 트랙
('팀 001', '웹 기술 기반 프로젝트 수행팀 A', 'TRK001', FALSE, 2, 1, NOW(), NOW()),

-- 웹 디자인 트랙
('팀 002', '웹 디자인 전문팀 A', 'TRK002', FALSE, 1, 1, NOW(), NOW()),

-- AIoT 트랙
('팀 003', 'AIoT 융합 프로젝트팀 A', 'TRK003', FALSE, 0, 2, NOW(), NOW()),

-- 모바일 트랙
('팀 004', '모바일 앱 개발팀 A', 'TRK004', FALSE, 2, 3, NOW(), NOW()),
('팀 005', '웹 디자인 전문팀 B', 'TRK002', FALSE, 2, 2, NOW(), NOW());

-- ------------------------------------------------------------------
-- Sample data for Recruitments table
-- ------------------------------------------------------------------
INSERT INTO recruitments (position_code, team_id, created_at, updated_at)
VALUES
    -- 웹 기술 팀 001
    ('POS001', 1, NOW(), NOW()), -- 프론트
    ('POS002', 1, NOW(), NOW()), -- 백엔드
    ('POS004', 1, NOW(), NOW()), -- 인프라
    -- 웹 디자인 팀 002
    ('POS001', 2, NOW(), NOW()), -- 프론트
    ('POS003', 2, NOW(), NOW()), -- 풀스택
    ('POS004', 2, NOW(), NOW()), -- 인프라
    -- AIoT 팀 003
    ('POS002', 3, NOW(), NOW()), -- 백엔드
    ('POS005', 3, NOW(), NOW()), -- 임베디드
    ('POS007', 3, NOW(), NOW()), -- AI
    -- 모바일 팀 004
    ('POS001', 4, NOW(), NOW()), -- 프론트
    ('POS002', 4, NOW(), NOW()), -- 백엔드
    ('POS006', 4, NOW(), NOW()), -- 모바일
    -- 웹 디자인 팀 005
    ('POS001', 5, NOW(), NOW()), -- 프론트
    ('POS002', 5, NOW(), NOW());
-- 백엔드


/*--------------------------------------------------------
-- Student 테이블에 데이터 삽입
--------------------------------------------------------*/
INSERT IGNORE INTO students (student_id, name, major_yn, team_id, class_code, created_at, updated_at)
VALUES
    -- team 1 (4명)
    (1300001, '김서준', TRUE, 1, 'CLS001', NOW(), NOW()),
    (1300002, '이하윤', FALSE, 1, 'CLS001', NOW(), NOW()),
    (1300003, '박지호', TRUE, 1, 'CLS001', NOW(), NOW()),
    (1300004, '최서아', FALSE, 1, 'CLS001', NOW(), NOW()),

    -- team 2 (6명)
    (1300007, '조유준', TRUE, 2, 'CLS001', NOW(), NOW()),
    (1300008, '윤하은', FALSE, 2, 'CLS001', NOW(), NOW()),
    (1300009, '장시우', TRUE, 2, 'CLS001', NOW(), NOW()),
    (1300010, '임수아', FALSE, 2, 'CLS001', NOW(), NOW()),
    (1300011, '한도윤', TRUE, 2, 'CLS001', NOW(), NOW()),
    (1300012, '오채원', FALSE, 2, 'CLS001', NOW(), NOW()),

    -- team 3 (6명)
    (1300013, '서예준', TRUE, 3, 'CLS001', NOW(), NOW()),
    (1300014, '신유나', FALSE, 3, 'CLS001', NOW(), NOW()),
    (1300015, '권민준', TRUE, 3, 'CLS001', NOW(), NOW()),
    (1300016, '황다은', FALSE, 3, 'CLS001', NOW(), NOW()),
    (1300017, '안주원', TRUE, 3, 'CLS001', NOW(), NOW()),
    (1300018, '송지우', FALSE, 3, 'CLS001', NOW(), NOW()),

    -- team 4 (6명)
    (1300019, '유하준', TRUE, 4, 'CLS001', NOW(), NOW()),
    (1300020, '전서윤', FALSE, 4, 'CLS001', NOW(), NOW()),
    (1300021, '정서아', TRUE, 4, 'CLS001', NOW(), NOW()),
    (1300022, '강지안', FALSE, 4, 'CLS001', NOW(), NOW()),
    (1300023, '최은우', TRUE, 4, 'CLS001', NOW(), NOW()),
    (1300024, '김하윤', FALSE, 4, 'CLS001', NOW(), NOW()),

    -- team 5 (6명)
    (1300025, '이시우', TRUE, 5, 'CLS001', NOW(), NOW()),
    (1300026, '박서준', FALSE, 5, 'CLS001', NOW(), NOW()),
    (1300027, '조지호', TRUE, 5, 'CLS001', NOW(), NOW()),
    (1300028, '윤수아', FALSE, 5, 'CLS001', NOW(), NOW()),
    (1300029, '장유준', TRUE, 5, 'CLS001', NOW(), NOW()),
    (1300030, '임채원', FALSE, 5, 'CLS001', NOW(), NOW()),

    -- team 6 (6명)
    (1300031, '한민준', TRUE, 6, 'CLS001', NOW(), NOW()),
    (1300032, '오유나', FALSE, 6, 'CLS001', NOW(), NOW()),
    (1300033, '서하준', TRUE, 6, 'CLS001', NOW(), NOW()),
    (1300034, '신지우', FALSE, 6, 'CLS001', NOW(), NOW()),
    (1300035, '권도윤', TRUE, 6, 'CLS001', NOW(), NOW()),
    (1300036, '황서윤', FALSE, 6, 'CLS001', NOW(), NOW()),

    -- team 7 (5명)
    (1300037, '안예준', TRUE, 7, 'CLS001', NOW(), NOW()),
    (1300038, '송다은', FALSE, 7, 'CLS001', NOW(), NOW()),
    (1300039, '유주원', TRUE, 7, 'CLS001', NOW(), NOW()),
    (1300040, '전하은', FALSE, 7, 'CLS001', NOW(), NOW()),
    (1300041, '오지호', TRUE, 7, 'CLS001', NOW(), NOW()),

    -- team 8 (4명)
    (1300043, '임은우', TRUE, 8, 'CLS001', NOW(), NOW()),
    (1300044, '장하윤', FALSE, 8, 'CLS001', NOW(), NOW()),
    (1300045, '윤서준', TRUE, 8, 'CLS001', NOW(), NOW()),
    (1300046, '조지안', FALSE, 8, 'CLS001', NOW(), NOW()),

    -- team 9 (1명)
    (1300047, '강시우', TRUE, 9, 'CLS001', NOW(), NOW()),

    -- team 10 (1명)
    (1300048, '정수아', FALSE, 10, 'CLS001', NOW(), NOW()),

    -- team 11 (1명)
    (1300049, '최유준', TRUE, 11, 'CLS001', NOW(), NOW()),
    (1300042, '한서아', FALSE, NULL, 'CLS001', NOW(), NOW()),

    -- team 12 (3명)
    (1300005, '정은우', TRUE, 12, 'CLS001', NOW(), NOW()),
    (1300006, '강지안', FALSE, NULL, 'CLS001', NOW(), NOW()),
    (1300050, '박채원', FALSE, NULL, 'CLS001', NOW(), NOW());



