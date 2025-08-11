import { StudentInfo } from "@/components/molecules"
import Loading from "@/components/templates/Loading"
import { usePositionRatio, useSummaryInfo, useTeamRatio } from "@/hooks/useDashboard"

import DashboardCard from "./organisms/DashboardCard"
import TrackPositionFunnel from "./organisms/PositionFunnel"
import TeamBuildingProgress from "./organisms/TeamBuildingProgress"
import TeamCardCarousel from "./organisms/TeamCardCarousel"
import { teamCardListDummy } from "./teamdummy"

export default function DashboardPage() {
  const { data: summary, isLoading: isSummaryLoading, error: summaryError } = useSummaryInfo()
  const { data: teamRatio, isLoading: isTeamRatioLoading, error: teamRatioError } = useTeamRatio()
  const { data: positionRatio, isLoading: isPositionRatioLoading, error: positionRatioError } = usePositionRatio()

  if (isSummaryLoading || isTeamRatioLoading || isPositionRatioLoading) return <Loading fullScreen />
  if (summaryError) return <div>프로필 요약 정보를 불러올 수 없습니다.</div>
  if (teamRatioError) return <div>팀 빌딩 현황 정보를 불러올 수 없습니다.</div>
  if (positionRatioError) return <div>포지션별 팀 빌딩 현황 정보를 불러올 수 없습니다.</div>

  return (
    <div className="bg-background flex min-h-screen flex-col gap-5 px-15 py-10">
      {/* 대시보드 */}
      <div className="text-text flex flex-row gap-3">
        <div className="flex flex-col justify-between gap-3">
          <StudentInfo
            name={summary?.student.name ?? ""}
            studentId={String(summary?.student.studentId)}
            imgUrl={summary?.profileImageUrl ?? ""}
            teamInfo={summary?.team ?? null}
            variant="dashboard"
            isMyProfile={true}
          />
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
        <DashboardCard title={"팀원을 구하고 있어요! 🚀"}>
          <div className="mt-5">
            <TeamCardCarousel items={teamCardListDummy} />
          </div>
        </DashboardCard>
        <DashboardCard title={"이 친구 어때요? 😊"}>
          <div>chart</div>
        </DashboardCard>
      </div>
    </div>
  )
}
