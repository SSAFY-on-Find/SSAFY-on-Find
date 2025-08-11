import { useNavigate } from "react-router-dom"
import { Heart } from "lucide-react"

import { MainTag, MajorTag, NormalTag, PositionTag, UserImg } from "@/components/atoms"
import { useStudentFavoriteToggle } from "@/hooks/useFavorite"
import type { IStudentCard } from "@/types/student"

function StudentCard({ student, position, track, goal, profileImageUrl, isFavorite, teamName }: IStudentCard) {
  const navigate = useNavigate()
  const { toggleFavorite, isLoading } = useStudentFavoriteToggle()

  return (
    <div
      className="border-line flex cursor-pointer flex-col items-center justify-center gap-3 rounded-lg border bg-white px-20 py-5"
      onClick={() => {
        navigate(`/studentlist/${student.studentId}`)
      }}
    >
      <div className="relative">
        <UserImg
          name={student.name}
          size={"xl"}
          showTeamBadge={false}
          url={profileImageUrl === null ? "" : profileImageUrl}
        />
        <Heart
          className={`absolute right-2 bottom-1 cursor-pointer ${isFavorite ? "text-error" : "text-subtext"}`}
          size={24}
          fill={isFavorite ? "currentColor" : "none"}
          onClick={(e) => {
            e.stopPropagation()
            if (!isLoading(student.studentId)) toggleFavorite(student.studentId)
          }}
        />
      </div>
      <div className="flex flex-row items-center justify-center gap-2">
        <div className="text-text text-lg font-bold">{student.name}</div>
        <div className="text-subtext text-sm">{student.studentId}</div>
      </div>
      <div className="flex flex-col gap-2">
        <div className="flex flex-row justify-center gap-2">
          {teamName && <MainTag tagContent={teamName} fillBg={true} />}
          <MajorTag tagContent={student.major} />
          <PositionTag positionName={position.subcodeName} />
        </div>
        <div className="flex flex-row justify-center gap-2">
          <NormalTag tagContent={track.subcodeName} />
          <NormalTag tagContent={`${goal.subcodeName} 우선`} />
        </div>
      </div>
    </div>
  )
}

export default StudentCard
