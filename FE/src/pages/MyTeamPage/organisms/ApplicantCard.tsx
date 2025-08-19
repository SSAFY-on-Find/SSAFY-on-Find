import { useNavigate } from "react-router-dom"

import { Button, MajorTag, PositionTag, UserImg } from "@/components/atoms"
import { useTeamDetails } from "@/hooks/useTeam"
import { useTeamStore } from "@/stores/teamStore"
import type { INotificationTeam } from "@/types/notification"

type TabType = "receive" | "send"

interface ApplicantCardProps extends INotificationTeam {
  tab: TabType
  onAccept?: (args: { notificationId: string; nextTeamId?: number }) => void
  onReject?: (notificationId: string) => void
  onCancel?: (notificationId: string) => void
}

const statusConfig = {
  PENDING: { label: "대기", className: "bg-line/5 text-text border-line" },
  ACCEPTED: { label: "수락", className: "bg-main/5 text-main border-line font-bold" },
  REJECTED: { label: "거절", className: "bg-error/5 text-error border-line font-bold" },
  CANCELED: { label: "취소", className: "bg-line/5 text-subtext border-line font-bold" },
} as const

type StatusKey = keyof typeof statusConfig

function StatusBadge({ status }: { status: StatusKey }) {
  const s = statusConfig[status]
  return <span className={`rounded-full border px-2 py-0.5 text-sm ${s.className}`}>{s.label}</span>
}

export default function ApplicantCard({
  notificationId,
  profileImageUrl,
  name,
  status,
  isMajor,
  position,
  majorCount,
  nonMajorCount,
  id,
  type,
  tab,
  onAccept,
  onReject,
  onCancel,
}: ApplicantCardProps) {
  const navigate = useNavigate()
  const isPending = status === "PENDING"
  const disabledClass = !isPending ? "pointer-events-none opacity-40" : ""
  const isTeam = type === "TEAM"
  const BASEURL = import.meta.env.VITE_APP_BASE_URL
  const { selectedTeamId, openDetailModal } = useTeamStore()
  const { data: selectedTeamData } = useTeamDetails(selectedTeamId || 0)

  const handleAcceptClick = () => {
    const nextTeamId = tab === "receive" && type === "TEAM" && typeof id === "number" ? id : undefined
    const safeAccept = onAccept ?? (() => {})
    safeAccept({ notificationId, nextTeamId })
  }

  return (
    <div className={`hover:bg-main/10 flex items-center justify-between rounded-lg p-2 ${disabledClass}`}>
      <div
        className="flex cursor-pointer items-center gap-4"
        onClick={() => {
          if (!isTeam) navigate(`/studentlist/${id}`)
          else openDetailModal(id)
        }}
      >
        <UserImg
          name={name}
          size={"m"}
          showTeamBadge={false}
          url={isTeam ? `${BASEURL}/uploads/team/ssafy.png` : profileImageUrl ? profileImageUrl : ""}
        />

        <div className="flex flex-row gap-1">
          <div className="mr-3">{name}</div>
          <div className="flex flex-row gap-2">
            {isTeam ? (
              <>
                <MajorTag tagContent={`전공 ${majorCount}`} />
                <MajorTag tagContent={`비전공 ${nonMajorCount}`} />
              </>
            ) : (
              <>
                <MajorTag tagContent={isMajor ? "전공" : "비전공"} />
                <PositionTag positionName={position} />
              </>
            )}
          </div>
        </div>
      </div>

      <div className="flex items-center gap-2">
        {tab === "receive" ? (
          isPending ? (
            <>
              <div className="w-15">
                <Button text="거절" variant="text" size="m" isIcon={false} onClick={() => onReject?.(notificationId)} />
              </div>
              <div className="w-15">
                <Button text="수락" variant="primary" size="m" isIcon={false} onClick={handleAcceptClick} />
              </div>
            </>
          ) : (
            <StatusBadge status={status as StatusKey} />
          )
        ) : /* tab === "send" */
        isPending ? (
          <Button text="취소" variant="text" size="m" isIcon={false} onClick={() => onCancel?.(notificationId)} />
        ) : (
          <StatusBadge status={status as StatusKey} />
        )}
      </div>
    </div>
  )
}
