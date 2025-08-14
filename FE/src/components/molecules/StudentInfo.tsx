import { useNavigate } from "react-router-dom"
import { toast } from "react-toastify"
import { isAxiosError } from "axios"
import { Heart, UserRoundPlus } from "lucide-react"

import { Button, CircleGrid, MainTag, MajorTag, NormalTag, PositionTag, UserImg } from "@/components/atoms"
import { useStudentFavoriteToggle } from "@/hooks/useFavorite"
import { useInvte } from "@/hooks/useInvite"
import { useTeamStore } from "@/stores/teamStore"
import { useUserStore } from "@/stores/userStore"
import type { ITeamInfo } from "@/types/team"

function getErrorMessage(err: unknown, fallback: string) {
  if (isAxiosError(err)) {
    return err.response?.data?.data?.message ?? err.message ?? fallback
  }
  if (err instanceof Error) return err.message ?? fallback
  return fallback
}
interface IStudentInfo {
  name: string
  studentId: string
  imgUrl: string
  teamInfo: ITeamInfo | null
  isMyProfile?: boolean
  isFavorite?: boolean
  variant?: "dashboard" | "detail"
  position?: string
  track?: string
  major?: string
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
  position,
  track,
  major,
}: IStudentInfo) {
  const navigate = useNavigate()
  const { toggleFavorite, isLoading } = useStudentFavoriteToggle()
  const targetUserTeamId = teamInfo?.teamId
  const { openDetailModal } = useTeamStore()
  const { invitation } = useInvte()
  const userTeamId = useUserStore((state) => state.user?.teamId)

  return (
    <div
      className={`border-line flex w-full flex-row items-center justify-center rounded-lg border bg-white ${variant === "dashboard" ? "gap-10 px-10 py-7" : "gap-20 p-10"}`}
    >
      <div
        className={`${variant === "dashboard" ? "hover:bg-main/10 duration-300 ease-in-out hover:cursor-pointer" : ""} flex flex-col items-center justify-center gap-1 rounded-xl p-5`}
        onClick={() => {
          if (variant === "dashboard") {
            navigate("/myprofile")
          }
        }}
      >
        <div className="relative">
          <UserImg name={name} size={variant === "detail" ? "xl" : "l"} showTeamBadge={false} url={imgUrl} />
          {!isMyProfile && (
            <Heart
              className={`absolute right-2 bottom-1 cursor-pointer transition-colors ${isFavorite ? "text-error fill-current" : "text-subtext hover:text-error/80 hover:fill-current/50"}`}
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
            {major && position && track && (
              <>
                <MajorTag tagContent={major} />
                <PositionTag positionName={position} />
                <NormalTag tagContent={track} />
              </>
            )}
          </div>
        )}
      </div>
      <div className="bg-line h-44 w-px" />
      {targetUserTeamId ? (
        <div
          onClick={() => {
            if (variant === "dashboard") {
              navigate("/myteam")
            } else {
              openDetailModal(targetUserTeamId)
            }
          }}
          className={`itesm-center hover:bg-main/10 flex flex-col justify-center rounded-xl p-5 duration-300 ease-in-out hover:cursor-pointer ${variant === "dashboard" ? "gap-4" : "gap-6"}`}
        >
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
              userTeamId && (
                <>
                  <div>아직 팀이 없습니다.</div>
                  <div className="m-3 w-30">
                    <Button
                      size="m"
                      isIcon
                      Icon={UserRoundPlus}
                      variant="primary"
                      text="초대하기"
                      onClick={async () => {
                        await toast.promise(invitation(Number(studentId), Number(userTeamId)), {
                          success: "초대를 보냈습니다!",
                          error: { render: ({ data }) => getErrorMessage(data, "초대 전송에 실패했습니다.") },
                        })
                      }}
                    />
                  </div>
                </>
              )
            )}
          </div>
          {isMyProfile && (
            <>
              {variant === "detail" && <div className="text-subtext mb-3 text-center">아직 팀이 없습니다.</div>}
              <Button
                text="팀 생성하기"
                size={variant === "dashboard" ? "s" : "m"}
                isIcon={false}
                onClick={() => {
                  navigate("/create-team")
                }}
              />
            </>
          )}{" "}
        </div>
      )}
    </div>
  )
}

export default StudentInfo
