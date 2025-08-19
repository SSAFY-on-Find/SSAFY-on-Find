import { forwardRef, useEffect, useRef, useState } from "react"
import { useNavigate } from "react-router-dom"
import { Inbox, Search } from "lucide-react"

import { NotificationModal } from "@/components/templates"
import { useNotificationStore } from "@/stores/notificationStore"
import { useUserStore } from "@/stores/userStore"

function DeadlineNotification() {
  const [timeLeft, setTimeLeft] = useState("")
  const [dayLeft, setDayLeft] = useState("")
  useEffect(() => {
    const deadline = new Date("2025-08-22T10:00:00")

    const updateTimer = () => {
      const now = new Date()
      const diff = deadline.getTime() - now.getTime()

      if (diff > 0) {
        const days = Math.floor(diff / (1000 * 60 * 60 * 24))
        const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
        const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))
        const seconds = Math.floor((diff % (1000 * 60)) / 1000)
        setDayLeft(`${days}일 `)
        setTimeLeft(`${hours}시간 ${minutes}분 ${seconds}초`)
      } else {
        setTimeLeft("마감됨")
      }
    }

    updateTimer()
    const interval = setInterval(updateTimer, 1000)

    return () => clearInterval(interval)
  }, [])
  return (
    <div className="border-main flex items-center justify-center gap-3 rounded-md border px-3.5 py-1.5">
      <div className="text-main text-sm font-semibold">팀빌딩 마감까지</div>
      <div className="flex min-w-40 items-center justify-center gap-2">
        <div className="text-text text-base font-semibold">{dayLeft}</div>
        <div className="text-subtext text-base font-normal">{timeLeft}</div>
      </div>
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
      "bg-main hover:opacity-80",
      // [added] 반짝임 (필요 시 pulse → ring 강조)
      isBlinking ? "ring-main/40 animate-bounce ring-4" : "",
    ].join(" ")}
  >
    <Inbox className="h-5 w-5 text-white" />
  </button>
))

function Header() {
  const navigate = useNavigate()
  const user = useUserStore((state) => state.user)
  const triggerRef = useRef<HTMLButtonElement | null>(null)
  const panelRef = useRef<HTMLDivElement | null>(null)
  const { unread, isBlinking, isOpen, setOpen, reset } = useNotificationStore()

  const handleAlarmClick = () => {
    const next = !isOpen
    setOpen(next)
    if (next) reset() // 열릴 때만 읽음 처리 + 깜빡임 OFF
  }

  useEffect(() => {
    const onPointerDown = (e: PointerEvent) => {
      if (!isOpen) return
      const t = e.target as Node
      if (panelRef.current?.contains(t)) return
      if (triggerRef.current?.contains(t)) return
      setOpen(false)
    }

    const opts: AddEventListenerOptions = { capture: true }

    document.addEventListener("pointerdown", onPointerDown, opts)
    return () => document.removeEventListener("pointerdown", onPointerDown, opts)
  }, [isOpen, setOpen])

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
        isOpen={isOpen}
        onClose={() => setOpen(false)}
        returnFocusRef={triggerRef}
        panelRef={panelRef}
      />
    </>
  )
}

export default Header
