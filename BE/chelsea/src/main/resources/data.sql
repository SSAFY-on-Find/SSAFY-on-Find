-- ==================================================================
-- 상위 코드 (main_code) 데이터
-- ==================================================================
INSERT IGNORE INTO main_code (MAIN_CODE, MAIN_CODE_NAME, MAIN_CODE_DESCRIPTION, USE_YN, created_by, created_at)
VALUES ('TRK', '트랙', '트랙 종류', true, '0001234', NOW()),
       ('POS', '포지션', '포지션 종류', true, '0001234', NOW()),
       ('GOAL', '목표', '프로젝트 목표 종류', true, '0001234', NOW()),
       ('TECH', '기술 스택', '기술 스택 종류', true, '0001234', NOW()),
       ('MBTI', 'MBTI', '성격 유형 지표', true, '0001234', NOW()),
       ('RULE', '규칙', '팀 빌딩 규칙', true, '0001234', NOW()),
       ('CLS','반','소속 반 종류', true, '0001234', NOW());


-- ==================================================================
-- 하위 코드 (sub_code) 데이터
-- ==================================================================

-- == TRK (트랙) ==
INSERT IGNORE INTO sub_code (SUB_CODE, MAIN_CODE, SUB_CODE_NAME, SUB_CODE_DESCRIPTION, USE_YN, created_by, created_at)
VALUES ('TRK001', 'TRK', '웹 기술', '웹 기술을(를) 나타내는 코드. (코드: TRK001)', true, '0001234', NOW()),
       ('TRK002', 'TRK', '웹 디자인', '웹 디자인을(를) 나타내는 코드. (코드: TRK002)', true, '0001234', NOW()),
       ('TRK003', 'TRK', 'AIOT', 'AIOT을(를) 나타내는 코드. (코드: TRK003)', true, '0001234', NOW()),
       ('TRK004', 'TRK', 'MOBILE', 'MOBILE을(를) 나타내는 코드. (코드: TRK004)', true, '0001234', NOW());

-- == POS (포지션) ==
INSERT IGNORE INTO sub_code (SUB_CODE, MAIN_CODE, SUB_CODE_NAME, SUB_CODE_DESCRIPTION, USE_YN, created_by, created_at)
VALUES ('POS001', 'POS', '프론트', '프론트을(를) 나타내는 코드. (코드: POS001)', true, '0001234', NOW()),
       ('POS002', 'POS', '백엔드', '백엔드을(를) 나타내는 코드. (코드: POS002)', true, '0001234', NOW()),
       ('POS003', 'POS', '풀스택', '풀스택을(를) 나타내는 코드. (코드: POS003)', true, '0001234', NOW()),
       ('POS004', 'POS', '인프라', '인프라을(를) 나타내는 코드. (코드: POS004)', true, '0001234', NOW()),
       ('POS005', 'POS', '임베디드', '임베디드을(를) 나타내는 코드. (코드: POS005)', true, '0001234', NOW()),
       ('POS006', 'POS', '모바일', '모바일을(를) 나타내는 코드. (코드: POS006)', true, '0001234', NOW()),
       ('POS007', 'POS', 'AI', 'AI을(를) 나타내는 코드. (코드: POS007)', true, '0001234', NOW());

-- == GOAL (목표) ==
INSERT IGNORE INTO sub_code (SUB_CODE, MAIN_CODE, SUB_CODE_NAME, SUB_CODE_DESCRIPTION, USE_YN, created_by, created_at)
VALUES ('GOAL001', 'GOAL', '포트폴리오', '포트폴리오을(를) 나타내는 코드. (코드: GOAL001)', true, '0001234', NOW()),
       ('GOAL002', 'GOAL', '수상', '수상을(를) 나타내는 코드. (코드: GOAL002)', true, '0001234', NOW()),
       ('GOAL003', 'GOAL', '취업', '취업을(를) 나타내는 코드. (코드: GOAL003)', true, '0001234', NOW());

-- == MBTI (성격 유형 지표) ==
INSERT IGNORE INTO sub_code (SUB_CODE, MAIN_CODE, SUB_CODE_NAME, SUB_CODE_DESCRIPTION, USE_YN, created_by, created_at)
VALUES ('MBTI001', 'MBTI', 'INTJ', 'INTJ을(를) 나타내는 코드. (코드: MBTI001)', true, '0001234', NOW()),
       ('MBTI002', 'MBTI', 'INTP', 'INTP을(를) 나타내는 코드. (코드: MBTI002)', true, '0001234', NOW()),
       ('MBTI003', 'MBTI', 'ENTJ', 'ENTJ을(를) 나타내는 코드. (코드: MBTI003)', true, '0001234', NOW()),
       ('MBTI004', 'MBTI', 'ENTP', 'ENTP을(를) 나타내는 코드. (코드: MBTI004)', true, '0001234', NOW()),
       ('MBTI005', 'MBTI', 'INFJ', 'INFJ을(를) 나타내는 코드. (코드: MBTI005)', true, '0001234', NOW()),
       ('MBTI006', 'MBTI', 'INFP', 'INFP을(를) 나타내는 코드. (코드: MBTI006)', true, '0001234', NOW()),
       ('MBTI007', 'MBTI', 'ENFJ', 'ENFJ을(를) 나타내는 코드. (코드: MBTI007)', true, '0001234', NOW()),
       ('MBTI008', 'MBTI', 'ENFP', 'ENFP을(를) 나타내는 코드. (코드: MBTI008)', true, '0001234', NOW()),
       ('MBTI009', 'MBTI', 'ISTJ', 'ISTJ을(를) 나타내는 코드. (코드: MBTI009)', true, '0001234', NOW()),
       ('MBTI010', 'MBTI', 'ISFJ', 'ISFJ을(를) 나타내는 코드. (코드: MBTI010)', true, '0001234', NOW()),
       ('MBTI011', 'MBTI', 'ESTJ', 'ESTJ을(를) 나타내는 코드. (코드: MBTI011)', true, '0001234', NOW()),
       ('MBTI012', 'MBTI', 'ESFJ', 'ESFJ을(를) 나타내는 코드. (코드: MBTI012)', true, '0001234', NOW()),
       ('MBTI013', 'MBTI', 'ISTP', 'ISTP을(를) 나타내는 코드. (코드: MBTI013)', true, '0001234', NOW()),
       ('MBTI014', 'MBTI', 'ISFP', 'ISFP을(를) 나타내는 코드. (코드: MBTI014)', true, '0001234', NOW()),
       ('MBTI015', 'MBTI', 'ESTP', 'ESTP을(를) 나타내는 코드. (코드: MBTI015)', true, '0001234', NOW()),
       ('MBTI016', 'MBTI', 'ESFP', 'ESFP을(를) 나타내는 코드. (코드: MBTI016)', true, '0001234', NOW());

