import { useNavigate } from "react-router-dom"
import { toast } from "react-toastify"
import { Mail, User, Users } from "lucide-react"

import { Button } from "@/components/atoms"
import { useUserStore } from "@/stores/userStore"
import type { INotificationStatus } from "@/types/notification"

function formatDate(dateString?: string) {
  if (!dateString) return ""

  const date = new Date(dateString)
  if (isNaN(date.getTime())) return dateString

  return date.toLocaleString("ko-KR", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  })
}

function getStatusColor(status: string) {
  switch (status) {
    case "PENDING":
      return "bg-line/5 text-text border-line"
    case "ACCEPTED":
      return "bg-main/5 text-main border-line"
    case "REJECTED":
      return "bg-error/5 text-error border-line"
    case "CANCELED":
      return "bg-line/5 text-subtext border-line"
    default:
      return "bg-line/5 text-subtext border-line"
  }
}

function getStatusText(status: string) {
  switch (status) {
    case "PENDING":
      return "대기"
    case "ACCEPTED":
      return "수락"
    case "REJECTED":
      return "거절"
    case "CANCELED":
      return "취소"
    default:
      return "대기"
  }
}

function getNotificationIcon(type: string) {
  switch (type) {
    case "STUDENT":
      return <User className="text-text h-5 w-5" />
    case "TEAM":
      return <Users className="text-text h-5 w-5" />
    default:
      return <Mail className="text-text h-5 w-5" />
  }
}

function getStatusBorderColor(status: string) {
  switch (status) {
    case "PENDING":
      return "border-main/40 border-3"
    case "ACCEPTED":
      return "border-main"
    case "REJECTED":
      return "border-error"
    case "CANCELED":
      return "border-subtext"
    default:
      return "border-line"
  }
}

export default function NotificationItem({
  data,
  tab,
  onAccept,
  onReject,
  onCancel,
}: {
  data: INotificationStatus
  tab: "left" | "right"
  onAccept: (statusId: string) => void
  onReject: (statusId: string) => void
  onCancel: (statusId: string) => void
}) {
  const navigate = useNavigate()
  const isInvitation =
    (data.role === "SUBSCRIBER" && data.subscriberType === "STUDENT") ||
    (data.role === "PUBLISHER" && data.publisherType === "STUDENT")
  const isPending = data.status === "PENDING"
  const userTeamId = useUserStore((state) => state.user?.teamId)

  const goToMyTeamIfTeam = () => {
    const goLeft = tab === "left" && data.subscriberType === "TEAM"
    const goRight = tab === "right" && data.publisherType === "TEAM"

    if (goLeft) {
      if (data.subscriberId === userTeamId) {
        navigate("/myteam")
      } else {
        toast.error("현재는 가입되지 않은 팀입니다.")
      }
      return
    }

    if (goRight) {
      if (data.publisherId === userTeamId) {
        navigate("/myteam")
      } else {
        toast.error("현재는 가입되지 않은 팀입니다.")
      }
      return
    }
  }

  return (
    <div
      className={`transform rounded-lg border bg-white p-4 transition-all duration-300 ease-in-out ${getStatusBorderColor(data.status)} ${!isPending ? "pointer-events-none opacity-50" : ""} ${isPending ? "hover:scale-[1.02] hover:shadow-md" : ""}`}
    >
      <div className="flex flex-col items-start justify-between gap-2">
        <div className="flex w-full items-center justify-between">
          <div
            className={`${!isInvitation ? "cursor-pointer" : ""} flex items-center gap-3`}
            onClick={goToMyTeamIfTeam}
          >
            <div>{getNotificationIcon(tab === "left" ? data.subscriberType : data.publisherType)}</div>
            <h3 className="text-text text-sm font-bold">
              {tab === "left" ? data.pubNotificationTitle : data.subNotificationTitle}
            </h3>
          </div>
          <div className="ml-3">
            <span
              className={`inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-medium ${getStatusColor(
                data.status
              )}`}
            >
              {getStatusText(data.status)}
            </span>
          </div>
        </div>

        <div className="mt-1 min-w-0">
          {/* <p className="text-text mb-2 text-sm">{data.subNotificationMessage}</p> */}
          <p className="text-text mb-2 text-sm">
            {tab === "left" ? data.pubNotificationMessage : data.subNotificationMessage}
          </p>
          <p className="text-subtext text-xs">{formatDate(data.updatedAt)}</p>
        </div>
      </div>

      {/* 액션 버튼 */}
      {tab === "left" && isInvitation && isPending && (
        <div className="mt-3 flex space-x-2 pt-2">
          <Button text="거절" variant="text" size="s" isIcon={false} onClick={() => onReject?.(data.notificationId)} />
          <Button
            text="수락"
            variant="primary"
            size="s"
            isIcon={false}
            onClick={() => onAccept?.(data.notificationId)}
          />
        </div>
      )}

      {tab === "right" && isInvitation && isPending && (
        <div className="mt-3 flex space-x-2 pt-2">
          <Button text="취소" variant="text" size="s" isIcon={false} onClick={() => onCancel?.(data.notificationId)} />
        </div>
      )}
    </div>
  )
}
