// components/ApplicantCard.tsx
import { Button, UserImg } from "@/components/atoms"
import type { INotificationStatus } from "@/types/notification"

type TabType = "receive" | "send"

interface ApplicantCardProps extends INotificationStatus {
  tab: TabType
  onAccept?: (notificationId: string) => void
  onReject?: (notificationId: string) => void
  onCancel?: (notificationId: string) => void
}

const statusLabelMap = {
  PENDING: "대기",
  ACCEPTED: "수락",
  REJECTED: "거절",
  CANCELED: "취소",
} as const

export default function ApplicantCard({
  notificationId,
  status,
  isRead,
  updatedAt,
  pubNotificationTitle,
  pubNotificationMessage,
  subNotificationTitle,
  subNotificationMessage,
  publisherId,
  publisherType,
  subscriberId,
  subscriberType,
  targetType,
  role,
  tab,
  onAccept,
  onReject,
  onCancel,
  ..._rest
}: ApplicantCardProps) {
  const isPending = status === "PENDING"

  return (
    <div className="hover:bg-main/10 flex items-center justify-between rounded-lg p-4">
      <div className="flex items-center gap-4">
        <UserImg
          name={String(publisherId)}
          size={"m"}
          showTeamBadge={false}
          url={publisherType === "TEAM" ? "../../../../public/ssafy.png" : ""}
        />

        <div className="flex flex-col gap-1">
          <div className="flex items-center gap-2">
            {tab === "receive" ? (
              <p className="font-senibold text-text text-sm">{publisherId}</p>
            ) : (
              <p className="font-senibold text-text text-sm">{subscriberId}</p>
            )}

            <span
              className={`border-line rounded-full border px-2 py-0.5 text-xs ${
                status === "PENDING"
                  ? "bg-line/5 text-text border-line"
                  : status === "ACCEPTED"
                    ? "bg-main/5 text-main border-lin"
                    : status === "REJECTED"
                      ? "bbg-error/5 text-error border-line"
                      : status === "CANCELED"
                        ? "bg-line/5 text-subtext border-line"
                        : "bg-gray-100 text-gray-500"
              }`}
            >
              {statusLabelMap[status]}
            </span>
          </div>
        </div>
      </div>

      <div className="flex items-center gap-2">
        {tab === "receive" ? (
          isPending ? (
            <>
              <div className="w-15">
                <Button text="거절" variant="text" size="s" isIcon={false} onClick={() => onReject?.(notificationId)} />
              </div>
              <div className="w-15">
                <Button
                  text="수락"
                  variant="primary"
                  size="s"
                  isIcon={false}
                  onClick={() => onAccept?.(notificationId)}
                />
              </div>
            </>
          ) : (
            <></>
          )
        ) : // tab === "send"
        isPending ? (
          <Button text="취소" variant="text" size="s" isIcon={false} onClick={() => onCancel?.(notificationId)} />
        ) : (
          <></>
        )}
      </div>
    </div>
  )
}
