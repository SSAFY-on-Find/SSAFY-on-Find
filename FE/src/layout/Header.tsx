import { Mailbox, Search } from "lucide-react"

function DeadlineNotification() {
  return (
    <div className="border-main flex items-center justify-center gap-3 rounded-md border px-3.5 py-1.5">
      <div className="text-main text-sm font-semibold">팀빌딩 마감시각</div>
      <div className="text-text text-base font-semibold">2025.07.21 10:00</div>
    </div>
  )
}

function AlarmBox() {
  return (
    <div
      className="bg-main flex aspect-square h-[120%] items-center justify-center rounded-full shadow-2xl"
      style={{
        boxShadow: "0 5px 15px -3px rgba(0, 0, 0, 0.10), 0 4px 6px -4px rgba(0, 0, 0, 0.10)",
      }}
    >
      <Mailbox className="h-5 w-5 text-white" />
    </div>
  )
}

interface IHeaderProps {
  classCode?: string
}

function Header({ classCode = "7" }: IHeaderProps) {
  return (
    <header className="fixed top-0 left-0 z-50 flex h-[64px] w-full items-center justify-center gap-3 bg-white px-5 py-4 shadow-xs">
      <div className="text-main flex h-full w-[200px] items-center justify-start gap-2">
        <Search />
        <h1 className="text-l font-bold">SSAFY On Find</h1>
      </div>
      <div className="flex h-full flex-1 items-center justify-between">
        <h1 className="text-text text-xl font-bold">서울 {classCode}반</h1>
        <div className="flex h-full items-center justify-center gap-3">
          <DeadlineNotification />
          <AlarmBox />
        </div>
      </div>
    </header>
  )
}

export default Header
