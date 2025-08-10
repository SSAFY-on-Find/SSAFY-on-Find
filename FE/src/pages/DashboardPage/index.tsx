import { StudentInfo } from "@/components/molecules"

import DashboardCard from "./organisms/DashboardCard"

export default function DashboardPage() {
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
            <div>chart</div>
          </DashboardCard>
        </div>
        <DashboardCard title={"희망 트랙별 포지션 비율 📊"}>
          <div>chart</div>
        </DashboardCard>
      </div>

      {/* 팀/교육생 추천 */}
      <div className="flex flex-row gap-3">
        <DashboardCard title={"팀원을 구하고 있어요! 🚀"}>
          <div>chart</div>
        </DashboardCard>
        <DashboardCard title={"이 친구 어때요? 😊"}>
          <div>chart</div>
        </DashboardCard>
      </div>
    </div>
  )
}
