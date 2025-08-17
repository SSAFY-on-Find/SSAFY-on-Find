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
    ('POS002', 5, NOW(), NOW()); -- 백엔드