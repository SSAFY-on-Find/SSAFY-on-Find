// src/components/templates/ChatView.tsx

import { useQuery } from "@tanstack/react-query"
import { X } from "lucide-react"

import { getChatMessages } from "@/apis/chatRoom"
import { useMyDirectChatRooms } from "@/hooks/useDM"
import { useChatViewStore } from "@/stores/useChatViewStore"
import { useUserStore } from "@/stores/userStore"

import { UserImg } from "../atoms"

import DM from "./DM"

function ChatView() {
  const { activeRoomId, activeRoomType, closeChat } = useChatViewStore()
  const currentUser = useUserStore((state) => state.user)

  const { data: chatRooms } = useMyDirectChatRooms()
  const { data: initialMessages, isLoading: isMessagesLoading } = useQuery({
    queryKey: ["chatMessages", activeRoomId],
    queryFn: () => getChatMessages(activeRoomId!),
    enabled: !!activeRoomId,
  })

  if (activeRoomType !== "direct" || !activeRoomId || !currentUser || !chatRooms) {
    return null
  }

  const currentChatRoom = chatRooms.find((room) => room.chatRoomId === activeRoomId)

  if (!currentChatRoom) {
    closeChat()
    return null
  }

  const members = [
    {
      studentId: Number(currentUser.studentId),
      name: currentUser.name,
      profileImageUrl: undefined,
    },
    {
      studentId: currentChatRoom.targetStudentId,
      name: currentChatRoom.targetUsername,
      profileImageUrl: currentChatRoom.targetProfileImageUrl,
    },
  ]

  return (
    <aside className="border-line shadow- absolute top-30 left-[260px] z-10 flex h-[650px] w-[400px] flex-col overflow-hidden rounded-xl border-1 bg-white shadow-lg">
      <header className="border-line flex items-center justify-between border-b p-4">
        <div className="flex gap-4">
          <UserImg
            name={currentChatRoom.targetUsername}
            size={"s"}
            showTeamBadge={false}
            url={currentChatRoom.targetProfileImageUrl}
          />
          <h3 className="text-lg font-bold">{currentChatRoom.targetUsername}</h3>
        </div>
        <button onClick={closeChat} className="rounded-full p-1 hover:cursor-pointer hover:bg-gray-100">
          <X className="h-5 w-5 text-gray-600" />
        </button>
      </header>

      <div className="min-h-0 flex-1">
        {isMessagesLoading ? (
          <div className="flex h-full items-center justify-center">
            <p>대화 기록을 불러오는 중...</p>
          </div>
        ) : (
          <DM
            roomId={activeRoomId}
            studentId={Number(currentUser.studentId)}
            members={members}
            initialMessages={initialMessages}
          />
        )}
      </div>
    </aside>
  )
}

export default ChatView
