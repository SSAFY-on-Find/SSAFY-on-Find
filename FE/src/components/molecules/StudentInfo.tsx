import { Heart } from "lucide-react"

import { Button, CircleGrid, MainTag, UserImg } from "@/components/atoms"
import type { ITeamInfo } from "@/types/team"

interface IStudentInfo {
  name: string
  studentId: string
  imgUrl: string
  teamInfo: ITeamInfo | null
  isMyProfile?: boolean
  isFavorite?: boolean
}

function StudentInfo({ name, studentId, imgUrl, teamInfo, isMyProfile = false, isFavorite = false }: IStudentInfo) {
  return (
    <div className="border-line flex w-full flex-row items-center justify-center gap-20 rounded-lg border bg-white p-10">
      <div className="flex flex-col items-center justify-center gap-1">
        <div className="relative">
          <UserImg name={name} size={"xl"} showTeamBadge={false} url={imgUrl} />
          {!isMyProfile && (
            <Heart
              className={`absolute right-2 bottom-1 cursor-pointer ${isFavorite ? "text-error" : "text-subtext"}`}
              size={24}
              fill={isFavorite ? "currentColor" : "none"}
            />
          )}
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
          {isMyProfile && <Button text="팀 생성하기" size={"m"} isIcon={false} onClick={() => {}} />}
          {/* 초대하기...? 채팅하기...? */}
        </div>
      )}
    </div>
  )
}

export default StudentInfo