-- == TECH (기술 스택) ==
INSERT IGNORE INTO sub_code (SUB_CODE, MAIN_CODE, SUB_CODE_NAME, SUB_CODE_DESCRIPTION, USE_YN, created_by, created_at)
VALUES ('TECH001', 'TECH', 'React', 'React을(를) 나타내는 코드. (코드: TECH001)', true, '0001234', NOW()),
       ('TECH002', 'TECH', 'Vue.js', 'Vue.js을(를) 나타내는 코드. (코드: TECH002)', true, '0001234', NOW()),
       ('TECH003', 'TECH', 'Angular', 'Angular을(를) 나타내는 코드. (코드: TECH003)', true, '0001234', NOW()),
       ('TECH004', 'TECH', 'Svelte', 'Svelte을(를) 나타내는 코드. (코드: TECH004)', true, '0001234', NOW()),
       ('TECH005', 'TECH', 'Next.js', 'Next.js을(를) 나타내는 코드. (코드: TECH005)', true, '0001234', NOW()),
       ('TECH006', 'TECH', 'Nuxt.js', 'Nuxt.js을(를) 나타내는 코드. (코드: TECH006)', true, '0001234', NOW()),
       ('TECH007', 'TECH', 'TypeScript', 'TypeScript을(를) 나타내는 코드. (코드: TECH007)', true, '0001234', NOW()),
       ('TECH008', 'TECH', 'JavaScript', 'JavaScript을(를) 나타내는 코드. (코드: TECH008)', true, '0001234', NOW()),
       ('TECH009', 'TECH', 'HTML5', 'HTML5을(를) 나타내는 코드. (코드: TECH009)', true, '0001234', NOW()),
       ('TECH010', 'TECH', 'CSS3', 'CSS3을(를) 나타내는 코드. (코드: TECH010)', true, '0001234', NOW()),
       ('TECH011', 'TECH', 'Sass', 'Sass을(를) 나타내는 코드. (코드: TECH011)', true, '0001234', NOW()),
       ('TECH012', 'TECH', 'TailwindCSS', 'TailwindCSS을(를) 나타내는 코드. (코드: TECH012)', true, '0001234', NOW()),
       ('TECH013', 'TECH', 'Styled Component', 'Styled Component을(를) 나타내는 코드. (코드: TECH013)', true, '0001234', NOW()),
       ('TECH014', 'TECH', 'Bootstrap', 'Bootstrap을(를) 나타내는 코드. (코드: TECH014)', true, '0001234', NOW()),
       ('TECH015', 'TECH', 'Spring Boot', 'Spring Boot을(를) 나타내는 코드. (코드: TECH015)', true, '0001234', NOW()),
       ('TECH016', 'TECH', 'Node.js', 'Node.js을(를) 나타내는 코드. (코드: TECH016)', true, '0001234', NOW()),
       ('TECH017', 'TECH', 'Express', 'Express을(를) 나타내는 코드. (코드: TECH017)', true, '0001234', NOW()),
       ('TECH018', 'TECH', 'Nest.js', 'Nest.js을(를) 나타내는 코드. (코드: TECH018)', true, '0001234', NOW()),
       ('TECH019', 'TECH', 'Django', 'Django을(를) 나타내는 코드. (코드: TECH019)', true, '0001234', NOW()),
       ('TECH020', 'TECH', 'Flask', 'Flask을(를) 나타내는 코드. (코드: TECH020)', true, '0001234', NOW()),
       ('TECH021', 'TECH', 'FastAPI', 'FastAPI을(를) 나타내는 코드. (코드: TECH021)', true, '0001234', NOW()),
       ('TECH022', 'TECH', 'Ruby on Rails', 'Ruby on Rails을(를) 나타내는 코드. (코드: TECH022)', true, '0001234', NOW()),
       ('TECH023', 'TECH', 'ASP.NET Core', 'ASP.NET Core을(를) 나타내는 코드. (코드: TECH023)', true, '0001234', NOW()),
       ('TECH024', 'TECH', 'PHP', 'PHP을(를) 나타내는 코드. (코드: TECH024)', true, '0001234', NOW()),
       ('TECH025', 'TECH', 'Java', 'Java을(를) 나타내는 코드. (코드: TECH025)', true, '0001234', NOW()),
       ('TECH026', 'TECH', 'Kotlin', 'Kotlin을(를) 나타내는 코드. (코드: TECH026)', true, '0001234', NOW()),
       ('TECH027', 'TECH', 'Python', 'Python을(를) 나타내는 코드. (코드: TECH027)', true, '0001234', NOW()),
       ('TECH028', 'TECH', 'Go', 'Go을(를) 나타내는 코드. (코드: TECH028)', true, '0001234', NOW()),
       ('TECH029', 'TECH', 'C#', 'C#을(를) 나타내는 코드. (코드: TECH029)', true, '0001234', NOW()),
       ('TECH030', 'TECH', 'Elixir', 'Elixir을(를) 나타내는 코드. (코드: TECH030)', true, '0001234', NOW()),
       ('TECH031', 'TECH', 'RESTful API', 'RESTful API을(를) 나타내는 코드. (코드: TECH031)', true, '0001234', NOW()),
       ('TECH032', 'TECH', 'GraphQL', 'GraphQL을(를) 나타내는 코드. (코드: TECH032)', true, '0001234', NOW()),
       ('TECH033', 'TECH', 'gRPC', 'gRPC을(를) 나타내는 코드. (코드: TECH033)', true, '0001234', NOW()),
       ('TECH034', 'TECH', 'WebSocket', 'WebSocket을(를) 나타내는 코드. (코드: TECH034)', true, '0001234', NOW()),
       ('TECH035', 'TECH', 'SSE', 'SSE을(를) 나타내는 코드. (코드: TECH035)', true, '0001234', NOW()),
       ('TECH036', 'TECH', 'MySQL', 'MySQL을(를) 나타내는 코드. (코드: TECH036)', true, '0001234', NOW()),
       ('TECH037', 'TECH', 'MariaDB', 'MariaDB을(를) 나타내는 코드. (코드: TECH037)', true, '0001234', NOW()),
       ('TECH038', 'TECH', 'PostgreSQL', 'PostgreSQL을(를) 나타내는 코드. (코드: TECH038)', true, '0001234', NOW()),
       ('TECH039', 'TECH', 'SQLite', 'SQLite을(를) 나타내는 코드. (코드: TECH039)', true, '0001234', NOW()),
       ('TECH040', 'TECH', 'MS-SQL', 'MS-SQL을(를) 나타내는 코드. (코드: TECH040)', true, '0001234', NOW()),
       ('TECH041', 'TECH', 'Oracle', 'Oracle을(를) 나타내는 코드. (코드: TECH041)', true, '0001234', NOW()),
       ('TECH042', 'TECH', 'MongoDB', 'MongoDB을(를) 나타내는 코드. (코드: TECH042)', true, '0001234', NOW()),
       ('TECH043', 'TECH', 'DynamoDB', 'DynamoDB을(를) 나타내는 코드. (코드: TECH043)', true, '0001234', NOW()),
       ('TECH044', 'TECH', 'Flutter', 'Flutter을(를) 나타내는 코드. (코드: TECH044)', true, '0001234', NOW()),
       ('TECH045', 'TECH', 'React Native', 'React Native을(를) 나타내는 코드. (코드: TECH045)', true, '0001234', NOW()),
       ('TECH046', 'TECH', 'Ionic', 'Ionic을(를) 나타내는 코드. (코드: TECH046)', true, '0001234', NOW()),
       ('TECH047', 'TECH', 'Android (Java/Kotlin)', 'Android (Java/Kotlin)을(를) 나타내는 코드. (코드: TECH047)', true, '0001234',NOW()),
       ('TECH048', 'TECH', 'Objective-C', 'Objective-C을(를) 나타내는 코드. (코드: TECH048)', true, '0001234', NOW()),
       ('TECH049', 'TECH', 'Swift', 'Swift을(를) 나타내는 코드. (코드: TECH049)', true, '0001234', NOW()),
       ('TECH050', 'TECH', 'Kotlin Multiplatform Mobile (KMM)',
        'Kotlin Multiplatform Mobile (KMM)을(를) 나타내는 코드. (코드: TECH050)', true, '0001234', NOW()),
       ('TECH051', 'TECH', 'Xamarin', 'Xamarin을(를) 나타내는 코드. (코드: TECH051)', true, '0001234', NOW()),
       ('TECH052', 'TECH', 'C', 'C을(를) 나타내는 코드. (코드: TECH052)', true, '0001234', NOW()),
       ('TECH053', 'TECH', 'C++', 'C++을(를) 나타내는 코드. (코드: TECH053)', true, '0001234', NOW()),
       ('TECH054', 'TECH', 'FreeRTOS', 'FreeRTOS을(를) 나타내는 코드. (코드: TECH054)', true, '0001234', NOW()),
       ('TECH055', 'TECH', 'Zephyr', 'Zephyr을(를) 나타내는 코드. (코드: TECH055)', true, '0001234', NOW()),
       ('TECH056', 'TECH', 'Arduino Platform', 'Arduino Platform을(를) 나타내는 코드. (코드: TECH056)', true, '0001234', NOW()),
       ('TECH057', 'TECH', 'MicroPython', 'MicroPython을(를) 나타내는 코드. (코드: TECH057)', true, '0001234', NOW()),
       ('TECH058', 'TECH', 'MQTT', 'MQTT을(를) 나타내는 코드. (코드: TECH058)', true, '0001234', NOW()),
       ('TECH059', 'TECH', 'Docker', 'Docker을(를) 나타내는 코드. (코드: TECH059)', true, '0001234', NOW()),
       ('TECH060', 'TECH', 'Kubernetes', 'Kubernetes을(를) 나타내는 코드. (코드: TECH060)', true, '0001234', NOW()),
       ('TECH061', 'TECH', 'Nginx', 'Nginx을(를) 나타내는 코드. (코드: TECH061)', true, '0001234', NOW()),
       ('TECH062', 'TECH', 'Apache', 'Apache을(를) 나타내는 코드. (코드: TECH062)', true, '0001234', NOW()),
       ('TECH063', 'TECH', 'Jenkins', 'Jenkins을(를) 나타내는 코드. (코드: TECH063)', true, '0001234', NOW()),
       ('TECH064', 'TECH', 'GitHub Actions', 'GitHub Actions을(를) 나타내는 코드. (코드: TECH064)', true, '0001234', NOW()),
       ('TECH065', 'TECH', 'RabbitMQ', 'RabbitMQ을(를) 나타내는 코드. (코드: TECH065)', true, '0001234', NOW()),
       ('TECH066', 'TECH', 'Redis', 'Redis을(를) 나타내는 코드. (코드: TECH066)', true, '0001234', NOW()),
       ('TECH067', 'TECH', 'Kafka', 'Kafka을(를) 나타내는 코드. (코드: TECH067)', true, '0001234', NOW()),
       ('TECH068', 'TECH', 'OpenSearch', 'OpenSearch을(를) 나타내는 코드. (코드: TECH068)', true, '0001234', NOW()),
       ('TECH069', 'TECH', 'Elasticsearch', 'Elasticsearch을(를) 나타내는 코드. (코드: TECH069)', true, '0001234', NOW()),
       ('TECH070', 'TECH', 'Rush (Monorepo tool)', 'Rush (Monorepo tool)을(를) 나타내는 코드. (코드: TECH070)', true, '0001234',
        NOW());

