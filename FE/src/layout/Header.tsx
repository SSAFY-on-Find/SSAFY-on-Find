import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { Mailbox, Search } from "lucide-react"

import { NotificationModal } from "@/components/templates"
import { useUserStore } from "@/stores/userStore"

function DeadlineNotification() {
  return (
    <div className="border-main flex items-center justify-center gap-3 rounded-md border px-3.5 py-1.5">
      <div className="text-main text-sm font-semibold">팀빌딩 마감시각</div>
      <div className="text-text text-base font-semibold">2025.07.21 10:00</div>
    </div>
  )
}

interface AlarmBoxProps {
  onClick: () => void
}

function AlarmBox({ onClick }: AlarmBoxProps) {
  return (
    <div
      className="bg-main flex aspect-square h-[120%] cursor-pointer items-center justify-center rounded-full shadow-2xl transition-opacity hover:opacity-90"
      style={{
        boxShadow: "0 5px 15px -3px rgba(0, 0, 0, 0.10), 0 4px 6px -4px rgba(0, 0, 0, 0.10)",
      }}
      onClick={onClick}
    >
      <Mailbox className="h-5 w-5 text-white" />
    </div>
  )
}

function Header() {
  const navigate = useNavigate()
  const user = useUserStore((state) => state.user)
  const [isNotificationOpen, setIsNotificationOpen] = useState(false)

  const handleAlarmClick = () => {
    setIsNotificationOpen(true)
  }

  const handleCloseNotification = () => {
    setIsNotificationOpen(false)
  }

  return (
    <>
      <header className="fixed top-0 left-0 z-50 flex h-[64px] w-full items-center justify-center gap-3 bg-white px-5 py-4 shadow-xs">
        <div
          className="text-main flex h-full w-[260px] cursor-pointer items-center justify-start gap-2"
          onClick={() => navigate("/")}
        >
          <Search />
          <h1 className="text-l font-bold">SSAFY On Find</h1>
        </div>
        <div className="flex h-full flex-1 items-center justify-between">
          <h1 className="text-text pl-8 text-xl font-bold">
            서울 {user?.className}{" "}
            <span className="text-subtext pl-2 text-base font-medium">
              {user?.name} ({user?.studentId})
            </span>
          </h1>
          <div className="flex h-full items-center justify-center gap-3">
            <DeadlineNotification />
            <AlarmBox onClick={handleAlarmClick} />
          </div>
        </div>
      </header>
      <NotificationModal isOpen={isNotificationOpen} onClose={handleCloseNotification} />
    </>
  )
}

export default Header
