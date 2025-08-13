import { forwardRef, useRef, useState } from "react"
import { useNavigate } from "react-router-dom"
import { Inbox, Search } from "lucide-react"

import { NotificationModal } from "@/components/templates"
import { useNotificationStore } from "@/stores/notificationStore"
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
  isBlinking?: boolean
  unread?: number
}

const AlarmBox = forwardRef<HTMLButtonElement, AlarmBoxProps>(({ onClick, isBlinking = false, unread = 0 }, ref) => (
  <button
    ref={ref}
    type="button"
    onClick={onClick}
    aria-label="알림함 열기"
    className={[
      "focus:ring-main/50 relative flex aspect-square h-[120%] cursor-pointer items-center justify-center rounded-full shadow-2xl transition-opacity focus:ring-2 focus:outline-none",
      "bg-main hover:opacity-90",
      // [added] 반짝임 (필요 시 pulse → ring 강조)
      isBlinking ? "ring-main/40 animate-pulse ring-4" : "",
    ].join(" ")}
  >
    <Inbox className="h-5 w-5 text-white" />
  </button>
))

function Header() {
  const navigate = useNavigate()
  const user = useUserStore((state) => state.user)
  const [isNotificationOpen, setIsNotificationOpen] = useState(false)
  const triggerRef = useRef<HTMLButtonElement | null>(null)
  const { unread, isBlinking, reset } = useNotificationStore()

  const handleAlarmClick = () => {
    setIsNotificationOpen((prev) => !prev)
    if (!isNotificationOpen) reset()
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
            <AlarmBox ref={triggerRef} onClick={handleAlarmClick} isBlinking={isBlinking} unread={unread} />
          </div>
        </div>
      </header>
      <NotificationModal
        isOpen={isNotificationOpen}
        onClose={() => setIsNotificationOpen(false)}
        returnFocusRef={triggerRef}
      />
    </>
  )
}

export default Header
