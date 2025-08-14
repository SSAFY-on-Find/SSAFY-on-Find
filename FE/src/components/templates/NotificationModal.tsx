import { useEffect, useMemo, useRef, useState } from "react"
import { toast } from "react-toastify"
import { isAxiosError } from "axios"

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

function getErrorMessage(err: unknown, fallback: string) {
  if (isAxiosError(err)) {
    return err.response?.data?.data?.message ?? err.message ?? fallback
  }
  if (err instanceof Error) return err.message ?? fallback
  return fallback
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

  const { cancleInvitationAsync, isPending: isCanceling } = useInviteCancel(type)
  const { acceptInvitationAsync, isPending: isAccepting } = useInviteAccept(type)
  const { rejectInvitationAsync, isPending: isRejecting } = useInviteReject(type)

  const handleAcceptInvitation = async (id: string) => {
    if (!id) return
    await toast.promise(acceptInvitationAsync(id), {
      success: "초대를 수락했습니다!",
      error: { render: ({ data }) => getErrorMessage(data, "수락 중 오류가 발생했습니다.") },
    })
  }
  const handleRejectInvitation = async (id: string) => {
    if (!id) return
    await toast.promise(rejectInvitationAsync(id), {
      success: "초대를 거절했습니다!",
      error: { render: ({ data }) => getErrorMessage(data, "거절 중 오류가 발생했습니다.") },
    })
  }
  const handleCancelInvitation = async (id: string) => {
    if (!id) return
    await toast.promise(cancleInvitationAsync(id), {
      success: "초대를 취소했습니다!",
      error: { render: ({ data }) => getErrorMessage(data, "취소 중 오류가 발생했습니다.") },
    })
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
  }, [isOpen, onClose, returnFocusRef, data, statusList, isLoading, isError])

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
              const key = s.notificationId
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
