import { Heart } from "lucide-react"

import { Button, CircleGrid, MainTag, UserImg } from "@/components/atoms"
import type { ITeamInfo } from "@/types/team"

interface IStudentInfo {
  name: string
  studentId: string
  teamInfo: ITeamInfo | null
}

function StudentInfo({ name, studentId, teamInfo }: IStudentInfo) {
  return (
    <div className="border-line flex w-full flex-row items-center justify-center gap-20 rounded-lg border bg-white p-10">
      <div className="flex flex-col items-center justify-center gap-1">
        <div className="relative">
          <UserImg name={name} size={"xl"} showTeamBadge={false} />
          {/* 마이 페이지 아닐 때만 좋아요 토글 */}
          <Heart className="text-error absolute right-2 bottom-1 cursor-pointer" size={24} fill="currentColor" />
        </div>
        <div className="text-text mt-2 text-2xl font-bold">{name}</div>
        <div className="text-subtext text-sm font-normal">{studentId}</div>
      </div>
      <div className="bg-line h-44 w-px" />
      {teamInfo ? (
        <div className="itesm-center flex flex-col justify-center gap-6">
          <div className="flex flex-col gap-2">
            <MainTag tagContent={teamInfo.name} fillBg={true} size="lg" />
            <MainTag tagContent={teamInfo.track} size="lg" />
          </div>
          <CircleGrid memberCount={teamInfo.majorCount + teamInfo.nonMajorCount} />
        </div>
      ) : (
        <div className="flex flex-col gap-2">
          <div className="text-subtext text-center"> 아직 팀이 없습니다.</div>
          {/* 마이페이지 일때만 팀 생성 버튼*/}
          <Button text="팀 생성하기" size={"m"} isIcon={false} onClick={() => {}} />
        </div>
      )}
    </div>
  )
}

export default StudentInfo
