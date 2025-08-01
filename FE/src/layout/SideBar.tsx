import { ChatList, Nav } from "@/components/molecules"

function SideBar() {
  return (
    <aside className="bg-background fixed top-[64px] left-0 flex h-[calc(100vh-64px)] w-[230px] flex-col">
      <Nav />
      <div className="flex min-h-0 flex-1 flex-col">
        <div className="text-subtext px-2 py-2 text-xs font-semibold">채팅 목록</div>
        <div className="min-h-0 flex-1 overflow-x-hidden overflow-y-auto">
          <ChatList />
        </div>
      </div>
    </aside>
  )
}

export default SideBar
