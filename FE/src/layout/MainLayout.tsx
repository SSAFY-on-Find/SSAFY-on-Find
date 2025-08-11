// src/components/layout/MainLayout.tsx 또는 App.tsx 등

import { Outlet } from "react-router-dom"

import ChatView from "@/components/templates/ChatView"
import { useChatViewStore } from "@/stores/useChatViewStore"

import SideBar from "./SideBar"

function MainLayout() {
  // [수정] activeRoomId 와 activeRoomType 을 모두 가져옵니다.
  const { activeRoomId, activeRoomType } = useChatViewStore()

  return (
    <div className="flex h-screen w-full">
      <SideBar />
      <main className="flex-1 overflow-y-auto bg-gray-50">
        <Outlet />
      </main>

      {/* [수정] activeRoomType이 'direct'일 때만 ChatView를 렌더링합니다. */}
      {activeRoomId && activeRoomType === "direct" && <ChatView />}
    </div>
  )
}

export default MainLayout
