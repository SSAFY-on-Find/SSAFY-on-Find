import { useMutation } from "@tanstack/react-query"
import { Award, Lightbulb, Users } from "lucide-react"

import api from "@/apis"
import { useUserStore } from "@/stores/userStore"

// API 응답 데이터 타입을 정의합니다.
interface Recommendation {
  student_id: number
  score: number
  reason: string
}

// API 호출 함수를 정의합니다.
const fetchRecommendations = async (studentId: number): Promise<Recommendation[]> => {
  const response = await api.post<Recommendation[]>(`http://127.0.0.1:8000/api/v1/recommendations/${studentId}`)
  return response.data
}
export default function AI() {
  // Zustand 스토어에서 로그인한 학생 ID를 가져옵니다.
  const user = useUserStore((state) => state.user)
  console.log("user", user)
  const studentId = user?.studentId
  // React Query의 useMutation 훅을 사용하여 API 요청을 관리합니다.
  const mutation = useMutation({
    mutationFn: fetchRecommendations,
    onSuccess: (data) => {
      console.log("추천 데이터 수신 성공:", data)
    },
    onError: (error) => {
      console.error("추천 데이터 수신 실패:", error)
    },
  })

  // 추천 받기 버튼 클릭 핸들러
  const handleRecommendClick = () => {
    console.log(studentId)
    if (studentId) {
      mutation.mutate(Number(studentId))
    } else {
      alert("로그인이 필요합니다.")
    }
  }

  return (
    <div className="min-h-screen bg-gray-100 p-4 sm:p-6 lg:p-8">
      <div className="mx-auto max-w-4xl">
        <h1 className="mb-6 text-3xl font-bold text-gray-800">팀원 추천 대시보드</h1>

        <div className="mb-8 text-center">
          <button
            onClick={handleRecommendClick}
            disabled={mutation.isPending}
            className="rounded-lg bg-blue-600 px-6 py-3 font-semibold text-white shadow-md transition-colors hover:bg-blue-700 disabled:cursor-not-allowed disabled:bg-gray-400"
          >
            {mutation.isPending ? "AI 분석 중..." : "나와 잘 맞는 팀원 추천받기"}
          </button>
        </div>

        {/* 1. 로딩 중일 때 UI */}
        {mutation.isPending && (
          <div className="text-center text-gray-600">
            <p>AI가 최적의 팀원을 분석하고 있습니다. 잠시만 기다려주세요...</p>
          </div>
        )}

        {/* 2. 에러 발생 시 UI */}
        {mutation.isError && (
          <div className="rounded-md border-l-4 border-red-500 bg-red-100 p-4 text-red-700" role="alert">
            <p className="font-bold">오류 발생</p>
            <p>추천 목록을 불러오는 데 실패했습니다. 잠시 후 다시 시도해주세요.</p>
          </div>
        )}

        {/* 3. 성공 시 UI */}
        {mutation.isSuccess && (
          <div className="space-y-4">
            {mutation.data.map((rec) => (
              <div key={rec.student_id} className="rounded-xl border border-gray-200 bg-white p-6 shadow-lg">
                <div className="mb-4 flex items-start justify-between">
                  <div className="flex items-center space-x-3">
                    <Users className="h-6 w-6 text-blue-500" />
                    <h2 className="text-xl font-semibold text-gray-900">추천 팀원 (ID: {rec.student_id})</h2>
                  </div>
                  <div className="flex items-center space-x-2 rounded-full bg-blue-100 px-3 py-1 text-sm font-bold text-blue-800">
                    <Award className="h-4 w-4" />
                    <span>궁합 점수: {rec.score}점</span>
                  </div>
                </div>
                <div className="flex items-start space-x-3 text-gray-700">
                  <Lightbulb className="mt-1 h-5 w-5 flex-shrink-0 text-yellow-500" />
                  <p className="leading-relaxed">{rec.reason}</p>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}
