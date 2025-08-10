import { StudentInfo } from "@/components/molecules"

export default function DashboardPage() {
  const newLocal = "팀 001"
  return (
    <div className="bg-background flex min-h-screen flex-col gap-5 px-15 py-10">
      {/* 대시보드 */}
      <div className="text-text flex flex-row gap-3">
        <div className="flex flex-col gap-3">
          <StudentInfo
            name={"김싸피"}
            studentId={"1300001"}
            imgUrl={""}
            teamInfo={{ teamId: 1, name: newLocal, track: "웹기술", majorCount: 3, nonMajorCount: 1 }}
            variant="dashboard"
            isMyProfile={true}
          />
          <div className="border-line h-50 rounded-md border bg-white">팀 빌딩 현황</div>
        </div>
        <div className="border-main w-full border">희망트랙별 포지션 비율</div>
      </div>

      {/* 팀/교육생 추천 */}
      <div className="flex flex-row gap-3">
        <div>팀 추천</div>
        <div>교육생 추천</div>
      </div>
    </div>
  )
}
