import { useState } from "react"
import { ChevronDown, ChevronUp } from "lucide-react"

import { ChatList, Nav } from "@/components/molecules"

function SideBar() {
  const [open, setOpen] = useState(true)

  return (
    <aside className="bg-background fixed top-[64px] left-0 flex h-[calc(100vh-64px)] w-[230px] flex-col">
      <Nav />
      <div className="flex min-h-0 flex-1 flex-col">
        <button
          className="text-subtext mt-5 flex cursor-pointer flex-row items-center justify-start gap-2 px-2 outline-none select-none"
          onClick={() => setOpen((prev) => !prev)}
          type="button"
        >
          {open ? <ChevronDown /> : <ChevronUp />}
          <div className="text-sm font-medium">채팅 목록</div>
        </button>
        <div className="min-h-0 flex-1 overflow-x-hidden overflow-y-auto transition-all">{open && <ChatList />}</div>
      </div>
    </aside>
  )
}

export default SideBar
