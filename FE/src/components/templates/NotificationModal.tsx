import { useEffect, useMemo, useRef, useState } from "react"

import { Segmented } from "@/components/atoms"
import { NotificationItem } from "@/components/molecules"
import { Loading } from "@/components/templates"
import { useInviteAccept, useInviteCancel, useInviteReject } from "@/hooks/useInvite"
import { useNotification } from "@/hooks/useNotification"
import type { INotificationStatus } from "@/types/notification"

interface NotificationModalProps {
  isOpen: boolean
  onClose: () => void
  returnFocusRef?: React.RefObject<HTMLElement | null>
}

export default function NotificationModal({ isOpen, onClose, returnFocusRef }: NotificationModalProps) {
  const [activeTab, setActiveTab] = useState<"left" | "right">("left")
  const containerRef = useRef<HTMLDivElement | null>(null)

  const type = activeTab === "left" ? "receive" : "send"
  const { data, isLoading, isError } = useNotification(type) // -> ApiNotification[] (묶음)

  // 묶음 → status 리스트 평탄화 (UI 변환 없음)
  const statusList: INotificationStatus[] = useMemo(
    () => (data ?? []).flatMap((n) => n.notificationStatusList ?? []),
    [data]
  )

  const { cancelInvitation, isPending: isCanceling } = useInviteCancel(type)
  const { acceptInvitation, isPending: isAccepting } = useInviteAccept(type)
  const { rejectInvitation, isPending: isRejecting } = useInviteReject(type)

  const handleAcceptInvitation = (notificationId: string) => {
    if (!notificationId) return
    acceptInvitation(notificationId)
  }
  const handleRejectInvitation = (notificationId: string) => {
    if (!notificationId) return
    rejectInvitation(notificationId)
  }
  const handleCancelInvitation = (notificationId: string) => {
    if (!notificationId) return
    cancelInvitation(notificationId)
  }

  useEffect(() => {
    if (!isOpen) return
    containerRef.current?.focus()
    const onKey = (e: KeyboardEvent) => {
      if (e.key === "Escape") {
        e.preventDefault()
        onClose()
      }
    }
    document.addEventListener("keydown", onKey)
    return () => {
      document.removeEventListener("keydown", onKey)
      returnFocusRef?.current?.focus?.()
      document.body.style.overflow = ""
    }
  }, [isOpen, onClose, returnFocusRef])

  const lockScroll = () => {
    document.body.style.overflow = "hidden"
  }
  const unlockScroll = () => {
    document.body.style.overflow = ""
  }

  return (
    <div
      role="dialog"
      aria-modal="true"
      aria-label="알림함"
      ref={containerRef}
      tabIndex={-1}
      onMouseEnter={lockScroll}
      onMouseLeave={unlockScroll}
      className={`fixed top-[64px] right-0 z-50 h-[calc(100%-64px)] w-96 transform bg-white p-2 shadow-2xl transition-transform duration-300 ease-in-out ${
        isOpen ? "translate-x-0" : "translate-x-full"
      }`}
    >
      <div className="bg-white p-3">
        <Segmented activeSegment={activeTab} onSegmentChange={setActiveTab} />
      </div>

      <div className="h-[calc(100vh-120px)] overflow-y-auto">
        {isLoading ? (
          <Loading />
        ) : isError ? (
          <div className="text-error p-8 text-center">알림을 불러오지 못했습니다.</div>
        ) : statusList.length === 0 ? (
          <div className="text-subtext p-8 text-center">알림이 없습니다.</div>
        ) : (
          <div className="space-y-4 px-4 py-2">
            {statusList.map((s) => {
              const key = typeof s.statusId === "string" ? s.statusId : String(s.statusId?.timestamp ?? Math.random())
              return (
                <NotificationItem
                  key={key}
                  data={s}
                  tab={activeTab}
                  onAccept={handleAcceptInvitation}
                  onReject={handleRejectInvitation}
                  onCancel={handleCancelInvitation}
                />
              )
            })}
          </div>
        )}
      </div>
    </div>
  )
}