-- == CLS (소속 반 종류) ==
INSERT IGNORE INTO sub_code (SUB_CODE, MAIN_CODE, SUB_CODE_NAME, SUB_CODE_DESCRIPTION, USE_YN, created_by, created_at)
VALUES ('CLS001', 'CLS', '1반', '1반을(를) 나타내는 코드. (코드: CLS001)', true, '0001234', NOW()),
       ('CLS002', 'CLS', '2반', '2반을(를) 나타내는 코드. (코드: CLS002)', true, '0001234', NOW()),
       ('CLS003', 'CLS', '3반', '3반을(를) 나타내는 코드. (코드: CLS003)', true, '0001234', NOW()),
       ('CLS004', 'CLS', '4반', '4반을(를) 나타내는 코드. (코드: CLS004)', true, '0001234', NOW()),
       ('CLS005', 'CLS', '5반', '5반을(를) 나타내는 코드. (코드: CLS005)', true, '0001234', NOW()),
       ('CLS006', 'CLS', '6반', '6반을(를) 나타내는 코드. (코드: CLS006)', true, '0001234', NOW()),
       ('CLS007', 'CLS', '7반', '7반을(를) 나타내는 코드. (코드: CLS007)', true, '0001234', NOW());

-- == RULE (팀 빌딩 규칙) ==
INSERT IGNORE INTO sub_code (SUB_CODE, MAIN_CODE, SUB_CODE_NAME, SUB_CODE_DESCRIPTION, USE_YN, created_by, created_at)
VALUES ('RULE001', 'RULE', 'SIZE_LIMIT', '6인 1팀 원칙', true, '0001234', NOW()),
       ('RULE002', 'RULE', 'MIN_MAJOR', '전공자 2인 이상', true, '0001234', NOW()),
       ('RULE003', 'RULE', 'MIN_NON_MAJOR', '비전공자 2인 이상', true, '0001234', NOW()),
       ('RULE004', 'RULE', 'BUILD_END_DATE', '팀 빌딩 종료 시간', true, '0001234', NOW());

