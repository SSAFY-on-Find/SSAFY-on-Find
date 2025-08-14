import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { useQueryClient } from "@tanstack/react-query"
import { Info, RotateCw } from "lucide-react"

import { MajorTag, NormalTag, PositionTag, Tooltip, UserImg } from "@/components/atoms"
import { StudentInfo } from "@/components/molecules"
import Loading from "@/components/templates/Loading"
import {
  useAIRecommendations,
  usePositionRatio,
  useRecommendTeam,
  useSummaryInfo,
  useTeamRatio,
} from "@/hooks/useDashboard"
import { useUserStore } from "@/stores/userStore"

import DashboardCard from "./organisms/DashboardCard"
import TrackPositionFunnel from "./organisms/PositionFunnel"
import TeamBuildingProgress from "./organisms/TeamBuildingProgress"
import TeamCardCarousel from "./organisms/TeamCardCarousel"

export default function DashboardPage() {
  const navigate = useNavigate()
  const studentId = useUserStore((s) => s.user?.studentId)
  const { data: summary, isLoading: isSummaryLoading, error: summaryError } = useSummaryInfo()
  const { data: teamRatio, isLoading: isTeamRatioLoading, error: teamRatioError } = useTeamRatio()
  const { data: positionRatio, isLoading: isPositionRatioLoading, error: positionRatioError } = usePositionRatio()
  const { data: recommendTeam, isLoading: isRecommendTeamLoading, error: recommendTeamError } = useRecommendTeam()
  const { data: aiRecommend, isLoading } = useAIRecommendations(studentId ? Number(studentId) : undefined)
  const [isRefreshing, setIsRefreshing] = useState(false)
  const queryClient = useQueryClient()
  const handleRefreshAiRecommend = () => {
    if (studentId) {
      setIsRefreshing(true)
      queryClient.removeQueries({
        queryKey: ["aiRecommendations", Number(studentId)],
      })
      queryClient
        .invalidateQueries({
          queryKey: ["aiRecommendations", Number(studentId)],
        })
        .then(() => {
          setIsRefreshing(false)
        })
    }
  }
  const showAiLoading = isLoading || isRefreshing
  if (isSummaryLoading || isTeamRatioLoading || isPositionRatioLoading || isRecommendTeamLoading)
    return <Loading fullScreen text="정보를 불러오는 중..." />
  if (summaryError) return <div>프로필 요약 정보를 불러올 수 없습니다.</div>
  if (teamRatioError) return <div>팀 빌딩 현황 정보를 불러올 수 없습니다.</div>
  if (positionRatioError) return <div>포지션별 팀 빌딩 현황 정보를 불러올 수 없습니다.</div>
  if (recommendTeamError) return <div>추천 팀 목록을 불러올 수 없습니다.</div>
  return (
    <div className="bg-background flex min-h-screen flex-col gap-5 px-15 py-10">
      {/* 대시보드 */}
      <div className="text-text flex flex-row gap-3">
        <div className="flex flex-col justify-between gap-3">
          {summary && (
            <StudentInfo
              name={summary?.student.name ?? ""}
              studentId={String(summary?.student.studentId)}
              imgUrl={summary?.profileImageUrl ?? ""}
              teamInfo={summary?.team ?? null}
              variant="dashboard"
              isMyProfile={true}
              major={summary?.student.major}
              position={summary?.position.subcodeName}
              track={summary?.track.subcodeName}
            />
          )}
          <DashboardCard title={"팀 빌딩 진행률 🏃‍♀️"}>
            {teamRatio ? <TeamBuildingProgress data={teamRatio} /> : null}
          </DashboardCard>
        </div>
        <DashboardCard title={"포지션별 팀 빌딩 현황 📊"}>
          <div className="mt-4">{positionRatio ? <TrackPositionFunnel api={positionRatio} /> : null}</div>
        </DashboardCard>
      </div>

      {/* 팀/교육생 추천 */}
      <div className="flex flex-row gap-3">
        <DashboardCard title={`${summary?.student.name} 님을 위한 팀 추천 🚀`}>
          {recommendTeam ? (
            recommendTeam.message ? (
              <div className="text-subtext text-center whitespace-pre-line">{recommendTeam.message}</div>
            ) : (
              <div className="mt-5">
                <TeamCardCarousel items={recommendTeam.items} />
              </div>
            )
          ) : null}
        </DashboardCard>
        <DashboardCard title={""}>
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <h3 className="text-text text-lg font-bold">AI 추천 개인 궁합도 😊</h3>
              <div>
                <Tooltip
                  content={
                    <>
                      <span className="block w-50"></span>자기소개의 여러 요소를 종합하여 평가한 지표입니다.
                    </>
                  }
                  side="right"
                >
                  <Info className="text-subtext w-4" />
                </Tooltip>
              </div>
            </div>
            <button
              onClick={handleRefreshAiRecommend}
              disabled={showAiLoading}
              className="hover:bg-main/20 rounded-full p-1 transition-colors disabled:cursor-not-allowed disabled:opacity-50"
              title="새로고침"
            >
              <RotateCw size={16} className={showAiLoading ? "animate-spin" : ""} />
            </button>
          </div>
          {showAiLoading && (
            <div className="text-subtext mt-15 text-center">
              <Loading text="AI가 최적의 팀원을 분석 중입니다..." bg="bg-white" />
            </div>
          )}
          {!showAiLoading && aiRecommend && (
            <div className="mt-3 flex flex-col gap-2">
              {aiRecommend?.map((rec, index) => (
                <div
                  key={rec.studentId}
                  className="hover:cursor-pointer"
                  onClick={() => navigate(`/studentlist/${rec.studentId}`)}
                >
                  <Tooltip content={rec.reason} side="top" className="block w-full">
                    <div className="hover:bg-main/10 flex items-center justify-between rounded-lg p-2 transition-colors">
                      {/* Left Section */}
                      <div className="flex items-center gap-3">
                        <span className="text-text w-8 text-lg font-bold">{index + 1}위</span>
                        <UserImg name={rec.name} url={rec.profileImageUrl ?? ""} size="m" showTeamBadge={false} />
                        <div className="flex flex-col sm:flex-row sm:items-center sm:gap-2">
                          <span className="text-text font-semibold text-nowrap">{rec.name}</span>
                          <div className="mt-1 flex items-center gap-2 sm:mt-0">
                            {rec.majorYn ? <MajorTag tagContent="전공" /> : <MajorTag tagContent="비전공" />}
                            {rec.position && <PositionTag positionName={rec.position} />}
                            {/* <NormalTag tagContent={`${rec.goal} 우선`} /> */}
                          </div>
                        </div>
                      </div>
                      <span className="text-main text-lg font-bold">{rec.score}%</span>
                    </div>
                  </Tooltip>
                </div>
              ))}
            </div>
          )}
        </DashboardCard>
      </div>
    </div>
  )
}
