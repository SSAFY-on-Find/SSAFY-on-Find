import os
import mysql.connector
from dotenv import load_dotenv

# .env 파일에서 환경 변수 로드
load_dotenv()

def get_db_connection():
    """MySQL 데이터베이스 커넥션을 생성하고 반환합니다."""
    try:
        conn = mysql.connector.connect(
            host=os.getenv("MYSQL_HOST"),
            port=os.getenv("MYSQL_PORT"),
            database=os.getenv("MYSQL_DATABASE"),
            user=os.getenv("MYSQL_USER"),
            password=os.getenv("MYSQL_PASSWORD")
        )
        return conn
    except mysql.connector.Error as e:
        print(f"DB 연결 오류: {e}")
        return None

def fetch_students_data():
    """모든 학생의 상세 정보를 DB에서 조회하여 리스트로 반환합니다."""
    conn = get_db_connection()
    if conn is None:
        return []

    # 실제 DB의 테이블/컬럼명에 맞춰 쿼리를 수정해야 할 수 있습니다.
    # SubCodes 테이블이 코드명(예: 'BE', 'FE')을 sub_code_name 컬럼에 저장한다고 가정했습니다.
    query = """
     SELECT
        s.student_id,
        s.name,
        s.major_yn,
        si.profile_image_url,
        si.strength,
        si.description,
        pos.sub_code_name AS `position`,
        goal.sub_code_name AS `goal`,
        mbti.sub_code_name AS `mbti`,
        (
            SELECT GROUP_CONCAT(sc.sub_code_name SEPARATOR ',')
            FROM sub_code sc
            WHERE FIND_IN_SET(sc.sub_code, si.tech_stack)
            AND sc.main_code = 'TECH'
        ) AS `tech_stack`
    FROM
        students s
    LEFT JOIN student_info si ON s.student_id = si.student_id
    LEFT JOIN sub_code pos ON si.position_code = pos.sub_code
    LEFT JOIN sub_code goal ON si.goal_code = goal.sub_code
    LEFT JOIN sub_code mbti ON si.mbti_code = mbti.sub_code;
    """
    
    students = []
    try:
        # dictionary=True 옵션은 결과를 딕셔너리 형태로 받기 위함입니다.
        with conn.cursor(dictionary=True) as cursor:
            cursor.execute(query)
            students = cursor.fetchall()
            for student in students:
                student['major_yn'] = bool(student['major_yn'])
    except mysql.connector.Error as e:
        print(f"쿼리 실행 오류: {e}")
    finally:
        if conn.is_connected():
            conn.close()
            
    return students