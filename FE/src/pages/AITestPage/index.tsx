import { useMutation } from "@tanstack/react-query"
import { Award, Info, Lightbulb, Users } from "lucide-react"

import api from "@/apis"
import { NormalTag, UserImg } from "@/components/atoms"
import { useUserStore } from "@/stores/userStore"

// API 응답 데이터 타입을 정의합니다.
interface Recommendation {
  studentId: number
  name: string
  profileImageUrl: string | null
  majorYn: boolean
  goal: string
  score: number
  reason: string
}

const fetchRecommendations = async (studentId: number): Promise<Recommendation[]> => {
  const response = await api.post<Recommendation[]>(`http://localhost:9000/api/v1/recommendations/${studentId}`)
  return response.data
}
export default function AI() {
  // Zustand 스토어에서 로그인한 학생 ID를 가져옵니다.
  const user = useUserStore((state) => state.user)
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
          <div className="border-line rounded-2xl border bg-white p-6 sm:p-8">
            <div className="mb-6 flex items-center gap-2">
              <h2 className="text-text text-2xl font-bold">이 친구 어때요?</h2>
              <span className="text-2xl">🤔</span>
              <Info className="text-gray-400" size={18} />
            </div>

            <div className="space-y-2">
              {mutation.data.map((rec, index) => (
                <div
                  key={rec.studentId}
                  className="group relative flex items-center justify-between rounded-lg p-3 transition-colors hover:bg-gray-100"
                >
                  {/* Left Section: Rank, Avatar, Name, Tags */}
                  <div className="flex items-center gap-4">
                    <span className="text-text w-8 text-lg font-bold">{index + 1}위</span>
                    {/* UserImg, NormalTag 컴포넌트를 사용하기 위해 임포트가 필요합니다. */}
                    {/* 예: import UserImg from "@/components/common/UserImg"; */}
                    {rec.profileImageUrl ? (
                      <UserImg name={rec.name} url={rec.profileImageUrl} size="m" showTeamBadge={false} />
                    ) : (
                      <UserImg name={rec.name} size="m" showTeamBadge={false} />
                    )}

                    <div className="flex flex-col sm:flex-row sm:items-center sm:gap-2">
                      <span className="text-text font-semibold">{rec.name}</span>
                      <div className="mt-1 flex items-center gap-2 sm:mt-0">
                        {rec.majorYn ? <NormalTag tagContent={"전공"} /> : <NormalTag tagContent={"비전공"} />}
                        <NormalTag tagContent={rec.goal} />
                      </div>
                    </div>
                  </div>

                  {/* Right Section: Score */}
                  <span className="text-main text-2xl font-bold">{rec.score}%</span>

                  {/* Tooltip for 'reason' on hover */}
                  <div className="pointer-events-none absolute top-full left-1/2 z-10 mt-2 w-72 -translate-x-1/2 rounded-lg bg-gray-800 p-3 text-sm text-white opacity-0 shadow-lg transition-opacity group-hover:opacity-100">
                    {rec.reason}
                    {/* Tooltip arrow */}
                    <div className="absolute bottom-full left-1/2 h-0 w-0 -translate-x-1/2 border-x-8 border-b-8 border-x-transparent border-b-gray-800"></div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  )
}