-- ------------------------------------------------------------------
-- Sample data for Teams table (12 rows)
-- ------------------------------------------------------------------
INSERT INTO Teams (
    name,
    description,
    track_code,   -- TRK 하위 코드 (TRK001~TRK004)
    is_deleted,
    major_count,
    non_major_count,
    created_by,
    created_at
) VALUES
      ('팀 001',
       '웹 기술 기반 프로젝트 수행팀 A',
       'TRK001',
       FALSE,
       3,
       1,
       '0001234',
       NOW()),
      ('팀 002',
       '웹 기술 기반 프로젝트 수행팀 B',
       'TRK001',
       FALSE,
       2,
       1,
       '0001234',
       NOW()),
      ('팀 003',
       '웹 기술 기반 프로젝트 수행팀 C',
       'TRK001',
       FALSE,
       1,
       2,
       '0001234',
       NOW()),
      ('팀 004',
       '웹 디자인 전문팀 A',
       'TRK002',
       FALSE,
       3,
       1,
       '0001234',
       NOW()),
      ('팀 005',
       '웹 디자인 전문팀 B',
       'TRK002',
       FALSE,
       2,
       1,
       '0001234',
       NOW()),
      ('팀 006',
       '웹 디자인 전문팀 C',
       'TRK002',
       FALSE,
       1,
       2,
       '0001234',
       NOW()),
      ('팀 007',
       'AIoT 융합 프로젝트팀 A',
       'TRK003',
       FALSE,
       3,
       1,
       '0001234',
       NOW()),
      ('팀 008',
       'AIoT 융합 프로젝트팀 B',
       'TRK003',
       FALSE,
       2,
       1,
       '0001234',
       NOW()),
      ('팀 009',
       'AIoT 융합 프로젝트팀 C',
       'TRK003',
       FALSE,
       1,
       2,
       '0001234',
       NOW()),
      ('팀 010',
       '모바일 앱 개발팀 A',
       'TRK004',
       FALSE,
       3,
       1,
       '0001234',
       NOW()),
      ('팀 011',
       '모바일 앱 개발팀 B',
       'TRK004',
       FALSE,
       2,
       1,
       '0001234',
       NOW()),
      ('팀 012',
       '모바일 앱 개발팀 C',
       'TRK004',
       FALSE,
       1,
       2,
       '0001234',
       NOW());

-- ------------------------------------------------------------------
-- Sample data for Recruitments table
-- ------------------------------------------------------------------
INSERT INTO Recruitments (
    position_code,  -- POS 하위 코드 (POS001~POS007)
    team_id,
    created_by,
    created_at
) VALUES
      -- 팀 001
      ('POS001', 1,  '000000', NOW()),
      ('POS002', 1,  '000000', NOW()),
      -- 팀 002
      ('POS003', 2,  '000000', NOW()),
      ('POS004', 2,  '000000', NOW()),
      -- 팀 003
      ('POS005', 3,  '000000', NOW()),
      ('POS006', 3,  '000000', NOW()),
      -- 팀 004
      ('POS001', 4,  '000000', NOW()),
      ('POS007', 4,  '000000', NOW()),
      -- 팀 005
      ('POS002', 5,  '000000', NOW()),
      ('POS003', 5,  '000000', NOW()),
      -- 팀 006
      ('POS004', 6,  '000000', NOW()),
      ('POS005', 6,  '000000', NOW()),
      -- 팀 007
      ('POS006', 7,  '000000', NOW()),
      -- 팀 008
      ('POS007', 8,  '000000', NOW()),
      -- 팀 009
      ('POS001', 9,  '000000', NOW()),
      -- 팀 010
      ('POS002', 10, '000000', NOW()),
      -- 팀 011
      ('POS003', 11, '000000', NOW()),
      -- 팀 012
      ('POS004', 12, '000000', NOW())
;

--------------------------------------------------------
-- Student 테이블에 데이터 삽입
--------------------------------------------------------
INSERT IGNORE INTO students (
    student_id,
    name,
    major_yn,
    team_id,
    class_code,
    created_by,
    created_at
) VALUES
  -- team 1 (4명)
  (1300001, '김서준', TRUE,  1, 'CLS001', '0001234', NOW()),
  (1300002, '이하윤', FALSE, 1, 'CLS001', '0001234', NOW()),
  (1300003, '박지호', TRUE,  1, 'CLS001', '0001234', NOW()),
  (1300004, '최서아', FALSE, 1, 'CLS001', '0001234', NOW()),

  -- team 2 (6명)
  (1300007, '조유준', TRUE,  2, 'CLS001', '0001234', NOW()),
  (1300008, '윤하은', FALSE, 2, 'CLS001', '0001234', NOW()),
  (1300009, '장시우', TRUE,  2, 'CLS001', '0001234', NOW()),
  (1300010, '임수아', FALSE, 2, 'CLS001', '0001234', NOW()),
  (1300011, '한도윤', TRUE,  2, 'CLS001', '0001234', NOW()),
  (1300012, '오채원', FALSE, 2, 'CLS001', '0001234', NOW()),

  -- team 3 (6명)
  (1300013, '서예준', TRUE,  3, 'CLS001', '0001234', NOW()),
  (1300014, '신유나', FALSE, 3, 'CLS001', '0001234', NOW()),
  (1300015, '권민준', TRUE,  3, 'CLS001', '0001234', NOW()),
  (1300016, '황다은', FALSE, 3, 'CLS001', '0001234', NOW()),
  (1300017, '안주원', TRUE,  3, 'CLS001', '0001234', NOW()),
  (1300018, '송지우', FALSE, 3, 'CLS001', '0001234', NOW()),

  -- team 4 (6명)
  (1300019, '유하준', TRUE,  4, 'CLS001', '0001234', NOW()),
  (1300020, '전서윤', FALSE, 4, 'CLS001', '0001234', NOW()),
  (1300021, '정서아', TRUE,  4, 'CLS001', '0001234', NOW()),
  (1300022, '강지안', FALSE, 4, 'CLS001', '0001234', NOW()),
  (1300023, '최은우', TRUE,  4, 'CLS001', '0001234', NOW()),
  (1300024, '김하윤', FALSE, 4, 'CLS001', '0001234', NOW()),

  -- team 5 (6명)
  (1300025, '이시우', TRUE,  5, 'CLS001', '0001234', NOW()),
  (1300026, '박서준', FALSE, 5, 'CLS001', '0001234', NOW()),
  (1300027, '조지호', TRUE,  5, 'CLS001', '0001234', NOW()),
  (1300028, '윤수아', FALSE, 5, 'CLS001', '0001234', NOW()),
  (1300029, '장유준', TRUE,  5, 'CLS001', '0001234', NOW()),
  (1300030, '임채원', FALSE, 5, 'CLS001', '0001234', NOW()),

  -- team 6 (6명)
  (1300031, '한민준', TRUE,  6, 'CLS001', '0001234', NOW()),
  (1300032, '오유나', FALSE, 6, 'CLS001', '0001234', NOW()),
  (1300033, '서하준', TRUE,  6, 'CLS001', '0001234', NOW()),
  (1300034, '신지우', FALSE, 6, 'CLS001', '0001234', NOW()),
  (1300035, '권도윤', TRUE,  6, 'CLS001', '0001234', NOW()),
  (1300036, '황서윤', FALSE, 6, 'CLS001', '0001234', NOW()),

  -- team 7 (5명)
  (1300037, '안예준', TRUE,  7, 'CLS001', '0001234', NOW()),
  (1300038, '송다은', FALSE, 7, 'CLS001', '0001234', NOW()),
  (1300039, '유주원', TRUE,  7, 'CLS001', '0001234', NOW()),
  (1300040, '전하은', FALSE, 7, 'CLS001', '0001234', NOW()),
  (1300041, '오지호', TRUE,  7, 'CLS001', '0001234', NOW()),

  -- team 8 (4명)
  (1300043, '임은우', TRUE,  8, 'CLS001', '0001234', NOW()),
  (1300044, '장하윤', FALSE, 8, 'CLS001', '0001234', NOW()),
  (1300045, '윤서준', TRUE,  8, 'CLS001', '0001234', NOW()),
  (1300046, '조지안', FALSE, 8, 'CLS001', '0001234', NOW()),

  -- team 9 (1명)
  (1300047, '강시우', TRUE,  9, 'CLS001', '0001234', NOW()),

  -- team 10 (1명)
  (1300048, '정수아', FALSE,10, 'CLS001', '0001234', NOW()),

  -- team 11 (1명)
  (1300049, '최유준', TRUE,  11, 'CLS001', '0001234', NOW()),
  (1300042, '한서아', FALSE, 11, 'CLS001', '0001234', NOW()),

  -- team 12 (3명)
  (1300005, '정은우', TRUE,  12, 'CLS001', '0001234', NOW()),
  (1300006, '강지안', FALSE, 12, 'CLS001', '0001234', NOW()),
  (1300050, '박채원', FALSE, 12, 'CLS001', '0001234', NOW());

