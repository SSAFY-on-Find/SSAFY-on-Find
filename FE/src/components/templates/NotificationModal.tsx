import { useState } from "react"
import { Check,Mail, User, Users, X } from "lucide-react"

import { Button } from "@/components/atoms"
import type { INotification, INotificationTab } from "@/types/common"

interface NotificationModalProps {
  isOpen: boolean
  onClose: () => void
}

const TABS: INotificationTab[] = [
  { id: "received", label: "받은 요청" },
  { id: "sent", label: "보낸 요청" },
]

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

function getStatusColor(status: string) {
  switch (status) {
    case "PENDING":
      return "bg-gray-100 text-gray-700 border-gray-200"
    case "ACCEPTED":
      return "bg-purple-100 text-purple-700 border-purple-200"
    case "REJECTED":
      return "bg-red-100 text-red-700 border-red-200"
    case "CANCELLED":
      return "bg-gray-200 text-gray-600 border-gray-300"
    default:
      return "bg-gray-100 text-gray-700 border-gray-200"
  }
}

function getStatusText(status: string) {
  switch (status) {
    case "PENDING":
      return "대기"
    case "ACCEPTED":
      return "수락됨"
    case "REJECTED":
      return "거절"
    case "CANCELLED":
      return "취소됨"
    default:
      return "대기"
  }
}

function getNotificationIcon(type: string) {
  switch (type) {
    case "INVITATION":
      return <User className="h-5 w-5 text-gray-600" />
    case "MERGE_PROPOSAL":
      return <Users className="h-5 w-5 text-gray-600" />
    default:
      return <Mail className="h-5 w-5 text-gray-600" />
  }
}

export default function NotificationModal({ isOpen, onClose }: NotificationModalProps) {
  const [activeTab, setActiveTab] = useState<"received" | "sent">("received")
  const [notifications, setNotifications] = useState<INotification[]>(MOCK_NOTIFICATIONS)
  const [sentNotifications, setSentNotifications] = useState<INotification[]>(SENT_NOTIFICATIONS)

  const currentNotifications = activeTab === "received" ? notifications : sentNotifications

  const handleAcceptInvitation = (notificationId: string) => {
    if (activeTab === "received") {
      setNotifications((prev) =>
        prev.map((notification) =>
          notification.id === notificationId ? { ...notification, status: "ACCEPTED" as const } : notification
        )
      )
    }
  }

  const handleRejectInvitation = (notificationId: string) => {
    if (activeTab === "received") {
      setNotifications((prev) =>
        prev.map((notification) =>
          notification.id === notificationId ? { ...notification, status: "REJECTED" as const } : notification
        )
      )
    }
  }

  const handleCancelInvitation = (notificationId: string) => {
    if (activeTab === "sent") {
      setSentNotifications((prev) =>
        prev.map((notification) =>
          notification.id === notificationId ? { ...notification, status: "CANCELLED" as const } : notification
        )
      )
    }
  }

  return (
    <>
      {/* Sidebar */}
      <div
        className={`fixed top-0 right-0 z-50 h-full w-96 transform bg-white shadow-2xl transition-transform duration-300 ease-in-out ${
          isOpen ? "translate-x-0" : "translate-x-full"
        }`}
      >
        {/* Header */}
        <div className="flex items-center justify-between border-b border-gray-200 p-4">
          <h2 className="text-lg font-semibold text-gray-900">알림</h2>
          <button onClick={onClose} className="text-gray-400 transition-colors hover:text-gray-600">
            <X className="h-5 w-5" />
          </button>
        </div>

        {/* Tabs */}
        <div className="flex border-b border-gray-200">
          {TABS.map((tab) => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`flex-1 px-4 py-3 text-sm font-medium transition-colors ${
                activeTab === tab.id ? "border-b-2 border-blue-600 text-blue-600" : "text-gray-500 hover:text-gray-700"
              }`}
            >
              {tab.label}
            </button>
          ))}
        </div>

        {/* Notifications List */}
        <div className="h-[calc(100vh-120px)] overflow-y-auto">
          {currentNotifications.length === 0 ? (
            <div className="p-8 text-center text-gray-500">알림이 없습니다.</div>
          ) : (
            <div className="space-y-4 p-4">
              {currentNotifications.map((notification) => (
                <div
                  key={notification.id}
                  className="transform rounded-lg border border-gray-200 bg-white p-4 transition-all duration-300 ease-in-out hover:scale-[1.02] hover:shadow-md"
                >
                  <div className="flex items-start justify-between">
                    <div className="flex flex-1 items-start space-x-3">
                      <div className="mt-1">{getNotificationIcon(notification.type)}</div>
                      <div className="min-w-0 flex-1">
                        <h3 className="mb-1 text-sm font-medium text-gray-900">{notification.title}</h3>
                        <p className="mb-2 text-sm text-gray-600">{notification.content}</p>
                        <p className="text-xs text-gray-400">{notification.timestamp}</p>
                      </div>
                    </div>
                    <div className="ml-3">
                      <span
                        className={`inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-medium ${getStatusColor(
                          notification.status
                        )}`}
                      >
                        {getStatusText(notification.status)}
                      </span>
                    </div>
                  </div>

                  {/* Action buttons for received invitations */}
                  {activeTab === "received" &&
                    notification.type === "INVITATION" &&
                    notification.status === "PENDING" && (
                      <div className="mt-3 flex space-x-2 border-t border-gray-100 pt-3">
                        <Button
                          text="거절"
                          variant="text"
                          size="m"
                          isIcon={true}
                          Icon={X}
                          onClick={() => handleRejectInvitation(notification.id)}
                        />
                        <Button
                          text="수락"
                          variant="primary"
                          size="m"
                          isIcon={true}
                          Icon={Check}
                          onClick={() => handleAcceptInvitation(notification.id)}
                        />
                      </div>
                    )}

                  {/* Action buttons for sent invitations */}
                  {activeTab === "sent" && notification.type === "INVITATION" && notification.status === "PENDING" && (
                    <div className="mt-3 flex space-x-2 border-t border-gray-100 pt-3">
                      <Button
                        text="취소"
                        variant="text"
                        size="m"
                        isIcon={true}
                        Icon={X}
                        onClick={() => handleCancelInvitation(notification.id)}
                      />
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </>
  )
}
