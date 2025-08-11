import { StudentInfo } from "@/components/molecules"
import Loading from "@/components/templates/Loading"
import { useSummaryInfo, useTeamRatio } from "@/hooks/useDashboard"

import DashboardCard from "./organisms/DashboardCard"
import type { PositionApi } from "./organisms/PositionFunnel"
import TrackPositionFunnel from "./organisms/PositionFunnel"
import TeamBuildingProgress from "./organisms/TeamBuildingProgress"
import TeamCardCarousel from "./organisms/TeamCardCarousel"
import { teamCardListDummy } from "./teamdummy"

type Section = {
  type: "전체" | "전공" | "비전공"
  totalStudentCount: number
  teamMemberCount: number
}
type TeamBuildingApi = {
  all: Section
  major: Section
  nonMajor: Section
}

export default function DashboardPage() {
  const { data: summary, isLoading: isSummaryLoading, error: summaryError } = useSummaryInfo()
  const { data: teamRatio, isLoading: isTeamRatioLoading, error: teamRatioError } = useTeamRatio()

  const dummy: TeamBuildingApi = {
    all: { type: "전체", totalStudentCount: 100, teamMemberCount: 30 },
    major: { type: "전공", totalStudentCount: 25, teamMemberCount: 12 },
    nonMajor: { type: "비전공", totalStudentCount: 25, teamMemberCount: 18 },
  }

  const trackdummy: PositionApi = {
    status: "SUCCESS",
    data: [
      {
        position: "프론트",
        totalCount: 16,
        majorType: [
          { name: "비전공", count: 8 },
          { name: "전공", count: 8 },
        ],
      },
      {
        position: "백엔드",
        totalCount: 14,
        majorType: [
          { name: "비전공", count: 6 },
          { name: "전공", count: 8 },
        ],
      },
      {
        position: "인프라",
        totalCount: 9,
        majorType: [
          { name: "비전공", count: 3 },
          { name: "전공", count: 6 },
        ],
      },
      {
        position: "모바일",
        totalCount: 7,
        majorType: [
          { name: "비전공", count: 4 },
          { name: "전공", count: 3 },
        ],
      },
      {
        position: "AI",
        totalCount: 6,
        majorType: [
          { name: "비전공", count: 2 },
          { name: "전공", count: 4 },
        ],
      },
      {
        position: "임베디드",
        totalCount: 5,
        majorType: [
          { name: "비전공", count: 2 },
          { name: "전공", count: 3 },
        ],
      },
    ],
  }

  if (isSummaryLoading || isTeamRatioLoading) return <Loading fullScreen />
  if (summaryError) return <div>프로필 요약 정보를 불러올 수 없습니다.</div>
  if (teamRatioError) return <div>팀 빌딩 현황 정보를 불러올 수 없습니다.</div>

  return (
    <div className="bg-background flex min-h-screen flex-col gap-5 px-15 py-10">
      {/* 대시보드 */}
      <div className="text-text flex flex-row gap-3">
        <div className="flex flex-col gap-3">
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
        <DashboardCard title={"희망 트랙별 포지션 비율 📊"}>
          <div className="mt-4">
            <TrackPositionFunnel api={trackdummy} />
          </div>
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
