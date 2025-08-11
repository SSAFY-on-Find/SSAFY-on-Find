import { StudentInfo } from "@/components/molecules"

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

  return (
    <div className="bg-background flex min-h-screen flex-col gap-5 px-15 py-10">
      {/* 대시보드 */}
      <div className="text-text flex flex-row gap-3">
        <div className="flex flex-col gap-3">
          <StudentInfo
            name={"김싸피"}
            studentId={"1300001"}
            imgUrl={""}
            teamInfo={{ teamId: 1, name: "팀 001", track: "웹기술", majorCount: 3, nonMajorCount: 1 }}
            variant="dashboard"
            isMyProfile={true}
          />
          <DashboardCard title={"팀 빌딩 진행률 🏃‍♀️"}>
            <TeamBuildingProgress data={dummy} />
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
          {/* <div className="flex flex-row gap-3 mt-5">
            {teamCardListDummy.slice(0, 2).map((item) => (
              <TeamCard key={item.teamId} {...item} />
            ))}
          </div> */}
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
