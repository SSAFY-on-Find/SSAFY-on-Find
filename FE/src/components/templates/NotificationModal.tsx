import { useEffect, useRef, useState } from "react"

import { Segmented } from "@/components/atoms"
import type { INotification } from "@/types/common"

import { NotificationItem } from "../molecules"

interface NotificationModalProps {
  isOpen: boolean
  onClose: () => void
  returnFocusRef?: React.RefObject<HTMLElement | null>
}

// 임시 데이터 - 실제로는 API에서 가져와야 함
const MOCK_NOTIFICATIONS: INotification[] = [
  {
    id: "1",
    type: "INVITATION",
    status: "PENDING",
    title: "팀 A의 초대",
    content: "팀 A가 정수빈(FE, 비전공)님을 초대했습니다.",
    timestamp: "7월 20일 오후 9:20",
    teamName: "팀 A",
    studentName: "정수빈",
    studentMajor: "FE",
    studentIsMajor: false,
  },
  {
    id: "2",
    type: "MERGE_PROPOSAL",
    status: "REJECTED",
    title: "팀 E의 합치기 제안",
    content: "팀 E(웹 디자인)가 팀 합치기를 제안했습니다.",
    timestamp: "2024. 7. 20. 오후 9:15:00",
    teamName: "팀 E",
    teamMajor: "웹 디자인",
  },
  {
    id: "3",
    type: "MERGE_PROPOSAL",
    status: "ACCEPTED",
    title: "팀 C의 합치기 제안",
    content: "팀 C(웹 디자인)가 팀 합치기를 제안했습니다.",
    timestamp: "2024. 7. 20. 오후 9:05:00",
    teamName: "팀 C",
    teamMajor: "웹 디자인",
  },
  {
    id: "4",
    type: "MERGE_PROPOSAL",
    status: "ACCEPTED",
    title: "팀 C의 합치기 제안",
    content: "팀 C(웹 디자인)가 팀 합치기를 제안했습니다.",
    timestamp: "2024. 7. 20. 오후 9:06:00",
    teamName: "팀 C",
    teamMajor: "웹 디자인",
  },
  {
    id: "5",
    type: "MERGE_PROPOSAL",
    status: "ACCEPTED",
    title: "팀 C의 합치기 제안",
    content: "팀 C(웹 디자인)가 팀 합치기를 제안했습니다.",
    timestamp: "2024. 7. 20. 오후 9:05:00",
    teamName: "팀 C",
    teamMajor: "웹 디자인",
  },
  {
    id: "6",
    type: "MERGE_PROPOSAL",
    status: "ACCEPTED",
    title: "팀 C의 합치기 제안",
    content: "팀 C(웹 디자인)가 팀 합치기를 제안했습니다.",
    timestamp: "2024. 7. 20. 오후 9:05:00",
    teamName: "팀 C",
    teamMajor: "웹 디자인",
  },
]

const SENT_NOTIFICATIONS: INotification[] = [
  {
    id: "4",
    type: "MERGE_PROPOSAL",
    status: "REJECTED",
    title: "팀 E 합치기 제안",
    content: "팀 E(웹 디자인)에게 팀 합치기를 제안했습니다.",
    timestamp: "2024. 7. 20. 오후 9:15:00",
    teamName: "팀 E",
    teamMajor: "웹 디자인",
  },
  {
    id: "5",
    type: "INVITATION",
    status: "PENDING",
    title: "정수빈님 초대",
    content: "정수빈(FE, 비전공)님을 팀 A에 초대했습니다.",
    timestamp: "2024. 7. 20. 오후 9:20:00",
    studentName: "정수빈",
    studentMajor: "FE",
    studentIsMajor: false,
  },
  {
    id: "6",
    type: "MERGE_PROPOSAL",
    status: "ACCEPTED",
    title: "팀 E 합치기 제안",
    content: "팀 E(웹 디자인)에게 팀 합치기를 제안했습니다.",
    timestamp: "2024. 7. 20. 오후 9:05:00",
    teamName: "팀 E",
    teamMajor: "웹 디자인",
  },
]

export default function NotificationModal({ isOpen, onClose, returnFocusRef }: NotificationModalProps) {
  const [activeTab, setActiveTab] = useState<"left" | "right">("left")
  const [notifications, setNotifications] = useState<INotification[]>(MOCK_NOTIFICATIONS)
  const [sentNotifications, setSentNotifications] = useState<INotification[]>(SENT_NOTIFICATIONS)
  const containerRef = useRef<HTMLDivElement | null>(null)

  const currentNotifications = activeTab === "left" ? notifications : sentNotifications

  const handleAcceptInvitation = (notificationId: string) => {
    if (activeTab === "left") {
      setNotifications((prev) =>
        prev.map((notification) =>
          notification.id === notificationId ? { ...notification, status: "ACCEPTED" as const } : notification
        )
      )
    }
  }

  const handleRejectInvitation = (notificationId: string) => {
    if (activeTab === "left") {
      setNotifications((prev) =>
        prev.map((notification) =>
          notification.id === notificationId ? { ...notification, status: "REJECTED" as const } : notification
        )
      )
    }
  }

  const handleCancelInvitation = (notificationId: string) => {
    if (activeTab === "right") {
      setSentNotifications((prev) =>
        prev.map((notification) =>
          notification.id === notificationId ? { ...notification, status: "CANCELLED" as const } : notification
        )
      )
    }
  }

  useEffect(() => {
    if (!isOpen) return

    const container = containerRef.current
    container?.focus()

    const handleKeydown = (e: KeyboardEvent) => {
      if (e.key === "Escape") {
        e.preventDefault()
        onClose()
      }
    }

    document.addEventListener("keydown", handleKeydown)
    return () => {
      document.removeEventListener("keydown", handleKeydown)
      returnFocusRef?.current?.focus?.()
    }
  }, [isOpen, onClose, returnFocusRef])

  return (
    <>
      {/* Sidebar */}
      <div
        role="dialog"
        aria-modal="true"
        aria-label="알림함"
        ref={containerRef}
        tabIndex={-1} // 포커스 받을 수 있게
        className={`fixed top-[64px] right-0 z-50 h-[calc(100%-64px)] w-96 transform bg-white p-2 shadow-2xl transition-transform duration-300 ease-in-out ${
          isOpen ? "translate-x-0" : "translate-x-full"
        }`}
      >
        {/* Tabs */}
        <div className="bg-white p-3">
          <Segmented activeSegment={activeTab} onSegmentChange={setActiveTab} />
        </div>

        {/* Notifications List */}
        <div className="h-[calc(100vh-120px)] overflow-y-auto">
          {currentNotifications.length === 0 ? (
            <div className="text-subtext p-8 text-center">알림이 없습니다.</div>
          ) : (
            <div className="space-y-4 px-4 py-2">
              {currentNotifications.map((n) => (
                <NotificationItem
                  key={n.id}
                  data={n}
                  tab={activeTab}
                  onAccept={handleAcceptInvitation}
                  onReject={handleRejectInvitation}
                  onCancel={handleCancelInvitation}
                />
              ))}
            </div>
          )}
        </div>
      </div>
    </>
  )
}