----------------------------------------------------------
-- StudentInfo 테이블에 데이터 삽입
----------------------------------------------------------

INSERT IGNORE INTO student_info (student_id, tech_stack, strength, profile_image_url, portfolio_saved_filename, portfolio_original_filename, description, track_code, position_code, goal_code, mbti_code, created_by, created_at)
VALUES
(1300001, 'TECH001,TECH002,TECH003,TECH004,TECH005', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=김서준', NULL, NULL, NULL, 'TRK001', 'POS001', 'GOAL001', 'MBTI001', '001234', NOW()),
(1300002, 'TECH006,TECH007,TECH008,TECH009,TECH010,TECH011,TECH012,TECH013,TECH014,TECH015', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=이하윤', NULL, NULL, NULL, 'TRK001', 'POS001', 'GOAL002', 'MBTI002', '001234', NOW()),
(1300003, 'TECH016,TECH017,TECH018,TECH019,TECH020,TECH021,TECH022,TECH023,TECH024,TECH025,TECH026,TECH027,TECH028,TECH029,TECH030', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=박지호', NULL, NULL, NULL, 'TRK001', 'POS001', 'GOAL003', 'MBTI003', '001234', NOW()),
(1300004, 'TECH031,TECH032,TECH033,TECH034,TECH035,TECH036,TECH037,TECH038,TECH039,TECH040,TECH041,TECH042,TECH043,TECH044,TECH045,TECH046,TECH047,TECH048,TECH049,TECH050', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=최서아', NULL, NULL, NULL, 'TRK001', 'POS001', 'GOAL001', 'MBTI004', '001234', NOW()),
(1300005, 'TECH015,TECH052,TECH003,TECH042,TECH022,TECH059,TECH047,TECH006,TECH028,TECH055,TECH007,TECH016,TECH062,TECH038,TECH026,TECH034,TECH063,TECH056,TECH020,TECH048,TECH066,TECH070,TECH050,TECH011,TECH005,TECH033,TECH041,TECH008,TECH061,TECH045,TECH013,TECH023,TECH039,TECH010,TECH065,TECH060,TECH037,TECH017,TECH040,TECH054,TECH069,TECH046', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=정은우', NULL, NULL, NULL, 'TRK004', 'POS004', 'GOAL003', 'MBTI016', '001234', NOW()),
(1300006, 'TECH023,TECH064,TECH060,TECH058,TECH042,TECH041,TECH020,TECH029,TECH005,TECH031,TECH063,TECH065,TECH069,TECH051', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=강지안', NULL, NULL, NULL, 'TRK004', 'POS004', 'GOAL001', 'MBTI001', '001234', NOW()),
(1300007, 'TECH046,TECH047,TECH048,TECH049,TECH050,TECH051,TECH052,TECH053,TECH054,TECH055,TECH056,TECH057,TECH058,TECH059,TECH060,TECH061,TECH062,TECH063,TECH064,TECH065,TECH066,TECH067,TECH068,TECH069,TECH070', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=조유준', NULL, NULL, NULL, 'TRK001', 'POS003', 'GOAL002', 'MBTI005', '001234', NOW()),
(1300008, 'TECH001,TECH002,TECH003,TECH004,TECH005,TECH006,TECH007,TECH008,TECH009,TECH010,TECH011,TECH012,TECH013,TECH014,TECH015,TECH016,TECH017,TECH018,TECH019,TECH020,TECH021,TECH022,TECH023,TECH024,TECH025,TECH026,TECH027,TECH028,TECH029,TECH030,TECH031,TECH032,TECH033,TECH034,TECH035,TECH036,TECH037,TECH038,TECH039,TECH040,TECH041,TECH042,TECH043,TECH044,TECH045,TECH046,TECH047,TECH048,TECH049,TECH050,TECH051,TECH052,TECH053,TECH054,TECH055,TECH056,TECH057,TECH058,TECH059,TECH060,TECH061,TECH062,TECH063,TECH064,TECH065,TECH066,TECH067,TECH068,TECH069,TECH070', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=윤하은', NULL, NULL, NULL, 'TRK001', 'POS003', 'GOAL003', 'MBTI006', '001234', NOW()),
(1300009, 'TECH070', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=장시우', NULL, NULL, NULL, 'TRK001', 'POS003', 'GOAL001', 'MBTI007', '001234', NOW()),
(1300010, 'TECH001,TECH070', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=임수아', NULL, NULL, NULL, 'TRK001', 'POS003', 'GOAL002', 'MBTI008', '001234', NOW()),
(1300011, 'TECH010,TECH020,TECH030', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=한도윤', NULL, NULL, NULL, 'TRK001', 'POS003', 'GOAL003', 'MBTI009', '001234', NOW()),
(1300012, 'TECH005,TECH015,TECH025,TECH035', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x:notionists-neutral/svg?seed=오채원', NULL, NULL, NULL, 'TRK001', 'POS003', 'GOAL001', 'MBTI010', '001234', NOW()),
(1300013, 'TECH002,TECH012,TECH022,TECH032,TECH042,TECH052', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x:notionists-neutral/svg?seed=서예준', NULL, NULL, NULL, 'TRK001', 'POS005', 'GOAL002', 'MBTI011', '001234', NOW()),
(1300014, 'TECH003,TECH013,TECH023,TECH033,TECH043,TECH053,TECH063', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x:notionists-neutral/svg?seed=신유나', NULL, NULL, NULL, 'TRK001', 'POS005', 'GOAL003', 'MBTI012', '001234', NOW()),
(1300015, 'TECH004,TECH014,TECH024,TECH034,TECH044,TECH054,TECH064,TECH070', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x:notionists-neutral/svg?seed=권민준', NULL, NULL, NULL, 'TRK001', 'POS005', 'GOAL001', 'MBTI013', '001234', NOW()),
(1300016, 'TECH001,TECH011,TECH021,TECH031,TECH041,TECH051,TECH061,TECH010,TECH020', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x:notionists-neutral/svg?seed=황다은', NULL, NULL, NULL, 'TRK001', 'POS005', 'GOAL002', 'MBTI014', '001234', NOW()),
(1300017, 'TECH006,TECH016,TECH026,TECH036,TECH046,TECH056,TECH066,TECH007,TECH017,TECH027,TECH037', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x:notionists-neutral/svg?seed=안주원', NULL, NULL, NULL, 'TRK001', 'POS005', 'GOAL003', 'MBTI015', '001234', NOW()),
(1300018, 'TECH008,TECH018,TECH028,TECH038,TECH048,TECH058,TECH009,TECH019,TECH029,TECH039,TECH049,TECH059', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x:notionists-neutral/svg?seed=송지우', NULL, NULL, NULL, 'TRK001', 'POS005', 'GOAL001', 'MBTI016', '001234', NOW()),
(1300019, 'TECH001,TECH005,TECH010,TECH015,TECH020,TECH025,TECH030,TECH035,TECH040,TECH045,TECH050,TECH055,TECH060', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x:notionists-neutral/svg?seed=유하준', NULL, NULL, NULL, 'TRK002', 'POS001', 'GOAL002', 'MBTI001', '001234', NOW()),
(1300020, 'TECH002,TECH007,TECH012,TECH017,TECH022,TECH027,TECH032,TECH037,TECH042,TECH047,TECH052,TECH057,TECH064,TECH069', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x:notionists-neutral/svg?seed=전서윤', NULL, NULL, NULL, 'TRK002', 'POS001', 'GOAL003', 'MBTI002', '001234', NOW()),
(1300021, 'TECH003,TECH008,TECH013,TECH018,TECH023,TECH028,TECH033,TECH038,TECH043,TECH048,TECH053,TECH058,TECH063,TECH068,TECH069,TECH070', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x:notionists-neutral/svg?seed=정서아', NULL, NULL, NULL, 'TRK002', 'POS001', 'GOAL001', 'MBTI003', '001234', NOW()),
(1300022, 'TECH004,TECH009,TECH014,TECH019,TECH024,TECH029,TECH034,TECH039,TECH044,TECH049,TECH054,TECH059,TECH064,TECH069,TECH002,TECH007,TECH012,TECH017', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x:notionists-neutral/svg?seed=강지안', NULL, NULL, NULL, 'TRK002', 'POS001', 'GOAL002', 'MBTI004', '001234', NOW()),
(1300023, 'TECH045,TECH029,TECH009,TECH006,TECH004,TECH032,TECH026,TECH054,TECH002,TECH040,TECH010', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=최은우', NULL, NULL, NULL, 'TRK002', 'POS001', 'GOAL003', 'MBTI005', '001234', NOW()),
(1300024, 'TECH017,TECH061,TECH015,TECH028,TECH060,TECH033,TECH048,TECH011,TECH039,TECH062,TECH064,TECH046,TECH008,TECH050,TECH053,TECH063,TECH020,TECH007,TECH038,TECH002,TECH054,TECH037,TECH044,TECH025,TECH026,TECH013,TECH005,TECH052,TECH041,TECH016,TECH056,TECH057,TECH058,TECH049,TECH003', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=김하윤', NULL, NULL, NULL, 'TRK002', 'POS001', 'GOAL001', 'MBTI006', '001234', NOW()),
(1300025, 'TECH069,TECH055,TECH048,TECH009,TECH065,TECH044,TECH002,TECH070,TECH027,TECH053,TECH032,TECH007,TECH028,TECH024,TECH041,TECH054,TECH030,TECH046,TECH010,TECH058,TECH012,TECH047,TECH034,TECH042,TECH018,TECH040,TECH035,TECH031,TECH063,TECH051,TECH038,TECH061,TECH021,TECH016,TECH006,TECH039,TECH029,TECH037,TECH049,TECH019,TECH020,TECH022,TECH013,TECH011,TECH001,TECH033,TECH062,TECH036,TECH025', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=이시우', NULL, NULL, NULL, 'TRK002', 'POS002', 'GOAL002', 'MBTI007', '001234', NOW()),
(1300026, 'TECH046,TECH034,TECH044,TECH036,TECH067,TECH002,TECH025,TECH006,TECH016,TECH047,TECH027,TECH032,TECH066,TECH049,TECH062,TECH045,TECH031,TECH042,TECH070,TECH059,TECH029,TECH065,TECH063,TECH019,TECH015,TECH026,TECH056,TECH020,TECH038,TECH024,TECH054,TECH058', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=박서준', NULL, NULL, NULL, 'TRK002', 'POS002', 'GOAL003', 'MBTI008', '001234', NOW()),
(1300027, 'TECH055,TECH043,TECH046,TECH059,TECH035,TECH040,TECH033,TECH015,TECH008,TECH047,TECH013,TECH021,TECH062,TECH048,TECH066,TECH049,TECH045,TECH012,TECH060,TECH014,TECH057,TECH031,TECH018,TECH061,TECH038,TECH034,TECH039,TECH019,TECH007,TECH052,TECH069,TECH063,TECH024,TECH053,TECH020,TECH001,TECH009,TECH050,TECH003,TECH002,TECH064,TECH010,TECH023,TECH005,TECH067,TECH025,TECH016,TECH004,TECH056', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=조지호', NULL, NULL, NULL, 'TRK002', 'POS002', 'GOAL001', 'MBTI009', '001234', NOW()),
(1300028, 'TECH061,TECH062,TECH057,TECH044,TECH024,TECH007,TECH033,TECH070,TECH056,TECH031,TECH008,TECH053,TECH005,TECH026,TECH032,TECH058,TECH037,TECH041,TECH067,TECH004,TECH010,TECH050,TECH054,TECH020,TECH006,TECH016,TECH060,TECH036,TECH027,TECH039,TECH059,TECH015,TECH034,TECH025,TECH029,TECH043,TECH047,TECH028,TECH038,TECH019,TECH069', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=윤수아', NULL, NULL, NULL, 'TRK002', 'POS002', 'GOAL002', 'MBTI010', '001234', NOW()),
(1300029, 'TECH061,TECH062,TECH057,TECH044,TECH024,TECH007,TECH033,TECH070,TECH056,TECH031,TECH008,TECH053,TECH005,TECH026,TECH032,TECH058,TECH037,TECH041,TECH067,TECH004,TECH010,TECH050,TECH054,TECH020,TECH006,TECH016,TECH060,TECH036,TECH027,TECH039,TECH059,TECH015,TECH034,TECH025,TECH029,TECH043,TECH047,TECH028,TECH038,TECH019,TECH069', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=윤수민', NULL, NULL, NULL, 'TRK002', 'POS002', 'GOAL002', 'MBTI010', '001234', NOW()),
(1300030, 'TECH058,TECH061,TECH038,TECH005,TECH030,TECH037,TECH065,TECH045,TECH056,TECH066,TECH067,TECH044,TECH015,TECH017,TECH051,TECH062,TECH041,TECH068,TECH043,TECH013,TECH028,TECH008,TECH035,TECH070,TECH042,TECH010,TECH018,TECH063,TECH060,TECH004,TECH011,TECH020,TECH064,TECH019,TECH029,TECH049,TECH069,TECH039,TECH026,TECH031,TECH009,TECH057,TECH059,TECH016,TECH047,TECH003,TECH033,TECH002,TECH014,TECH040,TECH024,TECH034,TECH001,TECH025,TECH048,TECH021,TECH022', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=임채원', NULL, NULL, NULL, 'TRK002', 'POS002', 'GOAL001', 'MBTI012', '001234', NOW()),
(1300031, 'TECH035,TECH006,TECH023,TECH061,TECH057,TECH036,TECH024', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=한민준', NULL, NULL, NULL, 'TRK002', 'POS004', 'GOAL002', 'MBTI013', '001234', NOW()),
(1300032, 'TECH063,TECH012,TECH061,TECH045,TECH053,TECH043,TECH042,TECH065,TECH007,TECH055,TECH011,TECH022,TECH027,TECH067,TECH032,TECH019,TECH070,TECH026,TECH049,TECH036,TECH003,TECH030,TECH006,TECH021,TECH017,TECH047,TECH008,TECH066,TECH033,TECH001,TECH035,TECH052,TECH058,TECH004,TECH013,TECH034,TECH024,TECH056,TECH029,TECH025,TECH002,TECH062,TECH009,TECH018,TECH005,TECH010,TECH015,TECH023,TECH016,TECH037,TECH041,TECH044,TECH048,TECH031,TECH020,TECH028,TECH064,TECH039,TECH057,TECH051', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=오유나', NULL, NULL, NULL, 'TRK002', 'POS004', 'GOAL003', 'MBTI014', '001234', NOW()),
(1300033, 'TECH016,TECH020,TECH064,TECH038,TECH066,TECH035,TECH054,TECH068,TECH031,TECH062,TECH070,TECH030,TECH036,TECH010,TECH025,TECH013,TECH039,TECH033,TECH048,TECH009,TECH005,TECH018,TECH027,TECH022,TECH053,TECH049,TECH001,TECH019,TECH069,TECH067,TECH041,TECH032,TECH057,TECH029,TECH065,TECH061,TECH023,TECH047,TECH056,TECH015,TECH059,TECH011,TECH028,TECH007,TECH034,TECH008,TECH043,TECH055,TECH063,TECH014,TECH002,TECH037,TECH060,TECH026,TECH017,TECH024,TECH045,TECH058,TECH003,TECH046,TECH044,TECH012,TECH006,TECH052', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=서하준', NULL, NULL, NULL, 'TRK002', 'POS004', 'GOAL001', 'MBTI015', '001234', NOW()),
(1300034, 'TECH057,TECH013,TECH068,TECH059,TECH002,TECH019,TECH053,TECH056,TECH042,TECH010,TECH005,TECH031,TECH051,TECH017,TECH022,TECH040,TECH045', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=신지우', NULL, NULL, NULL, 'TRK002', 'POS004', 'GOAL002', 'MBTI016', '001234', NOW()),
(1300035, 'TECH011,TECH043,TECH049,TECH041,TECH063,TECH005,TECH009,TECH016,TECH067,TECH044,TECH059,TECH019,TECH015,TECH048,TECH006,TECH028,TECH007,TECH068,TECH062,TECH046,TECH054,TECH029,TECH070,TECH045,TECH020,TECH002,TECH003,TECH021,TECH004,TECH060,TECH023,TECH024,TECH055,TECH010,TECH066,TECH034,TECH027,TECH012,TECH057,TECH056,TECH061,TECH051,TECH038,TECH013,TECH053,TECH022,TECH008,TECH036,TECH052,TECH065,TECH039,TECH058,TECH064,TECH025,TECH018', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=권도윤', NULL, NULL, NULL, 'TRK002', 'POS004', 'GOAL003', 'MBTI001', '001234', NOW()),
(1300036, 'TECH060,TECH037,TECH021,TECH010,TECH057,TECH045', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=황서윤', NULL, NULL, NULL, 'TRK002', 'POS004', 'GOAL001', 'MBTI002', '001234', NOW()),
(1300037, 'TECH055,TECH033,TECH059,TECH039,TECH026,TECH050,TECH062,TECH007,TECH016,TECH025,TECH037,TECH023,TECH060,TECH019,TECH045,TECH057,TECH002,TECH043,TECH066,TECH018,TECH001,TECH058,TECH044,TECH004,TECH067,TECH032,TECH070,TECH015,TECH046,TECH068,TECH053,TECH013,TECH017,TECH009,TECH063,TECH003,TECH020,TECH029,TECH035,TECH048,TECH012,TECH024,TECH005', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=안예준', NULL, NULL, NULL, 'TRK003', 'POS006', 'GOAL002', 'MBTI003', '001234', NOW()),
(1300038, 'TECH038,TECH042,TECH054,TECH023,TECH026,TECH017,TECH047,TECH034,TECH033,TECH059,TECH018,TECH068,TECH011,TECH065,TECH053,TECH031', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=송다은', NULL, NULL, NULL, 'TRK003', 'POS006', 'GOAL003', 'MBTI004', '001234', NOW()),
(1300039, 'TECH044,TECH015,TECH060,TECH010,TECH019,TECH029,TECH051,TECH062,TECH055,TECH052,TECH036,TECH024,TECH006,TECH064,TECH026,TECH001,TECH017,TECH035,TECH008,TECH030,TECH059,TECH070,TECH048,TECH049,TECH054,TECH038,TECH025,TECH041,TECH050,TECH007,TECH069,TECH031,TECH002,TECH068,TECH021,TECH040,TECH005,TECH057,TECH020,TECH037,TECH014,TECH004', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=유주원', NULL, NULL, NULL, 'TRK003', 'POS006', 'GOAL001', 'MBTI005', '001234', NOW()),
(1300040, 'TECH006,TECH005,TECH039,TECH064,TECH015,TECH013,TECH031,TECH057,TECH035,TECH009,TECH025,TECH030,TECH024,TECH043,TECH048,TECH045,TECH062,TECH027,TECH038,TECH056,TECH047,TECH010', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=전하은', NULL, NULL, NULL, 'TRK003', 'POS006', 'GOAL002', 'MBTI006', '001234', NOW()),
(1300041, 'TECH013,TECH063,TECH053,TECH036,TECH005,TECH048,TECH028,TECH029,TECH069,TECH061,TECH016,TECH055,TECH024,TECH007,TECH044,TECH058,TECH035,TECH042,TECH023,TECH004,TECH026,TECH018,TECH070,TECH008,TECH030,TECH006,TECH043,TECH014,TECH068,TECH041,TECH039,TECH002,TECH051,TECH022,TECH060,TECH009,TECH056,TECH066,TECH034,TECH019,TECH057,TECH027,TECH064,TECH047,TECH011,TECH025,TECH033,TECH020,TECH001,TECH054,TECH059,TECH052,TECH049,TECH021,TECH045,TECH040,TECH050,TECH037', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=오지호', NULL, NULL, NULL, 'TRK003', 'POS006', 'GOAL003', 'MBTI007', '001234', NOW()),
(1300042, 'TECH056,TECH003,TECH059,TECH010,TECH041,TECH055,TECH052,TECH046,TECH066,TECH027', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=한서아', NULL, NULL, NULL, 'TRK004', 'POS003', 'GOAL002', 'MBTI015', '001234', NOW()),
(1300043, 'TECH002,TECH046,TECH031,TECH042,TECH003,TECH023,TECH034,TECH004,TECH009,TECH048,TECH027,TECH064,TECH008,TECH061,TECH005,TECH068,TECH029,TECH050,TECH024,TECH033,TECH038', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=임은우', NULL, NULL, NULL, 'TRK003', 'POS007', 'GOAL001', 'MBTI008', '001234', NOW()),
(1300044, 'TECH058,TECH065,TECH029,TECH006,TECH039,TECH059,TECH004,TECH064,TECH031,TECH055,TECH026,TECH028,TECH044,TECH007,TECH032,TECH046,TECH068,TECH005', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=장하윤', NULL, NULL, NULL, 'TRK003', 'POS007', 'GOAL002', 'MBTI009', '001234', NOW()),
(1300045, 'TECH042,TECH019,TECH009,TECH017,TECH036,TECH070,TECH049,TECH039,TECH034,TECH069,TECH030,TECH033,TECH063,TECH028,TECH007', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=윤서준', NULL, NULL, NULL, 'TRK003', 'POS007', 'GOAL003', 'MBTI010', '001234', NOW()),
(1300046, 'TECH028,TECH056,TECH058,TECH030,TECH053,TECH044,TECH059,TECH026,TECH027,TECH047,TECH007,TECH021,TECH070,TECH064,TECH043,TECH017,TECH024,TECH010,TECH065', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=조지안', NULL, NULL, NULL, 'TRK003', 'POS007', 'GOAL001', 'MBTI011', '001234', NOW()),
(1300047, 'TECH009,TECH012,TECH011,TECH069,TECH056,TECH013,TECH048,TECH052,TECH070,TECH036,TECH004,TECH038,TECH061,TECH058,TECH022,TECH043,TECH008,TECH027,TECH023,TECH055,TECH049,TECH028,TECH047,TECH060,TECH019,TECH039,TECH020,TECH063,TECH007,TECH037,TECH033,TECH014,TECH010,TECH031,TECH015,TECH042,TECH051,TECH024,TECH054,TECH025,TECH062,TECH046,TECH032,TECH026,TECH045,TECH018,TECH044,TECH040,TECH066,TECH021,TECH041,TECH001,TECH030,TECH029,TECH006,TECH005,TECH067,TECH065,TECH057,TECH016,TECH068,TECH017,TECH003,TECH035,TECH002', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=강시우', NULL, NULL, NULL, 'TRK003', 'POS001', 'GOAL002', 'MBTI012', '001234', NOW()),
(1300048, 'TECH009,TECH019,TECH004,TECH012,TECH028,TECH049,TECH054,TECH030,TECH022,TECH011,TECH024,TECH020,TECH047,TECH021,TECH050,TECH037,TECH039,TECH006,TECH068,TECH010,TECH061,TECH065,TECH040,TECH052,TECH044,TECH053,TECH018,TECH029,TECH066,TECH032,TECH064,TECH043,TECH027,TECH046,TECH014,TECH033,TECH008,TECH023,TECH042,TECH058,TECH051,TECH062,TECH048,TECH069,TECH016,TECH017,TECH041,TECH063,TECH002,TECH034,TECH013,TECH060,TECH001,TECH007,TECH035,TECH031', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=정수아', NULL, NULL, NULL, 'TRK004', 'POS002', 'GOAL003', 'MBTI013', '001234', NOW()),
(1300049, 'TECH033,TECH038,TECH042,TECH016,TECH001,TECH064,TECH056,TECH012,TECH009,TECH025,TECH035,TECH046,TECH015,TECH070,TECH036,TECH054,TECH043,TECH052,TECH023,TECH005,TECH026,TECH048', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=최유준', NULL, NULL, NULL, 'TRK004', 'POS003', 'GOAL001', 'MBTI014', '001234', NOW()),
(1300050, 'TECH008,TECH046,TECH065,TECH010,TECH040,TECH060,TECH058,TECH003,TECH004,TECH024,TECH054,TECH019,TECH005,TECH042,TECH056,TECH055,TECH006,TECH066,TECH039,TECH033,TECH025,TECH030,TECH038,TECH036,TECH063,TECH029,TECH037,TECH057,TECH013,TECH021,TECH052,TECH031,TECH051,TECH067,TECH062,TECH045,TECH007,TECH022,TECH068,TECH017', '플젝경험;팀장경험;해커톤수상', 'https://api.dicebear.com/9.x/notionists-neutral/svg?seed=박채원', NULL, NULL, NULL, 'TRK004', 'POS004', 'GOAL002', 'MBTI002', '001234', NOW());
