from dotenv import load_dotenv
load_dotenv()

import json
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from langchain_openai import ChatOpenAI
from langchain.prompts import PromptTemplate
from pydantic import BaseModel, Field
from typing import List, Dict, Any

# database.py에서 만든 데이터 로딩 함수를 다시 임포트합니다.
from database import fetch_students_data

# --- FastAPI 앱 생성 및 CORS 설정 ---
app = FastAPI()

origins = ["http://localhost:5173" ] 
app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# GMS Key를 사용하기 위한 LLM 설정
llm = ChatOpenAI(
    model="gpt-4.1-nano", 
    temperature=0.2,
    openai_api_base="https://gms.ssafy.io/gmsapi/api.openai.com/v1"
)

# --- 프롬프트를 파일에서 읽어오는 로직 ---
def load_prompt(file_path: str) -> str:
    with open(file_path, "r", encoding="utf-8") as f:
        return f.read()

PROMPT_FILE_PATH = "prompt_template.txt"
prompt_template_string = load_prompt(PROMPT_FILE_PATH)
prompt_template = PromptTemplate.from_template(prompt_template_string)
chain = prompt_template | llm


@app.post("/api/v1/recommendations/{student_id}")
async def get_recommendations(student_id: int):
    # 1. DB에서 전체 학생 데이터 로드
    all_students = fetch_students_data()
    if not all_students:
        raise HTTPException(status_code=503, detail="DB에서 학생 정보를 가져올 수 없습니다.")

    # 2. 기준 학생과 추천 대상 학생들을 분리
    base_student_list = [s for s in all_students if s['student_id'] == student_id]
    if not base_student_list:
        raise HTTPException(status_code=404, detail=f"ID {student_id}에 해당하는 학생을 찾을 수 없습니다.")
    base_student = base_student_list[0]
    
    candidate_students = [s for s in all_students if s['student_id'] != student_id]
    if not candidate_students:
        raise HTTPException(status_code=404, detail="추천할 다른 학생이 없습니다.")
    
    # 3. LLM에 전달할 데이터 포맷팅
    base_student_info_str = json.dumps(base_student, ensure_ascii=False)
    candidate_students_info_str = json.dumps(candidate_students, ensure_ascii=False)
    print(candidate_students_info_str)
    # 4. LangChain 실행하여 LLM에 요청
    llm_response = await chain.ainvoke({
        "base_student_info": base_student_info_str,
        "candidate_students_info": candidate_students_info_str
    })
    
    # 5. LLM 응답 파싱 및 반환 (마크다운 처리 로직 포함)
    try:
        response_text = llm_response.content.strip()
        if response_text.startswith("```json"):
            start_index = response_text.find('[')
            end_index = response_text.rfind(']')
            json_string = response_text[start_index:end_index+1]
        else:
            json_string = response_text
        
        recommendations = json.loads(json_string)
        recommendations.sort(key=lambda x: x['score'], reverse=True)
        return recommendations[:5]
    except (json.JSONDecodeError, TypeError):
        raise HTTPException(status_code=500, detail="LLM의 응답을 파싱하는 데 실패했습니다.")