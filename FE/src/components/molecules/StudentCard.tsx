import { useNavigate } from "react-router-dom"
import { toast } from "react-toastify"
import { Heart } from "lucide-react"

import { MainTag, MajorTag, NormalTag, PositionTag, UserImg } from "@/components/atoms"
import { useStudentFavoriteToggle } from "@/hooks/useFavorite"
import type { IStudentCard } from "@/types/student"

interface IStudentCardWithMyId extends IStudentCard {
  userId: number
}
function StudentCard({
  student,
  position,
  track,
  goal,
  profileImageUrl,
  isFavorite,
  teamName,
  userId,
}: IStudentCardWithMyId) {
  const navigate = useNavigate()
  const { toggleFavorite, isLoading } = useStudentFavoriteToggle()
  const alreadyWroteProfile = position.subcode ? true : false

  return (
    <div
      className={`border-line flex cursor-pointer flex-col items-center justify-center gap-3 rounded-lg border bg-white px-20 py-5 shadow-sm transition-all duration-300 ease-out hover:-translate-y-1 hover:shadow-lg`}
      onClick={() => {
        if (!alreadyWroteProfile) {
          toast.warn("아직 가입하지 않은 교육생입니다.")
        } else if (userId !== student.studentId) {
          navigate(`/studentlist/${student.studentId}`)
        } else {
          navigate(`/myprofile`)
        }
      }}
    >
      <div className="relative">
        <UserImg
          name={student.name}
          size={"xl"}
          showTeamBadge={false}
          url={profileImageUrl === null ? "" : profileImageUrl}
        />
        {userId !== student.studentId ? (
          <Heart
            className={`absolute right-2 bottom-1 cursor-pointer transition-colors ${isFavorite ? "text-error fill-current" : "text-subtext hover:text-error/80 hover:fill-current/50"}`}
            size={24}
            fill={isFavorite ? "currentColor" : "none"}
            onClick={(e) => {
              e.stopPropagation()
              if (!isLoading(student.studentId)) toggleFavorite(student.studentId)
            }}
          />
        ) : (
          <></>
        )}
      </div>
      <div className="flex flex-row items-center justify-center gap-2">
        <div className="text-text text-lg font-bold">{student.name}</div>
        <div className="text-subtext text-sm">{student.studentId}</div>
      </div>
      <div className="flex flex-col gap-2">
        <div className="flex flex-row justify-center gap-2">
          {teamName && <MainTag tagContent={teamName} fillBg={true} />}
          <MajorTag tagContent={student.major} />
          {position.subcodeName && <PositionTag positionName={position.subcodeName} />}
        </div>
        <div className="flex flex-row justify-center gap-2">
          {track.subcodeName && <NormalTag tagContent={track.subcodeName} />}
          {goal.subcodeName && <NormalTag tagContent={`${goal.subcodeName} 우선`} />}
        </div>
      </div>
    </div>
  )
}

export default StudentCard
