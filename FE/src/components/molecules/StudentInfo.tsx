import { useNavigate } from "react-router-dom"
import { Heart } from "lucide-react"

import { Button, CircleGrid, MainTag, MajorTag, NormalTag, PositionTag, UserImg } from "@/components/atoms"
import { useStudentFavoriteToggle } from "@/hooks/useFavorite"
import type { ITeamInfo } from "@/types/team"
interface IStudentInfo {
  name: string
  studentId: string
  imgUrl: string
  teamInfo: ITeamInfo | null
  isMyProfile?: boolean
  isFavorite?: boolean
  variant?: "dashboard" | "detail"
}

const fontMap = {
  detail: "text-text mt-2 text-2xl font-bold",
  dashboard: "text-text mt-2 text-xl font-semibold",
}

function StudentInfo({
  name,
  studentId,
  imgUrl,
  teamInfo,
  isMyProfile = false,
  isFavorite = false,
  variant = "detail",
}: IStudentInfo) {
  const { toggleFavorite, isLoading } = useStudentFavoriteToggle()
  const navigate = useNavigate()

  return (
    <div
      className={`border-line flex w-full flex-row items-center justify-center rounded-lg border bg-white ${variant === "dashboard" ? "gap-10 px-15 py-5" : "gap-20 p-10"}`}
    >
      <div className="flex flex-col items-center justify-center gap-1">
        <div className="relative">
          <UserImg name={name} size={variant === "detail" ? "xl" : "l"} showTeamBadge={false} url={imgUrl} />
          {!isMyProfile && (
            <Heart
              className={`absolute right-2 bottom-1 cursor-pointer ${isFavorite ? "text-error" : "text-subtext"}`}
              size={24}
              fill={isFavorite ? "currentColor" : "none"}
              onClick={(e) => {
                e.stopPropagation()
                if (!isLoading(Number(studentId))) toggleFavorite(Number(studentId))
              }}
            />
          )}
        </div>
        <div className={`${fontMap[variant]}`}>{name}</div>
        {variant === "detail" && <div className="text-subtext text-sm font-normal">{studentId}</div>}
        {variant === "dashboard" && (
          <div className="mt-1 flex flex-row gap-1">
            <MajorTag tagContent={"전공"} />
            <PositionTag positionName={"임베디드"} />
            <NormalTag tagContent={"웹기술"} />
          </div>
        )}
      </div>
      <div className="bg-line h-44 w-px" />
      {teamInfo ? (
        <div className={`itesm-center flex flex-col justify-center ${variant === "dashboard" ? "gap-4" : "gap-6"}`}>
          <div className="flex flex-col gap-2">
            <MainTag tagContent={teamInfo.name} fillBg={true} size={variant === "dashboard" ? "sm" : "lg"} />
            <MainTag tagContent={teamInfo.track} size={variant === "dashboard" ? "sm" : "lg"} />
          </div>
          <CircleGrid memberCount={teamInfo.majorCount + teamInfo.nonMajorCount} variant={variant} />
        </div>
      ) : (
        <div className="flex min-w-20 flex-col gap-2">
          <div className={`text-subtext text-center ${variant === "dashboard" ? "text-sm" : ""}`}>
            {variant === "dashboard" ? (
              <>
                아직 팀이 <br /> 없습니다.
              </>
            ) : (
              "아직 팀이 없습니다."
            )}
          </div>
          {isMyProfile && (
            <Button
              text="팀 생성하기"
              size={variant === "dashboard" ? "s" : "m"}
              isIcon={false}
              onClick={() => {
                navigate("/create-team")
              }}
            />
          )}
        </div>
      )}
    </div>
  )
}

export default StudentInfo
